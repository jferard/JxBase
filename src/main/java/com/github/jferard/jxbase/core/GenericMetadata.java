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

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.XBaseMetadata;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents the file meta data, that is the first part of the header
 */
public class GenericMetadata implements XBaseMetadata {
    public static GenericMetadata create(final XBaseFileTypeEnum type, final Date updateDate,
                                         final int recordsQty, final int fullHeaderLength,
                                         final int oneRecordLength, final byte uncompletedTxFlag,
                                         final byte encryptionFlag) {
        if (type == null) {
            throw new IllegalArgumentException("File type should not be null");
        }
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("updateDate", updateDate);
        meta.put("recordsQty", recordsQty);
        meta.put("uncompletedTxFlag", uncompletedTxFlag);
        meta.put("encryptionFlag", encryptionFlag);
        return new GenericMetadata(type.toByte(), fullHeaderLength, oneRecordLength, meta);
    }

    private final byte typeByte;
    private final int fullHeaderLength;
    private final int oneRecordLength;
    private final Map<String, Object> meta;

    public GenericMetadata(final byte typeByte, final int fullHeaderLength,
                           final int oneRecordLength, final Map<String, Object> meta) {
        this.typeByte = typeByte;
        this.fullHeaderLength = fullHeaderLength;
        this.oneRecordLength = oneRecordLength;
        this.meta = meta;
    }

    @Override
    public int getFileTypeByte() {
        return this.typeByte;
    }

    @Override
    public int getFullHeaderLength() {
        return this.fullHeaderLength;
    }

    @Override
    public int getOneRecordLength() {
        return this.oneRecordLength;
    }

    @Override
    public Object get(final String key) {
        return this.meta.get(key);
    }

    @Override
    public String toString() {
        return "GenericMetadata[type=" + this.typeByte + ", fullHeaderLength=" + this.
                fullHeaderLength + ", oneRecordLength=" + this.oneRecordLength + ", meta=" +
                this.meta + "]";
    }

    private String formatUpdateDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.meta.get("updateDate"));
    }
}
