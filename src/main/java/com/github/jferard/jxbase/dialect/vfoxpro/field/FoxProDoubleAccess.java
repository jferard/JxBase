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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Access for FoxPro double values
 */
public class FoxProDoubleAccess implements DoubleAccess {
    @Override
    public int getDoubleValueLength() {
        return 8;
    }

    @Override
    public double extractDoubleValue(final byte[] recordBuffer, final int offset, final int length) {
        assert length == 8;
        return ByteBuffer.wrap(recordBuffer, offset, length).getDouble();
    }

    @Override
    public void writeDoubleValue(final OutputStream out, final double value) throws IOException {
        assert 8 == this.getDoubleValueLength();
        new DataOutputStream(out).writeDouble(value);
    }

    @Override
    public FieldRepresentation getDoubleFieldRepresentation(final String fieldName,
                                                            final int fieldNumberOfDecimalPlaces) {
        return new FieldRepresentation(fieldName, 'B', 8, fieldNumberOfDecimalPlaces);
    }
}
