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

package com.github.jferard.jxbase.core.field;

import com.github.jferard.jxbase.core.FoxProDialect;
import com.github.jferard.jxbase.core.XBaseLengths;
import com.github.jferard.jxbase.core.XBaseRepresentations;
import com.github.jferard.jxbase.reader.internal.FoxProRecordReader;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.writer.internal.FoxProFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.internal.FoxProRecordWriter;
import com.github.jferard.jxbase.writer.internal.XBaseFieldDescriptorArrayWriter;
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
    public int getByteLength(final XBaseLengths dialect) {
        if (dialect instanceof FoxProDialect) {
            return ((FoxProDialect) dialect).getDatetimeFieldLength();
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Date getValue(final XBaseRecordReader reader, final byte[] recordBuffer,
                         final int offset, final int length) throws IOException {
        if (reader instanceof FoxProRecordReader) {
            return ((FoxProRecordReader) reader).getDatetimeValue(recordBuffer, offset, length);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void write(final XBaseFieldDescriptorArrayWriter writer, final int offset)
            throws IOException {
        if (writer instanceof FoxProFieldDescriptorArrayWriter) {
            ((FoxProFieldDescriptorArrayWriter) writer).writeDatetimeField(this.name, offset);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void writeValue(final XBaseRecordWriter writer, final Object value) throws IOException {
        if (writer instanceof FoxProRecordWriter) {
            ((FoxProRecordWriter) writer).writeDatetimeValue((Date) value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toStringRepresentation(final XBaseRepresentations dialect) {
        if (dialect instanceof FoxProDialect) {
            return ((FoxProDialect) dialect).datetimeFieldToStringRepresentation(this.name);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
