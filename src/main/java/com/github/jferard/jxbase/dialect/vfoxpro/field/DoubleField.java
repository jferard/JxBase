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

package com.github.jferard.jxbase.dialect.vfoxpro.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A double field
 */
public class DoubleField implements XBaseField<DoubleAccess> {
    private final String fieldName;
    private final int fieldNumberOfDecimalPlaces;

    public DoubleField(final String fieldName, final int fieldNumberOfDecimalPlaces) {
        this.fieldName = fieldName;
        this.fieldNumberOfDecimalPlaces = fieldNumberOfDecimalPlaces;
    }

    @Override
    public String getName() {
        return this.fieldName;
    }

    @Override
    public FieldRepresentation toRepresentation(final DoubleAccess access) {
        return access.getDoubleFieldRepresentation(this.fieldName, this.fieldNumberOfDecimalPlaces);
    }

    @Override
    public String toStringRepresentation(final DoubleAccess access) {
        return this.toRepresentation(access).toString();
    }

    @Override
    public void writeValue(final DoubleAccess access, final OutputStream out, final Object value)
            throws IOException {
        access.writeDoubleValue(out, (Double) value);
    }

    @Override
    public Object extractValue(final DoubleAccess access, final byte[] recordBuffer, final int offset,
                               final int length)
            throws IOException {
        return access.extractDoubleValue(recordBuffer, offset, length);
    }

    @Override
    public int getValueLength(final DoubleAccess access) {
        return 8;
    }

    @Override
    public String toString() {
        return "DoubleField[name=" + this.fieldName + ", numberOfDecimalPlaces=" +
                this.fieldNumberOfDecimalPlaces + "]";
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoubleField)) {
            return false;
        }

        final DoubleField that = (DoubleField) o;
        return this.fieldNumberOfDecimalPlaces == that.fieldNumberOfDecimalPlaces &&
                this.fieldName.equals(that.fieldName);
    }

    @Override
    public int hashCode() {
        return 31 * this.fieldNumberOfDecimalPlaces + this.fieldName.hashCode();
    }
}
