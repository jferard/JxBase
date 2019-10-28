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

package com.github.jferard.jxbase.reader;

import com.github.jferard.jxbase.core.DbfField;
import com.github.jferard.jxbase.core.DbfFieldImpl;
import com.github.jferard.jxbase.core.DbfFieldTypeEnum;
import com.github.jferard.jxbase.core.DbfFileTypeEnum;
import com.github.jferard.jxbase.core.DbfMetadata;
import com.github.jferard.jxbase.core.OffsetDbfField;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.IOUtils;
import com.github.jferard.jxbase.util.JdbfUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DbfMetadataReader {

    private DbfFileTypeEnum type;
    private Date updateDate;
    private int recordsQty;
    private int fullHeaderLength;
    private int oneRecordLength;
    private byte uncompletedTxFlag;
    private byte encryptionFlag;
    private HashMap<String, OffsetDbfField<?>> offsetFieldByName;
    private List<DbfField<?>> fields;

    public DbfMetadata read(InputStream dbfInputStream) throws IOException {
        this.parseHeaderFields(dbfInputStream);
        this.parseFieldDescriptorArray(dbfInputStream);
        return DbfMetadata
                .create(this.type, this.updateDate, this.recordsQty, this.fullHeaderLength,
                        this.oneRecordLength, this.uncompletedTxFlag, this.encryptionFlag,
                        this.fields);
    }

    /**
     * https://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm, 1.1 Table File Header
     *
     * @param dbfInputStream the source stream
     */
    private void parseHeaderFields(InputStream dbfInputStream) throws IOException {
        byte[] headerBytes = new byte[JdbfUtils.HEADER_FIELDS_SIZE];
        if (IOUtils.readFully(dbfInputStream, headerBytes) != JdbfUtils.HEADER_FIELDS_SIZE) {
            throw new IOException("The file is corrupted or is not a dbf file");
        }
        this.type = DbfFileTypeEnum.fromInt(headerBytes[0]);
        this.updateDate =
                this.createHeaderUpdateDate(headerBytes[1], headerBytes[2], headerBytes[3]);
        this.recordsQty =
                BitUtils.makeInt(headerBytes[4], headerBytes[5], headerBytes[6], headerBytes[7]);
        this.fullHeaderLength = BitUtils.makeInt(headerBytes[8], headerBytes[9]);
        this.oneRecordLength = BitUtils.makeInt(headerBytes[10], headerBytes[11]);
        // 12-13: Reserved; filled with zeros.
        this.uncompletedTxFlag = headerBytes[14];
        this.encryptionFlag = headerBytes[15];
        // next 16 bytes: for most DBF types these are reserved bytes
    }

    @SuppressWarnings("deprecation")
    private Date createHeaderUpdateDate(byte yearByte, byte monthByte, byte dayByte) {
        final int year;
        switch (this.type) {
            case FoxBASEPlus1:
                year = yearByte;
                break;
            default:
                year = yearByte + 2000 - 1900;
                break;
        }
        return new Date(year, monthByte - 1, dayByte);
    }

    private void parseFieldDescriptorArray(InputStream inputStream) throws IOException {
        this.fields = new ArrayList<DbfField<?>>();
        byte[] fieldBytes = new byte[JdbfUtils.FIELD_RECORD_LENGTH];
        int headerLength = JdbfUtils.HEADER_FIELDS_SIZE;
        int recordLength = 0;
        while (true) {
            if (IOUtils.readFully(inputStream, fieldBytes) != JdbfUtils.FIELD_RECORD_LENGTH) {
                throw new IOException("The file is corrupted or is not a dbf file");
            }

            DbfField<?> field = createDbfField(fieldBytes);
            fields.add(field);

            recordLength += field.getLength();
            headerLength += fieldBytes.length;

            if (IOUtils.isEndOfFieldArray(inputStream, JdbfUtils.HEADER_TERMINATOR)) {
                headerLength += 1;
                recordLength += 1; // +1 for the flag
                break;
            }
        }
        this.checkLengths(headerLength, recordLength);
    }

    private DbfField<?> createDbfField(byte[] fieldBytes) {
        // 1. name
        final String name = getName(fieldBytes);
        // 2. type
        final DbfFieldTypeEnum type = DbfFieldTypeEnum.fromChar((char) fieldBytes[11]);
        // 3. length
        int length = getLength(fieldBytes[16]);
        // 4. number of decimal places
        final byte numberOfDecimalPlaces = fieldBytes[17];

        return DbfFieldImpl.getDbfField(name, type, length, numberOfDecimalPlaces);
    }

    private String getName(byte[] fieldBytes) {
        int nameLength = 0;
        while (nameLength < 11 && fieldBytes[nameLength] != 0x0) {
            nameLength++;
        }
        return new String(fieldBytes, 0, nameLength, JdbfUtils.ASCII_CHARSET);
    }

    private int getLength(byte lenByte) {
        int length = lenByte;
        if (length < 0) {
            length += 256;
        }
        return length;
    }

    private void checkLengths(int headerLength, int recordLength) throws IOException {
        if (headerLength != this.fullHeaderLength) {
            throw new IOException(String.format("Bad header length: expected: %s, actual: %s",
                    this.fullHeaderLength, headerLength));
        }
        if (recordLength != this.oneRecordLength) {
            throw new IOException(String.format("Bad record length: expected: %s, actual: %s",
                    this.oneRecordLength, recordLength));
        }
    }
}
