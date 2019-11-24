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

public class NullFlagsField implements XBaseField<NullFlagsAccess> {
    private final String name;
    private final int length;

    public NullFlagsField(final String name, final int length) {
        this.name = name;
        this.length = length;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValueByteLength(final NullFlagsAccess dialect) {
        return dialect.getNullFlagsFieldLength(this.length);
    }

    @Override
    public byte[] getValue(final NullFlagsAccess reader, final byte[] recordBuffer,
                           final int offset, final int length) throws IOException {
        return reader.getNullFlagsValue(recordBuffer, offset, length);
    }

    @Override
    public void writeValue(final NullFlagsAccess writer, final OutputStream out, final Object value) throws IOException {
        writer.writeNullFlagsValue(out, (byte[]) value, this.length);
    }

    @Override
    public String toStringRepresentation(final NullFlagsAccess dialect) {
        return this.toRepresentation(dialect).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final NullFlagsAccess dialect) {
        return dialect.getNullFlagsFieldRepresentation(this.name, this.length);
    }

    @Override
    public String toString() {
        return "NullFlagsField[name=" + this.name + ", length=" + this.length + "]";
    }
}
