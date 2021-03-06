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

package com.github.jferard.jxbase.dialect.db2;

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.DB2CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2NumericAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalField;
import com.github.jferard.jxbase.dialect.db2.field.NumericAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericField;
import com.github.jferard.jxbase.dialect.db2.reader.DB2ChunkReaderFactory;
import com.github.jferard.jxbase.dialect.db2.writer.DB2ChunkWriterFactory;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.reader.XBaseChunkReaderFactory;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.XBaseChunkWriterFactory;

import java.nio.charset.Charset;
import java.util.TimeZone;

public class DB2Dialect implements XBaseDialect<DB2Access, DB2Dialect> {
    public static DB2Dialect create(final XBaseFileTypeEnum type, final Charset charset) {
        final RawRecordReadHelper rawRecordReader = new RawRecordReadHelper(charset);
        final RawRecordWriteHelper rawRecordWriter = new RawRecordWriteHelper(charset);
        final CharacterAccess characterDialect =
                new DB2CharacterAccess(rawRecordReader, rawRecordWriter);
        final LogicalAccess logicalDialect = new DB2LogicalAccess(rawRecordWriter);
        final NumericAccess numericDialect = new DB2NumericAccess(rawRecordWriter);
        final DB2Access access = new DB2Access(characterDialect, logicalDialect, numericDialect);
        return new DB2Dialect(type, access);
    }

    protected final XBaseFileTypeEnum type;
    private final DB2Access access;

    public DB2Dialect(final XBaseFileTypeEnum type, final DB2Access access) {
        this.type = type;
        this.access = access;
    }


    @Override
    public XBaseField<? super DB2Access> createXBaseField(final String name, final byte typeByte,
                                                          final int length,
                                                          final int numberOfDecimalPlaces) {
        switch (typeByte) {
            case 'C':
                return new CharacterField(name, length);
            case 'L':
                if (length != 1) {
                    throw new IllegalArgumentException("A boolean has one char");
                }
                return new LogicalField(name);
            case 'N':
                return new NumericField(name, length, numberOfDecimalPlaces);
            default:
                throw new IllegalArgumentException(
                        String.format("'%c' (%d) is not a DB2 field type", typeByte, typeByte));
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
        return DB2Utils.DB2_FIELD_DESCRIPTOR_LENGTH;
    }

    @Override
    public int getOptionalLength() {
        return 0;
    }

    @Override
    public DB2Access getAccess() {
        return this.access;
    }

    @Override
    public XBaseChunkReaderFactory<DB2Access, DB2Dialect> getInternalReaderFactory() {
        return new DB2ChunkReaderFactory(this, TimeZone.getDefault());
    }

    @Override
    public XBaseChunkWriterFactory<DB2Access, DB2Dialect> getInternalWriterFactory() {
        return new DB2ChunkWriterFactory(this, TimeZone.getDefault());
    }
}
