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
import java.math.BigDecimal;

public class NumericField implements XBaseField<NumericAccess> {
    private final String name;
    private final int length;
    private final int numberOfDecimalPlaces;

    public NumericField(final String name, final int length, final int numberOfDecimalPlaces) {
        this.name = name;
        this.length = length;
        this.numberOfDecimalPlaces = numberOfDecimalPlaces;
    }

    public int getNumberOfDecimalPlaces() {
        return this.numberOfDecimalPlaces;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValueByteLength(final NumericAccess dialect) {
        return dialect.getNumericValueLength(this.length);
    }

    @Override
    public BigDecimal getValue(final NumericAccess reader, final byte[] recordBuffer,
                               final int offset, final int length) throws IOException {
        return reader.extractNumericValue(recordBuffer, offset, length, this.numberOfDecimalPlaces);
    }

    @Override
    public void writeValue(final NumericAccess writer, final OutputStream out, final Object value)
            throws IOException {
        writer.writeNumericValue(out, (BigDecimal) value, this.length, this.numberOfDecimalPlaces);
    }

    @Override
    public String toStringRepresentation(final NumericAccess dialect) {
        return dialect
                .getNumericFieldRepresentation(this.name, this.length, this.numberOfDecimalPlaces)
                .toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final NumericAccess dialect) {
        return dialect
                .getNumericFieldRepresentation(this.name, this.length, this.numberOfDecimalPlaces);
    }

    @Override
    public String toString() {
        return "NumericField[name=" + this.name + ", length=" + this.length +
                ", numberOfDecimalPlaces=" + this.numberOfDecimalPlaces + "]";
    }
}
