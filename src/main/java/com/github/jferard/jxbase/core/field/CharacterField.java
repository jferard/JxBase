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

import com.github.jferard.jxbase.core.XBaseLengths;
import com.github.jferard.jxbase.core.XBaseRepresentations;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.writer.internal.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.internal.XBaseRecordWriter;

import java.io.IOException;

public class CharacterField implements XBaseField {
    private final int dataSize;
    private final String name;

    public CharacterField(final String name, final int dataSize) {
        this.name = name;
        this.dataSize = dataSize;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValueByteLength(final XBaseLengths dialect) {
        return dialect.getCharacterValueLength(this.dataSize);
    }

    @Override
    public void write(final XBaseFieldDescriptorArrayWriter writer, final int offset)
            throws IOException {
        writer.writeCharacterField(this.name, this.dataSize, offset);
    }

    @Override
    public String getValue(final XBaseRecordReader reader, final byte[] recordBuffer,
                           final int offset, final int length) throws IOException {
        return reader.getCharacterValue(recordBuffer, offset, length);
    }

    @Override
    public void writeValue(final XBaseRecordWriter writer, final Object value) throws IOException {
        writer.writeCharacterValue((String) value, this.dataSize);
    }

    @Override
    public String toStringRepresentation(final XBaseRepresentations dialect) {
        return dialect.getCharacterFieldRepresentation(this.name, this.dataSize).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final XBaseRepresentations dialect) {
        return dialect.getCharacterFieldRepresentation(this.name, this.dataSize);
    }

    @Override
    public String toString() {
        return "CharacterField[name=" + this.name + ", length=" + this.dataSize + "]";
    }
}
