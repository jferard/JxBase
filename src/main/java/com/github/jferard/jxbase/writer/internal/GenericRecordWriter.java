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

package com.github.jferard.jxbase.writer.internal;

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseMemoRecord;
import com.github.jferard.jxbase.core.field.XBaseField;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.JdbfUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class GenericRecordWriter implements XBaseRecordWriter {
    public static final ThreadLocal<SimpleDateFormat> DATE_FORMAT =
            new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("yyyyMMdd");
                }
            };

    final XBaseDialect dialect;
    final OutputStream out;
    final Charset charset;
    private final Collection<XBaseField> fields;
    private int recordCount;

    public GenericRecordWriter(final XBaseDialect dialect, final OutputStream out,
                               final Charset charset, final Collection<XBaseField> fields) {
        this.dialect = dialect;
        this.fields = fields;
        this.out = out;
        this.charset = charset;
        this.recordCount = 0;
    }

    @Override
    public void write(final Map<String, Object> objectByName) throws IOException {
        this.out.write(JdbfUtils.EMPTY);
        for (final XBaseField field : this.fields) {
            final Object value = objectByName.get(field.getName());
            field.writeValue(this, value);
        }
        this.recordCount++;
    }

    @Override
    public <V> void writeValue(final XBaseField field, final V value) throws IOException {
        throw new IOException("Can't write " + field);
    }

    @Override
    public void writeLogicalValue(final Boolean value) throws IOException {
        final byte[] bytes;
        if (value == null) {
            bytes = "?".getBytes(JdbfUtils.ASCII_CHARSET);
        } else {
            final String s = value ? "T" : "F";
            bytes = s.getBytes();
        }
        this.out.write(bytes);
    }

    @Override
    public <T extends XBaseMemoRecord<?>> void writeMemoValue(final T value) {
        // TODO
    }

    @Override
    public void writeCharacterValue(final String value, final int length) throws IOException {
        if (value == null) {
            BitUtils.writeEmpties(this.out, length);
        } else {
            final byte[] bytes = value.getBytes(this.charset);
            if (bytes.length >= length) {
                for (int i = 0; i < length; i++) {
                    this.out.write(bytes[i]);
                }
            } else {
                this.out.write(bytes);
                BitUtils.writeEmpties(this.out, length - bytes.length);
            }
        }
    }

    @Override
    public void writeDateValue(final Date value) throws IOException {
        final int fieldLength = 8;
        if (value == null) {
            BitUtils.writeZeroes(this.out, fieldLength);
        } else {
            final String s = DATE_FORMAT.get().format(value);
            this.out.write(s.getBytes(JdbfUtils.ASCII_CHARSET));
        }
    }

    @Override
    public void writeNumericValue(final BigDecimal value, final int length,
                                  final int numberOfDecimalPlaces) throws IOException {
        if (value == null) {
            BitUtils.writeEmpties(this.out, length);
        } else {
            String str = String.format("% 20.18f",
                    value); // Whitespace pad; 20 min length; 18 max precision
            if (str.length() > 20) { // Trim to 20 places, if longer
                str = str.substring(0, 20);
            }
            this.out.write(str.getBytes(JdbfUtils.ASCII_CHARSET));
        }
    }

    // TODO: Appears to be 64 bit epoch timestamp, but there was no reliable source for that
    @Override
    public void writeDatetimeValue(final Date value) throws IOException {
        final int fieldLength = 8;
        if (value == null) {
            BitUtils.writeEmpties(this.out, fieldLength);
        } else {
            BitUtils.writeLEByte8(this.out, value.getTime());
        }
    }

    @Override
    public void writeIntegerValue(final Long i, final int length) throws IOException {
        if (i == null) {
            BitUtils.writeEmpties(this.out, length);
        } else {
            final ByteBuffer bb = ByteBuffer.allocate(length);
            bb.putLong(i);
            this.out.write(bb.array());
        }
    }

    @Override
    public int getRecordQty() {
        return this.recordCount;
    }
}
