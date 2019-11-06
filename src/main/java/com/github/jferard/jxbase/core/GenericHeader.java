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

import com.github.jferard.jxbase.core.field.XBaseField;
import com.github.jferard.jxbase.writer.XBaseWriter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents the file meta data, that is the first part of the header
 */
public class GenericHeader {

    /*
implements XBaseHeader {
    public static GenericHeader create(final byte type, final Date updateDate, final int recordsQty,
                                       final int fullHeaderLength, final int oneRecordLength,
                                       final byte uncompletedTxFlag, final byte encryptionFlag,
                                       final Collection<XBaseField> fields) {
        final Map<String, Object> meta = new HashMap<String, Object>(4);
        meta.put("updateDate", updateDate);
        meta.put("recordsQty", recordsQty);
        meta.put("uncompletedTxFlag", uncompletedTxFlag);
        meta.put("encryptionFlag", encryptionFlag);
        final XBaseMetadata metadata =
                new GenericMetadata(type, 0, fullHeaderLength, oneRecordLength, meta);
        return new GenericHeader(metadata, fields);
    }

    private final Collection<XBaseField> fields;
    private final XBaseMetadata metadata;

    public GenericHeader(final XBaseMetadata metadata, final Collection<XBaseField> fields) {
        this.metadata = metadata;
        this.fields = fields;
    }

    @Override
    public int getFileTypeByte() {
        return this.metadata.getFileTypeByte();
    }

    @Override
    public int getFullHeaderLength() {
        return this.metadata.getFullHeaderLength();
    }

    @Override
    public int getOneRecordLength() {
        return this.metadata.getOneRecordLength();
    }

    @Override
    public Object getMeta(final String name) {
        return this.metadata.get(name);
    }

    @Override
    public Collection<XBaseField> getFields() {
        return this.fields;
    }

    @Override
    public String toString() {
        return "GenericHeader[metadata=" + this.metadata + ", fields=" + this.fields + "]";
    }

    @Override
    public void write(final XBaseWriter writer) throws IOException {
        writer.writeMetaData(this.metadata);
        writer.writeFieldDescriptorArray(this.fields);
    }

     */
}
