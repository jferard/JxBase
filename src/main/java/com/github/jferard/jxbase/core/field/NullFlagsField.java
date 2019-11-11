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

public class NullFlagsField implements XBaseField {
    private final String name;
    private final int length;

    public NullFlagsField(final String name, final int length) {
        this.name = name;
        this.length = length;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValueByteLength(final XBaseLengths dialect) {
        return ((FoxProDialect) dialect).getNullFlagsFieldLength(this.length);
    }

    @Override
    public void write(final XBaseFieldDescriptorArrayWriter writer, final int offset)
            throws IOException {
        ((FoxProFieldDescriptorArrayWriter) writer)
                .writeNullFlagsField(this.name, this.length, offset);
    }

    @Override
    public byte[] getValue(final XBaseRecordReader reader, final byte[] recordBuffer,
                           final int offset, final int length) throws IOException {
        return ((FoxProRecordReader) reader).getNullFlagsValue(recordBuffer, offset, length);
    }

    @Override
    public void writeValue(final XBaseRecordWriter writer, final Object value) throws IOException {
        ((FoxProRecordWriter) writer).writeNullFlagsValue((byte[]) value, this.length);
    }

    @Override
    public String toStringRepresentation(final XBaseRepresentations dialect) {
        return this.toRepresentation(dialect).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final XBaseRepresentations dialect) {
        return ((FoxProDialect) dialect).getNullFlagsFieldRepresentation(this.name, this.length);
    }

    @Override
    public String toString() {
        return "NullFlagsField[name=" + this.name + ", length=" + this.length + "]";
    }
}
