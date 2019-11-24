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

import java.io.IOException;
import java.io.OutputStream;

public class FoxProNullFlagsAccess implements NullFlagsAccess {
    @Override
    public int getNullFlagsFieldLength(final int dataSize) {
        return dataSize;
    }

    @Override
    public byte[] getNullFlagsValue(final byte[] recordBuffer, final int offset, final int length) {
        final byte[] bytes = new byte[length];
        System.arraycopy(recordBuffer, offset, bytes, 0, length);
        return bytes;
    }

    @Override
    public void writeNullFlagsValue(final OutputStream out, final byte[] value, final int length)
            throws IOException {
        out.write(value, 0, length);
    }

    @Override
    public FieldRepresentation getNullFlagsFieldRepresentation(final String name,
                                                               final int length) {
        return new FieldRepresentation(name, '0', length, 0);
    }
}
