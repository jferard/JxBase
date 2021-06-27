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
import java.math.BigDecimal;

/**
 * A numeric field.
 */
public class NumericField implements XBaseField<NumericAccess> {
    private final String fieldName;
    private final int fieldLength;
    private final int fieldNumberOfDecimalPlaces;

    public NumericField(final String fieldName, final int fieldLength,
                        final int fieldNumberOfDecimalPlaces) {
        this.fieldName = fieldName;
        this.fieldLength = fieldLength;
        this.fieldNumberOfDecimalPlaces = fieldNumberOfDecimalPlaces;
    }

    /**
     * @return the actual number of decimal places.
     */
    public int getNumberOfDecimalPlaces() {
        return this.fieldNumberOfDecimalPlaces;
    }

    @Override
    public String getName() {
        return this.fieldName;
    }

    @Override
    public int getValueLength(final NumericAccess access) {
        return access.getNumericValueLength(this.fieldLength);
    }

    @Override
    public BigDecimal extractValue(final NumericAccess access, final byte[] recordBuffer,
                                   final int offset) {
        return access
                .extractNumericValue(recordBuffer, offset, this.getValueLength(access), this.fieldNumberOfDecimalPlaces);
    }

    @Override
    public void writeValue(final NumericAccess access, final OutputStream out, final Object value)
            throws IOException {
        access.writeNumericValue(out, (BigDecimal) value, this.fieldLength,
                this.fieldNumberOfDecimalPlaces);
    }

    @Override
    public String toStringRepresentation(final NumericAccess access) {
        return access
                .getNumericFieldRepresentation(this.fieldName, this.fieldLength,
                        this.fieldNumberOfDecimalPlaces)
                .toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final NumericAccess access) {
        return access
                .getNumericFieldRepresentation(this.fieldName, this.fieldLength,
                        this.fieldNumberOfDecimalPlaces);
    }

    @Override
    public String toString() {
        return "NumericField[name=" + this.fieldName + ", length=" + this.fieldLength +
                ", numberOfDecimalPlaces=" + this.fieldNumberOfDecimalPlaces + "]";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NumericField)) {
            return false;
        }

        final NumericField that = (NumericField) o;
        return this.fieldLength == that.fieldLength &&
                this.fieldNumberOfDecimalPlaces == that.fieldNumberOfDecimalPlaces &&
                this.fieldName.equals(that.fieldName);
    }

    @Override
    public int hashCode() {
        return 31 * ((31 * this.fieldLength) + this.fieldNumberOfDecimalPlaces) +
                this.fieldName.hashCode();
    }
}
