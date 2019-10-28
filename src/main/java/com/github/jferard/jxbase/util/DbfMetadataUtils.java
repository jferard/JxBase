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

import com.github.jferard.jxbase.core.DbfMemoRecord;
import com.github.jferard.jxbase.core.field.XBaseField;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.DbfMetadata;
import com.github.jferard.jxbase.core.OffsetXBaseField;

import java.util.Date;
import java.util.List;

public class DbfMetadataUtils {
    public static final int BUFFER_SIZE = 8192;

    public static DbfMetadata fromFieldsString(XBaseFileTypeEnum fileType, Date updateDate,
                                               int recordQty, String s) {
        List<XBaseField<?, DbfMemoRecord>> fields = JdbfUtils.createFieldsFromString(s);
        return fromFields(fileType, updateDate, recordQty, fields);
    }

    public static DbfMetadata fromFields(XBaseFileTypeEnum fileType, Date updateDate, int recordQty,
                                         List<XBaseField<?, DbfMemoRecord>> fields) {
        final int fullHeaderLength = calculateFullHeaderLength(fields);
        final int oneRecordLength = calculateOneRecordLength(fields);

        return DbfMetadata
                .create(fileType, updateDate, recordQty, fullHeaderLength, oneRecordLength,
                        JdbfUtils.NULL_BYTE, JdbfUtils.NULL_BYTE, fields);
    }

    public static int calculateOneRecordLength(List<XBaseField<?, DbfMemoRecord>> fields) {
        int result = 0;
        for (XBaseField<?, DbfMemoRecord> field : fields) {
            result += field.getLength();
        }
        return result + 1;
    }

    private static int calculateFullHeaderLength(List<XBaseField<?, DbfMemoRecord>> fields) {
        int result = JdbfUtils.HEADER_FIELDS_SIZE;
        result += JdbfUtils.FIELD_RECORD_LENGTH * fields.size();
        return result + 1;
    }

    public static void writeDbfField(OffsetXBaseField<?, DbfMemoRecord> field, byte[] fieldBytes) {
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
