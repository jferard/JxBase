/*
* JxBase - Copyright (c) 2019-2021 Julien Férard
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

package com.github.jferard.jxbase.dialect.vfoxpro;

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.LogicalField;
import com.github.jferard.jxbase.dialect.db2.field.NumericField;
import com.github.jferard.jxbase.dialect.db3.DB3Utils;
import com.github.jferard.jxbase.dialect.db3.field.DateField;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.db4.field.FloatField;
import com.github.jferard.jxbase.dialect.vfoxpro.field.CurrencyField;
import com.github.jferard.jxbase.dialect.vfoxpro.field.DatetimeField;
import com.github.jferard.jxbase.dialect.vfoxpro.field.DoubleField;
import com.github.jferard.jxbase.dialect.vfoxpro.field.IntegerField;
import com.github.jferard.jxbase.dialect.vfoxpro.field.NullFlagsField;
import com.github.jferard.jxbase.dialect.vfoxpro.reader.VisualFoxProChunksReaderFactory;
import com.github.jferard.jxbase.dialect.vfoxpro.writer.VisualFoxProChunkWriterFactory;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.reader.XBaseChunkReaderFactory;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.XBaseChunkWriterFactory;

import java.util.TimeZone;

/**
 * The VisualFoxPro dialect.
 */
public class VisualFoxProDialect implements XBaseDialect<VisualFoxProAccess, VisualFoxProDialect> {
    protected final XBaseFileTypeEnum type;
    private final VisualFoxProAccess access;

    public VisualFoxProDialect(final XBaseFileTypeEnum type, final VisualFoxProAccess access) {
        this.type = type;
        this.access = access;
    }

    @Override
    public XBaseField<? super VisualFoxProAccess> createXBaseField(final String name, final byte typeByte,
                                                                   final int length,
                                                                   final int numberOfDecimalPlaces) {
        switch (typeByte) {
            case 'B':
                // length is ignored
                return new DoubleField(name, numberOfDecimalPlaces);
            case 'C':
                return new CharacterField(name, length);
            case 'D':
                if (length != 8) {
                    throw new IllegalArgumentException("A date has 8 chars");
                }
                return new DateField(name);
            case 'F':
                if (length != 20) {
                    throw new IllegalArgumentException("A float has 20 chars");
                }
                return new FloatField(name);
            case 'I':
                if (length != 4) {
                    throw new IllegalArgumentException("An integer has 4 bytes");
                }
                return new IntegerField(name);
            case 'L':
                if (length != 1) {
                    throw new IllegalArgumentException("A boolean has one char");
                }
                return new LogicalField(name);
            case 'M':
                if (length != 4) {
                    throw new IllegalArgumentException(String.format("A memo offset has 4 bytes, was %d", length));
                }
                return new MemoField(name);
            case 'N':
                return new NumericField(name, length, numberOfDecimalPlaces);
            case 'T':
                if (length != 8) {
                    throw new IllegalArgumentException("A date time has 8 chars");
                }
                return new DatetimeField(name);
            case 'Y':
                if (length != 8) {
                    throw new IllegalArgumentException("A currency has 8 bytes");
                }
                return new CurrencyField(name);
            case '0':
                return new NullFlagsField(name, length);
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
        return JxBaseUtils.VISUAL_FOXPRO_OPTIONAL_LENGTH;
    }

    @Override
    public VisualFoxProAccess getAccess() {
        return this.access;
    }

    @Override
    public XBaseChunkReaderFactory<VisualFoxProAccess, VisualFoxProDialect> getInternalReaderFactory() {
        return new VisualFoxProChunksReaderFactory(this, TimeZone.getDefault());
    }

    @Override
    public XBaseChunkWriterFactory<VisualFoxProAccess, VisualFoxProDialect> getInternalWriterFactory() {
        return new VisualFoxProChunkWriterFactory(this, TimeZone.getDefault());
    }
}

