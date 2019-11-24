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

package com.github.jferard.jxbase.dialect.db2.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;

import java.io.IOException;
import java.io.OutputStream;

public class CharacterField implements XBaseField<CharacterAccess> {
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
    public int getValueByteLength(final CharacterAccess dialect) {
        return dialect.getCharacterValueLength(this.dataSize);
    }

    @Override
    public String getValue(final CharacterAccess reader, final byte[] recordBuffer,
                           final int offset, final int length) throws IOException {
        return reader.extractCharacterValue(recordBuffer, offset, length);
    }

    @Override
    public void writeValue(final CharacterAccess writer, final OutputStream out,
                           final Object value) throws IOException {
        writer.writeCharacterValue(out, (String) value, this.dataSize);
    }

    @Override
    public String toStringRepresentation(final CharacterAccess dialect) {
        return dialect.getCharacterFieldRepresentation(this.name, this.dataSize).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final CharacterAccess dialect) {
        return dialect.getCharacterFieldRepresentation(this.name, this.dataSize);
    }

    @Override
    public String toString() {
        return "CharacterField[name=" + this.name + ", length=" + this.dataSize + "]";
    }
}
