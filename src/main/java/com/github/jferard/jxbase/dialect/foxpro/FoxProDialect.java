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

package com.github.jferard.jxbase.dialect.foxpro;

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.XBaseReader;
import com.github.jferard.jxbase.dialect.db3memo.DB3MemoDialect;
import com.github.jferard.jxbase.dialect.db3memo.DB3MemoWriter;
import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.reader.GenericReader;
import com.github.jferard.jxbase.reader.internal.XBaseInternalReaderFactory;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.writer.internal.XBaseInternalWriterFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TimeZone;

public class FoxProDialect extends DB3MemoDialect {
    public FoxProDialect(final XBaseFileTypeEnum type) {
        super(type);
    }

    @Override
    public FieldRepresentation getCharacterFieldRepresentation(final String name,
                                                               final int dataSize) {
        return new FieldRepresentation(name, 'C', dataSize & 0xFF, dataSize >> 8);
    }

    public int getDatetimeValueLength() {
        return 8;
    }

    public FieldRepresentation getDatetimeFieldRepresentation(final String name) {
        return new FieldRepresentation(name, 'T', 8, 0);
    }

    public int getNullFlagsFieldLength(final int length) {
        return length;
    }

    public FieldRepresentation getNullFlagsFieldRepresentation(final String name,
                                                               final int length) {
        return new FieldRepresentation(name, '0', length, 0);
    }

    public FieldRepresentation getSmallMemoFieldRepresentation(final String name) {
        return new FieldRepresentation(name, 'M', 4, 0);
    }

    public int getSmallMemoFieldLength() {
        return 4;
    }


    /**
     * https://www.clicketyclick.dk/databases/xbase/format/data_types.html:
     * > Pointer to ASCII text field in memo file 10 digits representing a pointer to a DBT block
     * (default is blanks).
     *
     * @param recordReader
     * @param recordBuffer
     * @param offset
     * @param length
     * @return
     */
    @Override
    public long getOffsetInBlocks(final XBaseRecordReader recordReader, final byte[] recordBuffer,
                                  final int offset, final int length) {
        assert length == 4;
        return BitUtils
                .makeInt(recordBuffer[offset], recordBuffer[offset + 1], recordBuffer[offset + 2],
                        recordBuffer[offset + 3]);
    }

    @Override
    public XBaseReader getReader(final String databaseName, final InputStream resettableInputStream,
                                 final Charset charset) throws IOException {
        final XBaseMemoReader memoReader = this.getMemoReader(databaseName, charset);
        final XBaseInternalReaderFactory readerFactory =
                new FoxProInternalReaderFactory(this, TimeZone.getDefault(), memoReader);
        return new GenericReader(this, resettableInputStream, charset, readerFactory);
    }

    @Override
    public XBaseField getXBaseField(final String name, final byte typeByte, final int length,
                                    final int numberOfDecimalPlaces) {
        switch (typeByte) {
            case 'M':
                if (length != 4) {
                    throw new IllegalArgumentException();
                }
                return new SmallMemoField(name);
            case '0':
                return new NullFlagsField(name, length);
            default:
                return super.getXBaseField(name, typeByte, length, numberOfDecimalPlaces);
        }
    }

    @Override
    public XBaseInternalWriterFactory getInternalWriterFactory(final String databaseName,
                                                               final Charset charset,
                                                               final Map<String, Object> headerMeta)
            throws IOException {
        final File memoFile = new File(databaseName + ".dbt");
        final XBaseMemoWriter memoWriter = DB3MemoWriter.fromChannel(memoFile, charset, headerMeta);
        return new FoxProInternalWriterFactory(this, TimeZone.getDefault(), memoWriter);
    }
}

