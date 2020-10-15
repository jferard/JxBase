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

package com.github.jferard.jxbase.dialect.foxpro.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.util.BitUtils;

import java.io.IOException;
import java.io.OutputStream;

public class FoxProIntegerAccess implements IntegerAccess {
    @Override
    public int getIntegerValueLength() {
        return 4;
    }

    @Override
    public Long getIntegerValue(final byte[] recordBuffer, final int offset, final int length) {
        assert length == 4;
        return (long) BitUtils
                .makeInt(recordBuffer[offset], recordBuffer[offset + 1], recordBuffer[offset + 2],
                        recordBuffer[offset + 3]);
    }

    @Override
    public void writeIntegerValue(final OutputStream out, final Long value) throws IOException {
        if (value == null) {
            BitUtils.writeEmpties(out, this.getIntegerValueLength());
        } else {
            assert 4 == this.getIntegerValueLength();
            BitUtils.writeLEByte4(out, value.intValue());
        }
    }

    @Override
    public FieldRepresentation getIntegerFieldRepresentation(final String name) {
        return new FieldRepresentation(name, 'I', 4, 0);
    }
}
