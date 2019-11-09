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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
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

    // cf. www.nr.com/julian.html
    private static int dateToJulian(final Date date) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        final int Y = calendar.get(Calendar.YEAR);
        final int M = calendar.get(Calendar.MONTH) + 1;
        final int D = calendar.get(Calendar.DAY_OF_MONTH);

        // https://en.wikipedia
        // .org/wiki/Julian_day#Converting_Gregorian_calendar_date_to_Julian_Day_Number
        return (1461 * (Y + 4800 + (M - 14) / 12)) / 4 +
                (367 * (M - 2 - 12 * ((M - 14) / 12))) / 12 -
                (3 * ((Y + 4900 + (M - 14) / 12) / 100)) / 4 + D - 32075;
    }

    private static int millis(final Date date) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return ((calendar.get(Calendar.HOUR) * 60 + calendar.get(Calendar.MINUTE)) * 60 +
                calendar.get(Calendar.SECOND)) * 1000;
    }


    final XBaseDialect dialect;
    final OutputStream out;
    final Charset charset;
    private final XBaseMemoWriter memoWriter;
    private final Collection<XBaseField> fields;
    private int recordCount;

    public GenericRecordWriter(final XBaseDialect dialect, final OutputStream out,
                               final Charset charset, final Collection<XBaseField> fields,
                               final XBaseMemoWriter memoWriter) {
        this.dialect = dialect;
        this.fields = fields;
        this.out = out;
        this.charset = charset;
        this.memoWriter = memoWriter;
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

    /**
     * FoxPro
     * https://www.clicketyclick.dk/databases/xbase/format/data_types.html
     * The first 4 bytes are a 32-bit little-endian integer representation of the Julian date,
     * where Oct. 15, 1582 = 2299161 per www.nr.com/julian.html The last 4 bytes are a 32-bit
     * little-endian integer time of day represented as milliseconds since midnight.
     *
     * @param value
     * @throws IOException
     */
    // TODO: FIXME
    @Override
    public void writeDatetimeValue(final Date value) throws IOException {
        final int fieldLength = 8;
        if (value == null) {
            BitUtils.writeEmpties(this.out, fieldLength);
        } else {
            final long time = value.getTime();
            BitUtils.writeLEByte4(this.out, GenericRecordWriter.dateToJulian(value));
            BitUtils.writeLEByte4(this.out, GenericRecordWriter.millis(value));
        }
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

    @Override
    public void writeNullFlagsValue(final byte[] value, final int length) throws IOException {
        this.out.write(value);
    }
}
