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

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.core.XBaseLengths;
import com.github.jferard.jxbase.core.XBaseRepresentations;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.writer.internal.XBaseRecordWriter;

import java.io.IOException;
import java.util.Date;

public class DatetimeField implements XBaseField {
    private final String name;

    public DatetimeField(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValueByteLength(final XBaseLengths dialect) {
        return ((FoxProDialect) dialect).getDatetimeValueLength();
    }

    @Override
    public Date getValue(final XBaseRecordReader reader, final byte[] recordBuffer,
                         final int offset, final int length) throws IOException {
        return ((FoxProRecordReader) reader).getDatetimeValue(recordBuffer, offset, length);
    }

    @Override
    public void writeValue(final XBaseRecordWriter writer, final Object value) throws IOException {
        ((FoxProRecordWriter) writer).writeDatetimeValue((Date) value);
    }

    @Override
    public String toStringRepresentation(final XBaseRepresentations dialect) {
        return this.toRepresentation(dialect).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final XBaseRepresentations dialect) {
        return ((FoxProDialect) dialect).getDatetimeFieldRepresentation(this.name);
    }
}