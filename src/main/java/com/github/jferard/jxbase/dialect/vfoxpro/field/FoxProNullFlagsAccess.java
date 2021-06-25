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

package com.github.jferard.jxbase.dialect.vfoxpro.field;

import com.github.jferard.jxbase.field.FieldRepresentation;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Access for FoxPro null flags values
 */
public class FoxProNullFlagsAccess implements NullFlagsAccess {
    @Override
    public int getNullFlagsFieldLength(final int fieldLength) {
        return fieldLength;
    }

    @Override
    public byte[] extractNullFlagsValue(final byte[] recordBuffer, final int offset, final int length) {
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
    public FieldRepresentation getNullFlagsFieldRepresentation(final String fieldName,
                                                               final int fieldLength) {
        return new FieldRepresentation(fieldName, '0', fieldLength, 0);
    }
}
