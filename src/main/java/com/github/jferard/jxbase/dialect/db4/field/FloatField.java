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

package com.github.jferard.jxbase.dialect.db4.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;

import java.io.IOException;
import java.io.OutputStream;

public class FloatField implements XBaseField<FloatAccess> {
    private final String name;

    public FloatField(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValueLength(final FloatAccess access) {
        return access.getFloatValueLength();
    }

    @Override
    public Object getValue(final FloatAccess access, final byte[] recordBuffer, final int offset,
                           final int length) throws IOException {
        return access.extractFloatValue(recordBuffer, offset, length);
    }

    @Override
    public void writeValue(final FloatAccess access, final OutputStream out, final Object value)
            throws IOException {
        access.writeFloatValue(out, value);
    }

    @Override
    public String toStringRepresentation(final FloatAccess access) {
        return this.toRepresentation(access).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final FloatAccess access) {
        return access.getFloatFieldRepresentation(this.name);
    }

    @Override
    public String toString() {
        return "FloatField[name=" + this.name + "]";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FloatField)) {
            return false;
        }

        final FloatField that = (FloatField) o;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
