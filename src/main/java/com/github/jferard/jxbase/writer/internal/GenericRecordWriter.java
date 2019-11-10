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
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.XBaseMemoWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class GenericRecordWriter implements XBaseRecordWriter {
    public static final ThreadLocal<SimpleDateFormat> DATE_FORMAT =
            new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("yyyyMMdd");
                }
            };
    private static final int MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
    protected final XBaseMemoWriter memoWriter;
    protected final Collection<XBaseField> fields;
    final XBaseDialect dialect;
    final OutputStream out;
    final Charset charset;
    protected int recordCount;

    public GenericRecordWriter(final XBaseDialect dialect, final OutputStream out,
                               final Charset charset, final XBaseMemoWriter memoWriter,
                               final Collection<XBaseField> fields) {
        this.dialect = dialect;
        this.out = out;
        this.charset = charset;
        this.memoWriter = memoWriter;
        this.fields = fields;
        this.recordCount = 0;
    }

    @Override
    public void write(final Map<String, Object> objectByName) throws IOException {
        this.out.write(JxBaseUtils.EMPTY);
        for (final XBaseField field : this.fields) {
            final Object value = objectByName.get(field.getName());
            field.writeValue(this, value);
        }
        this.recordCount++;
    }

    @Override
    public void writeLogicalValue(final Boolean value) throws IOException {
        final byte[] bytes;
        if (value == null) {
            bytes = "?".getBytes(JxBaseUtils.ASCII_CHARSET);
        } else {
            final String s = value ? "T" : "F";
            bytes = s.getBytes();
        }
        this.out.write(bytes);
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
            this.out.write(s.getBytes(JxBaseUtils.ASCII_CHARSET));
        }
    }

    @Override
    public void writeNumericValue(final BigDecimal value, final int length,
                                  final int numberOfDecimalPlaces) throws IOException {
        if (value == null) {
            BitUtils.writeEmpties(this.out, length);
        } else {
            final DecimalFormat df = this.createDecimalFormat(numberOfDecimalPlaces);
            final String s = df.format(value);
            final byte[] numberBytes = s.getBytes(JxBaseUtils.ASCII_CHARSET);
            final int missingCount = length - numberBytes.length;
            if (missingCount >= 0) {
                this.writeNumeric(numberBytes, missingCount, length);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public <T extends XBaseMemoRecord<?>> void writeMemoValue(final T value) throws IOException {
        final int length = this.dialect.getMemoFieldLength();
        if (value == null) {
            BitUtils.writeEmpties(this.out, length);
        } else {
            final long offsetInBlocks = value.getOffsetInBlocks();
            final String s = String.format("%10d", offsetInBlocks);
            this.out.write(s.getBytes(JxBaseUtils.ASCII_CHARSET));
            this.memoWriter.write(offsetInBlocks, value);
        }
    }

    private void writeNumeric(final byte[] numberBytes, final int missingCount, final int length)
            throws IOException {
        final byte[] bytes = new byte[length];
        for (int i = 0; i < missingCount; i++) {
            bytes[i] = (byte) JxBaseUtils.EMPTY;
        }
        for (int i = 0; i < numberBytes.length; i++) {
            bytes[missingCount + i] = numberBytes[i];
        }
        this.out.write(bytes);
    }

    private DecimalFormat createDecimalFormat(final int numberOfDecimalPlaces) {
        final DecimalFormat df = new DecimalFormat();
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        df.setMinimumIntegerDigits(1);
        df.setGroupingUsed(false);
        df.setMinimumFractionDigits(numberOfDecimalPlaces);
        df.setMaximumFractionDigits(numberOfDecimalPlaces);
        return df;
    }

    @Override
    public void writeIntegerValue(final Long i) throws IOException {
        if (i == null) {
            BitUtils.writeEmpties(this.out, this.dialect.getIntegerFieldLength());
        } else {
            assert 4 == this.dialect.getIntegerFieldLength();
            BitUtils.writeLEByte4(this.out, i.intValue());
        }
    }

    @Override
    public int getRecordQty() {
        return this.recordCount;
    }
}
