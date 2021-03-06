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

package com.github.jferard.jxbase.dialect.db2.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A character field.
 */
public class CharacterField implements XBaseField<CharacterAccess> {
    private final int fieldLength;
    private final String fieldName;

    public CharacterField(final String fieldName, final int fieldLength) {
        this.fieldName = fieldName;
        this.fieldLength = fieldLength;
    }

    @Override
    public String getName() {
        return this.fieldName;
    }

    @Override
    public int getValueLength(final CharacterAccess access) {
        return access.getCharacterValueLength(this.fieldLength);
    }

    @Override
    public String extractValue(final CharacterAccess access, final byte[] recordBuffer,
                               final int offset) {
        return access.extractCharacterValue(recordBuffer, offset, this.getValueLength(access));
    }

    @Override
    public void writeValue(final CharacterAccess access, final OutputStream out, final Object value)
            throws IOException {
        access.writeCharacterValue(out, (String) value, this.fieldLength);
    }

    @Override
    public String toStringRepresentation(final CharacterAccess access) {
        return access.getCharacterFieldRepresentation(this.fieldName, this.fieldLength).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final CharacterAccess access) {
        return access.getCharacterFieldRepresentation(this.fieldName, this.fieldLength);
    }

    @Override
    public String toString() {
        return "CharacterField[name=" + this.fieldName + ", length=" + this.fieldLength + "]";
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
        return this.fieldLength == that.fieldLength && this.fieldName.equals(that.fieldName);
    }

    @Override
    public int hashCode() {
        return 31 * this.fieldLength + this.fieldName.hashCode();
    }
}
