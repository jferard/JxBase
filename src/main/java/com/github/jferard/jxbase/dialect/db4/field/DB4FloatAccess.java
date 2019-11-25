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

package com.github.jferard.jxbase.dialect.db4.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.RawRecordReader;
import com.github.jferard.jxbase.field.RawRecordWriter;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

public class DB4FloatAccess implements FloatAccess {
    public static final CharSequence NUMERIC_OVERFLOW = "*";
    private final RawRecordReader rawRecordReader;
    private final RawRecordWriter rawRecordWriter;

    public DB4FloatAccess(final RawRecordReader rawRecordReader,
                          final RawRecordWriter rawRecordWriter) {
        this.rawRecordReader = rawRecordReader;
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
                this.rawRecordReader.extractTrimmedASCIIString(recordBuffer, offset, length);
        if (s == null || s.contains(NUMERIC_OVERFLOW)) {
            return null;
        }
        return new BigDecimal(s);
    }

    @Override
    public void writeFloatValue(final OutputStream out, final Object value) throws IOException {
        if (value == null) {
            this.rawRecordWriter.writeEmpties(out, 20);
        } else if (value instanceof Number) {
            final String s = String.valueOf(value);
            final byte[] numberBytes = s.getBytes(JxBaseUtils.ASCII_CHARSET);
            final int missingCount = 20 - numberBytes.length;
            if (missingCount >= 0) {
                this.writeNumeric(out, numberBytes, missingCount);
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void writeNumeric(final OutputStream out, final byte[] numberBytes,
                              final int missingCount) throws IOException {
        final byte[] bytes = new byte[20];
        for (int i = 0; i < missingCount; i++) {
            bytes[i] = (byte) JxBaseUtils.EMPTY;
        }
        for (int i = 0; i < numberBytes.length; i++) {
            bytes[missingCount + i] = numberBytes[i];
        }
        this.rawRecordWriter.write(out, bytes);
    }

    @Override
    public FieldRepresentation getFloatFieldRepresentation(final String name) {
        return new FieldRepresentation(name, 'F', 20, 0);
    }
}
