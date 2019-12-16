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

import com.github.jferard.jxbase.dialect.foxpro.FoxProUtils;
import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.util.BitUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;

public class FoxProDatetimeAccess implements DatetimeAccess {

    @Override
    public int getDatetimeValueLength() {
        return 8;
    }

    @Override
    public Date getDatetimeValue(final byte[] recordBuffer, final int offset, final int length) {
        assert length == 8;
        return new Date(FoxProUtils.julianDaysToDate(recordBuffer[offset], recordBuffer[offset + 1],
                recordBuffer[offset + 2], recordBuffer[offset + 3]).getTime() + FoxProUtils
                .toMillis(recordBuffer[offset + 4], recordBuffer[offset + 5],
                        recordBuffer[offset + 6], recordBuffer[offset + 7]));
    }

    @Override
    public void writeDatetimeValue(final OutputStream out, final Date value) throws IOException {
        final int fieldLength = 8;
        if (value == null) {
            BitUtils.writeEmpties(out, fieldLength);
        } else {
            final long time = value.getTime();
            BitUtils.writeLEByte4(out, FoxProUtils.dateToJulianDays(value));
            BitUtils.writeLEByte4(out, FoxProUtils.millisFromDate(value));
        }
    }

    @Override
    public FieldRepresentation getDatetimeRepresentation(final String name) {
        return new FieldRepresentation(name, 'T', 8, 0);
    }
}
