/*
 * JxBase - Copyright (c) 2019 Julien Férard
 * JDBF - Copyright (c) 2012-2018 Ivan Ryndin (https://github.com/iryndin)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.jferard.jxbase.util;

import com.github.jferard.jxbase.core.DbfField;
import com.github.jferard.jxbase.core.DbfFieldTypeEnum;
import com.github.jferard.jxbase.core.DbfFileTypeEnum;
import com.github.jferard.jxbase.core.DbfMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbfMetadataUtils {

    public static DbfMetadata fromFieldsString(String s) throws IOException {
        List<DbfField> fields = JdbfUtils.createFieldsFromString(s);

        DbfMetadata metadata = new DbfMetadata();

        metadata.setType(DbfFileTypeEnum.FoxBASEPlus1);
        metadata.setUpdateDate(new Date());
        //metadata.setRecordsQty(recordsQty);
        int fullHeaderLength = calculateFullHeaderLength(fields);
        metadata.setFullHeaderLength(fullHeaderLength);
        int oneRecordLength = calculateOneRecordLength(fields);
        metadata.setOneRecordLength(oneRecordLength);

        metadata.setFields(fields);

        return metadata;
    }

    public static DbfMetadata fromFields(List<DbfField> fields, DbfFileTypeEnum fileType)
            throws IOException {
        DbfMetadata metadata = new DbfMetadata();

        metadata.setType(fileType);
        metadata.setUpdateDate(new Date());
        int fullHeaderLength = calculateFullHeaderLength(fields);
        metadata.setFullHeaderLength(fullHeaderLength);
        int oneRecordLength = calculateOneRecordLength(fields);
        metadata.setOneRecordLength(oneRecordLength);

        metadata.setFields(fields);

        return metadata;
    }

    public static int calculateOneRecordLength(List<DbfField> fields) {
        int result = 0;
        for (DbfField field : fields) {
            result += field.getLength();
        }
        result++;
        return result;
    }

    private static int calculateFullHeaderLength(List<DbfField> fields) {
        int result = 32;
        result += 32 * fields.size();
        result++;
        return result;
    }

    //	public static byte[] toByteArray(DbfMetadata metadata) {
    //
    //	}

    /**
     * https://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm, 1.1 Table File Header
     *
     * @param metadata    the metadata destination
     * @param headerBytes the source bytes
     * @throws IOException
     */
    public static void fillHeaderFields(DbfMetadata metadata, byte[] headerBytes)
            throws IOException {
        metadata.setType(DbfFileTypeEnum.fromInt(headerBytes[0]));
        metadata.setUpdateDate(parseHeaderUpdateDate(headerBytes[1], headerBytes[2], headerBytes[3],
                metadata.getFileType()));
        metadata.setRecordsQty(
                BitUtils.makeInt(headerBytes[4], headerBytes[5], headerBytes[6], headerBytes[7]));
        metadata.setFullHeaderLength(BitUtils.makeInt(headerBytes[8], headerBytes[9]));
        metadata.setOneRecordLength(BitUtils.makeInt(headerBytes[10], headerBytes[11]));
        // 12-13: Reserved; filled with zeros.
        metadata.setUncompletedTxFlag(headerBytes[14]);
        metadata.setEncryptionFlag(headerBytes[15]);
    }

    @SuppressWarnings("deprecation")
    public static Date parseHeaderUpdateDate(byte yearByte, byte monthByte, byte dayByte,
                                             DbfFileTypeEnum fileType) {
        int year = yearByte + 2000 - 1900;
        switch (fileType) {
            case FoxBASEPlus1:
                year = yearByte;
        }
        int month = monthByte - 1;
        int day = dayByte;
        return new Date(year, month, day);

    }

    public static void readFields(DbfMetadata metadata, InputStream inputStream)
            throws IOException {
        List<DbfField> fields = new ArrayList<DbfField>();
        byte[] fieldBytes = new byte[JdbfUtils.FIELD_RECORD_LENGTH];
        int headerLength = 0;
        int fieldLength = 0;
        while (true) {
            if (inputStream.read(fieldBytes) != JdbfUtils.FIELD_RECORD_LENGTH) {
                throw new IOException("The file is corrupted or is not a dbf file");
            }

            DbfField field = createDbfField(fieldBytes);
            fields.add(field);

            fieldLength += field.getLength();
            headerLength += fieldBytes.length;

            long oldAvailable = inputStream.available();
            int terminator = inputStream.read();
            if (terminator == -1) {
                throw new IOException("The file is corrupted or is not a dbf file");
            } else if (terminator == JdbfUtils.HEADER_TERMINATOR) {
                break;
            } else {
                inputStream.reset();
                inputStream.skip(inputStream.available() - oldAvailable);
            }
        }
        fieldLength += 1;
        headerLength += 32;
        headerLength += 1;

        if (headerLength != metadata.getFullHeaderLength()) {
            // TODO: handle this anyway!
        }
        if (fieldLength != metadata.getOneRecordLength()) {
            // TODO: handle this anyway!
        }

        metadata.setFields(fields);
    }

    public static DbfField createDbfField(byte[] fieldBytes) {
        // 1. name
        int nameLength = 0;
        while (nameLength < 11 && fieldBytes[nameLength] > 0) { // maybe != 0
            nameLength++;
        }
        final String name = new String(fieldBytes, 0, nameLength);
        // 2. type
        final DbfFieldTypeEnum type = DbfFieldTypeEnum.fromChar((char) fieldBytes[11]);
        // 3. length
        int length = fieldBytes[16];
        if (length < 0) {
            length += 256;
        }
        // 4. number of decimal places
        final byte numberOfDecimalPlaces = fieldBytes[17];

        return new DbfField(name, type, length, numberOfDecimalPlaces);
    }

    public static void writeDbfField(DbfField field, byte[] fieldBytes) {
        BitUtils.memset(fieldBytes, 0);
        byte[] nameBytes = field.getName().getBytes();
        int nameLength = nameBytes.length;
        if (nameLength > 11) {
            // throw error here!
        }
        System.arraycopy(nameBytes, 0, fieldBytes, 0, nameBytes.length);
        fieldBytes[11] = field.getType().toByte();
        byte[] b = BitUtils.makeByte4(field.getOffset());
        fieldBytes[12] = b[0];
        fieldBytes[13] = b[1];
        fieldBytes[14] = b[2];
        fieldBytes[15] = b[3];
        int length = field.getLength();
        fieldBytes[16] = (byte) (length & 0xff);
        fieldBytes[17] = (byte) (field.getNumberOfDecimalPlaces() & 0xff);
    }

    @SuppressWarnings("deprecation")
    public static byte[] toByteArrayHeader(DbfMetadata metadata) {
        byte[] headerBytes = new byte[16];
        BitUtils.memset(headerBytes, 0);


        headerBytes[0] = metadata.getFileType().toByte();

        Date updateDate = metadata.getUpdateDate();
        // date
        if (updateDate == null) {
            updateDate = new Date();
        }
        // write date bytes
        {
            byte[] dateBytes = JdbfUtils.writeDateForHeader(updateDate);
            headerBytes[1] = dateBytes[0];
            headerBytes[2] = dateBytes[1];
            headerBytes[3] = dateBytes[2];
        }

        byte[] b = BitUtils.makeByte4(metadata.getRecordsQty());
        headerBytes[4] = b[0];
        headerBytes[5] = b[1];
        headerBytes[6] = b[2];
        headerBytes[7] = b[3];

        b = BitUtils.makeByte2(metadata.getFullHeaderLength());
        headerBytes[8] = b[0];
        headerBytes[9] = b[1];

        b = BitUtils.makeByte2(metadata.getOneRecordLength());
        headerBytes[10] = b[0];
        headerBytes[11] = b[1];

        headerBytes[12] = 0;
        headerBytes[13] = 0;

        headerBytes[14] = metadata.getUncompletedTxFlag();
        headerBytes[15] = metadata.getEncryptionFlag();

        return headerBytes;
    }
}
