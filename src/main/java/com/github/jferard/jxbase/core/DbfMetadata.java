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

package com.github.jferard.jxbase.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents the file meta data, read from the first part of the header
 */
public class DbfMetadata {
    public static DbfMetadata create(DbfFileTypeEnum type, Date updateDate, int recordsQty,
                                     int fullHeaderLength, int oneRecordLength,
                                     byte uncompletedTxFlag, byte encryptionFlag,
                                     List<DbfField> fields) {
        Map<String, OffsetDbfField> offsetFieldByName =
                new LinkedHashMap<String, OffsetDbfField>(fields.size() * 2);
        int offset = 1;
        for (DbfField f : fields) {
            offsetFieldByName.put(f.getName(), new OffsetDbfField(f, offset));
            offset += f.getLength();
        }
        fields = new ArrayList<DbfField>(fields);
        return new DbfMetadata(type, updateDate, recordsQty, fullHeaderLength, oneRecordLength,
                uncompletedTxFlag, encryptionFlag, fields, offsetFieldByName);
    }

    private final DbfFileTypeEnum type;
    private final Date updateDate;
    private final int recordsQty;
    private final int fullHeaderLength;
    private final int oneRecordLength;
    private final byte uncompletedTxFlag;
    private final byte encryptionFlag;
    private final Map<String, OffsetDbfField> offsetFieldByName;
    private final List<DbfField> fields;

    public DbfMetadata(DbfFileTypeEnum type, Date updateDate, int recordsQty, int fullHeaderLength,
                       int oneRecordLength, byte uncompletedTxFlag, byte encryptionFlag,
                       List<DbfField> fields, Map<String, OffsetDbfField> offsetFieldByName) {
        if (type == null) {
            throw new IllegalArgumentException("File type should not be null");
        }
        this.type = type;
        this.updateDate = updateDate;
        this.recordsQty = recordsQty;
        this.fullHeaderLength = fullHeaderLength;
        this.oneRecordLength = oneRecordLength;
        this.uncompletedTxFlag = uncompletedTxFlag;
        this.encryptionFlag = encryptionFlag;
        this.fields = fields;
        this.offsetFieldByName = offsetFieldByName;
    }

    public DbfFileTypeEnum getFileType() {
        return type;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public int getRecordsQty() {
        return recordsQty;
    }

    public int getFullHeaderLength() {
        return fullHeaderLength;
    }

    public int getOneRecordLength() {
        return oneRecordLength;
    }

    public byte getUncompletedTxFlag() {
        return uncompletedTxFlag;
    }

    public byte getEncryptionFlag() {
        return encryptionFlag;
    }

    public OffsetDbfField getOffsetField(String name) {
        return offsetFieldByName.get(name);
    }

    public Collection<OffsetDbfField> getOffsetFields() {
        return offsetFieldByName.values();
    }

    public Collection<DbfField> getFields() {
        return fields;
    }

    public String getFieldsStringRepresentation() {
        if (offsetFieldByName == null) {
            return null;
        }
        int i = offsetFieldByName.size();
        // i*64 - just to allocate enough space
        StringBuilder sb = new StringBuilder(i * 64);
        for (OffsetDbfField of : offsetFieldByName.values()) {
            sb.append(of.toString());
            i--;
            if (i > 0) {
                sb.append("|");
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "DbfMetadata[type=" + type + ", updateDate=" + formatUpdateDate() + ", recordsQty=" +
                recordsQty + ", fullHeaderLength=" + fullHeaderLength + ", oneRecordLength=" +
                oneRecordLength + ", uncompletedTxFlag=" + uncompletedTxFlag + ", encryptionFlag=" +
                encryptionFlag + ", fields=" + getFieldsStringRepresentation() + "]";
    }

    private String formatUpdateDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(updateDate);
    }
}
