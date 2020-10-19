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

package com.github.jferard.jxbase.dialect.foxpro.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class DoubleField implements XBaseField<DoubleAccess> {
    private final String name;
    private final int numberOfDecimalPlaces;

    public DoubleField(final String name, final int numberOfDecimalPlaces) {
        this.name = name;
        this.numberOfDecimalPlaces = numberOfDecimalPlaces;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public FieldRepresentation toRepresentation(final DoubleAccess access) {
        return access.getDoubleFieldRepresentation(this.name, this.numberOfDecimalPlaces);
    }

    @Override
    public String toStringRepresentation(final DoubleAccess access) {
        return this.toRepresentation(access).toString();
    }

    @Override
    public void writeValue(final DoubleAccess access, final OutputStream out, final Object value)
            throws IOException {
        // TODO
    }

    @Override
    public Object getValue(final DoubleAccess access, final byte[] recordBuffer, final int offset,
                           final int length)
            throws IOException {
        return ByteBuffer.wrap(recordBuffer, offset, length).getDouble();
    }

    @Override
    public int getValueByteLength(final DoubleAccess access) {
        return 8;
    }

    @Override
    public String toString() {
        return "DoubleField[name=" + this.name + ", numberOfDecimalPlaces=" +
                this.numberOfDecimalPlaces + "]";
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
        return this.numberOfDecimalPlaces == that.numberOfDecimalPlaces &&
                this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return 31 * this.numberOfDecimalPlaces + this.name.hashCode();
    }
}
