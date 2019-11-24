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

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.util.BitUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FoxProDatetimeAccess implements DatetimeAccess {
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

    @Override
    public int getDatetimeValueLength() {
        return 8;
    }

    @Override
    public Date getDatetimeValue(final byte[] recordBuffer, final int offset, final int length) {
        // TODO
        // https://en.wikipedia
        // .org/wiki/Julian_day#Julian_or_Gregorian_calendar_from_Julian_day_number
        return null;
    }

    @Override
    public void writeDatetimeValue(final OutputStream out, final Date value) throws IOException {
        final int fieldLength = 8;
        if (value == null) {
            BitUtils.writeEmpties(out, fieldLength);
        } else {
            final long time = value.getTime();
            BitUtils.writeLEByte4(out, FoxProDatetimeAccess.dateToJulian(value));
            BitUtils.writeLEByte4(out, FoxProDatetimeAccess.millis(value));
        }
    }

    @Override
    public FieldRepresentation getDatetimeRepresentation(final String name) {
        return new FieldRepresentation(name, 'T', 8, 0);
    }
}
