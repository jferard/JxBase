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

package com.github.jferard.jxbase.dialect.foxpro;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.memo.WithMemoRecordWriter;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.BitUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class FoxProRecordWriter extends WithMemoRecordWriter {
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

    public FoxProRecordWriter(final XBaseDialect dialect, final OutputStream out,
                              final Charset charset, final XBaseMemoWriter memoWriter,
                              final Collection<XBaseField> fields) {
        super(dialect, out, charset, fields, memoWriter);
    }


    public FoxProRecordWriter(final XBaseDialect dialect, final OutputStream out,
                              final Charset charset, final Collection<XBaseField> fields,
                              final XBaseMemoWriter memoWriter) {
        super(dialect, out, charset, fields, memoWriter);
    }

    public void writeSmallMemoValue(final XBaseMemoRecord value) throws IOException {
        final int length = ((FoxProDialect) this.dialect).getSmallMemoFieldLength();
        if (value == null) {
            BitUtils.writeEmpties(this.out, length);
        } else {
            final long offsetInBlocks = this.memoWriter.write(value);
            BitUtils.writeLEByte4(this.out, (int) offsetInBlocks);
        }
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
    public void writeDatetimeValue(final Date value) throws IOException {
        final int fieldLength = 8;
        if (value == null) {
            BitUtils.writeEmpties(this.out, fieldLength);
        } else {
            final long time = value.getTime();
            BitUtils.writeLEByte4(this.out, FoxProRecordWriter.dateToJulian(value));
            BitUtils.writeLEByte4(this.out, FoxProRecordWriter.millis(value));
        }
    }

    public void writeNullFlagsValue(final byte[] value, final int length) throws IOException {
        this.out.write(value);
    }
}
