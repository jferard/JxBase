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

package com.github.jferard.jxbase.field;

/**
 * A field representation is a dbf typical representation: name, type, length, number of decimals.
 * It can be written to a file, printed to screen, ...
 * It's dialect dependent. Contains
 */
public class FieldRepresentation {
    private final String fieldName;
    private final byte fieldType;
    private final int fieldLength;
    private final int fieldNumberOfDecimalPlaces;

    public FieldRepresentation(final String name, final char type, final int repLength,
                               final int numberOfDecimalPlaces) {
        this.fieldName = name;
        this.fieldType = (byte) type;
        this.fieldLength = repLength;
        this.fieldNumberOfDecimalPlaces = numberOfDecimalPlaces;
    }

    public String getName() {
        return this.fieldName;
    }

    public byte getType() {
        return this.fieldType;
    }

    public int getFieldLength() {
        return this.fieldLength;
    }

    public int getNumberOfDecimalPlaces() {
        return this.fieldNumberOfDecimalPlaces;
    }

    @Override
    public String toString() {
        return this.fieldName + "," + (char) this.fieldType + "," + this.fieldLength + "," +
                this.fieldNumberOfDecimalPlaces;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldRepresentation)) {
            return false;
        }
        final FieldRepresentation other = (FieldRepresentation) o;
        return this.fieldType == other.fieldType && this.fieldLength == other.fieldLength &&
                this.fieldNumberOfDecimalPlaces == other.fieldNumberOfDecimalPlaces &&
                this.fieldName.equals(other.fieldName);
    }

    @Override
    public int hashCode() {
        return ((31 * this.fieldType + this.fieldLength) * 31 + this.fieldNumberOfDecimalPlaces) *
                31 +
                this.fieldName.hashCode();
    }
}
