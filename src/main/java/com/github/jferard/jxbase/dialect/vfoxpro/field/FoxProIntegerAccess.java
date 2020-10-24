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
import com.github.jferard.jxbase.util.BytesUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Access for FoxPro integer values
 */
public class FoxProIntegerAccess implements IntegerAccess {
    @Override
    public int getIntegerValueLength() {
        return 4;
    }

    @Override
    public Long extractIntegerValue(final byte[] recordBuffer, final int offset, final int length) {
        assert length == 4;
        return (long) BytesUtils
                .extractLEInt4(recordBuffer, offset);
    }

    @Override
    public void writeIntegerValue(final OutputStream out, final Long value) throws IOException {
        if (value == null) {
            BytesUtils.writeEmpties(out, this.getIntegerValueLength());
        } else {
            assert 4 == this.getIntegerValueLength();
            BytesUtils.writeLEByte4(out, value.intValue());
        }
    }

    @Override
    public FieldRepresentation getIntegerFieldRepresentation(final String fieldName) {
        return new FieldRepresentation(fieldName, 'I', 4, 0);
    }
}
