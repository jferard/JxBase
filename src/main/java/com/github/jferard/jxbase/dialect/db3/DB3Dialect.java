/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

package com.github.jferard.jxbase.dialect.db3;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.LogicalField;
import com.github.jferard.jxbase.dialect.db2.field.NumericField;
import com.github.jferard.jxbase.dialect.db3.field.DateField;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.db3.reader.DB3InternalReaderFactory;
import com.github.jferard.jxbase.dialect.db3.writer.DB3InternalWriterFactory;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.reader.XBaseInternalReaderFactory;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.XBaseInternalWriterFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TimeZone;

public class DB3Dialect implements XBaseDialect<DB3Dialect, DB3Access> {
    protected final XBaseFileTypeEnum type;
    private final DB3Access access;

    public DB3Dialect(final XBaseFileTypeEnum type, final DB3Access access) {
        this.type = type;
        this.access = access;
    }

    @Override
    public XBaseField<? super DB3Access> getXBaseField(final String name, final byte typeByte,
                                                       final int length,
                                                       final int numberOfDecimalPlaces) {
        switch (typeByte) {
            case 'C':
                return new CharacterField(name, length);
            case 'D':
                if (length != 8) {
                    throw new IllegalArgumentException("A date has 8 chars");
                }
                return new DateField(name);
            case 'L':
                if (length != 1) {
                    throw new IllegalArgumentException("A boolean has one char");
                }
                return new LogicalField(name);
            case 'M':
                if (length != 10) {
                    throw new IllegalArgumentException("A memo address has 10 chars");
                }
                return new MemoField(name);
            case 'N':
                return new NumericField(name, length, numberOfDecimalPlaces);
            default:
                throw new IllegalArgumentException(
                        String.format("'%c' (%d) is not a dbf field type", typeByte, typeByte));
        }
    }

    @Override
    public XBaseFileTypeEnum getType() {
        return this.type;
    }

    @Override
    public int getMetaDataLength() {
        return JxBaseUtils.METADATA_LENGTH;
    }

    @Override
    public int getFieldDescriptorLength() {
        return DB3Utils.DB3_FIELD_DESCRIPTOR_LENGTH;
    }

    @Override
    public int getOptionalLength() {
        return JxBaseUtils.OPTIONAL_LENGTH;
    }

    @Override
    public DB3Access getAccess() {
        return this.access;
    }

    @Override
    public XBaseInternalReaderFactory<DB3Dialect, DB3Access> getInternalReaderFactory(
            final String databaseName, final Charset charset) throws IOException {
        return new DB3InternalReaderFactory(this, TimeZone.getDefault());
    }

    @Override
    public XBaseInternalWriterFactory<DB3Dialect, DB3Access> getInternalWriterFactory(
            final String databaseName, final Charset charset, final Map<String, Object> headerMeta)
            throws IOException {
        return new DB3InternalWriterFactory(this, TimeZone.getDefault());
    }
}
