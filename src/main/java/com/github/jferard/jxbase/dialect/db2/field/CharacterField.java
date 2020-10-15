/*
 * JxBase - Copyright (c) 2019-2020 Julien Férard
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
    private final int length;
    private final String name;

    public CharacterField(final String name, final int length) {
        this.name = name;
        this.length = length;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValueByteLength(final CharacterAccess dialect) {
        return dialect.getCharacterValueLength(this.length);
    }

    @Override
    public String getValue(final CharacterAccess reader, final byte[] recordBuffer,
                           final int offset, final int length) throws IOException {
        return reader.extractCharacterValue(recordBuffer, offset, length);
    }

    @Override
    public void writeValue(final CharacterAccess writer, final OutputStream out, final Object value)
            throws IOException {
        writer.writeCharacterValue(out, (String) value, this.length);
    }

    @Override
    public String toStringRepresentation(final CharacterAccess dialect) {
        return dialect.getCharacterFieldRepresentation(this.name, this.length).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final CharacterAccess dialect) {
        return dialect.getCharacterFieldRepresentation(this.name, this.length);
    }

    @Override
    public String toString() {
        return "CharacterField[name=" + this.name + ", length=" + this.length + "]";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CharacterField)) {
            return false;
        }

        final CharacterField that = (CharacterField) o;
        return this.length == that.length && this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return 31 * this.length + this.name.hashCode();
    }
}
