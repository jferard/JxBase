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

package com.github.jferard.jxbase.dialect.db4.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;
import com.github.jferard.jxbase.util.BytesUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 * Access (read/write) for DB4 float.
 */
public class DB4FloatAccess implements FloatAccess {
    public static final CharSequence NUMERIC_OVERFLOW = "*";
    private final RawRecordWriteHelper rawRecordWriter;

    public DB4FloatAccess(final RawRecordWriteHelper rawRecordWriter) {
        this.rawRecordWriter = rawRecordWriter;
    }

    @Override
    public int getFloatValueLength() {
        return 20;
    }

    @Override
    public BigDecimal extractFloatValue(final byte[] recordBuffer, final int offset,
                                        final int length) {
        final String s =
                BytesUtils.extractTrimmedASCIIString(recordBuffer, offset, length);
        if (s == null || s.contains(NUMERIC_OVERFLOW)) {
            return null;
        }
        return new BigDecimal(s);
    }

    @Override
    public void writeFloatValue(final OutputStream out, final Number value) throws IOException {
        if (value == null) {
            this.rawRecordWriter.writeEmpties(out, 20);
        } else {
            final String s = String.valueOf(value);
            final byte[] numberBytes = s.getBytes(JxBaseUtils.ASCII_CHARSET);
            final int missingCount = 20 - numberBytes.length;
            if (missingCount >= 0) {
                this.writeNumeric(out, numberBytes, missingCount);
            } else {
                throw new IllegalArgumentException("Number too long");
            }
        }
    }

    private void writeNumeric(final OutputStream out, final byte[] numberBytes,
                              final int missingCount) throws IOException {
        final byte[] buffer = new byte[20];
        for (int i = 0; i < missingCount; i++) {
            buffer[i] = (byte) JxBaseUtils.EMPTY;
        }
        System.arraycopy(numberBytes, 0, buffer, missingCount, numberBytes.length);
        this.rawRecordWriter.write(out, buffer);
    }

    @Override
    public FieldRepresentation getFloatFieldRepresentation(final String fieldName) {
        return new FieldRepresentation(fieldName, 'F', 20, 0);
    }
}
