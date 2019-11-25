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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class FoxProUtils {
    public static Date createHeaderUpdateDate(final byte yearByte, final byte monthByte,
                                              final byte dayByte) {
        final int year = yearByte + 2000;
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthByte - 1, dayByte);
        return calendar.getTime();
    }

    // cf. www.nr.com/julian.html
    public static int dateToJulian(final Date date) {
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

    public static int millis(final Date date) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return ((calendar.get(Calendar.HOUR) * 60 + calendar.get(Calendar.MINUTE)) * 60 +
                calendar.get(Calendar.SECOND)) * 1000;
    }

    // TODO
    // https://en.wikipedia
    // .org/wiki/Julian_day#Julian_or_Gregorian_calendar_from_Julian_day_number
    public static long julianToDate(final byte b, final byte b1, final byte b2, final byte b3) {
        return 0;
    }

    public static long toMillis(final byte b, final byte b1, final byte b2, final byte b3) {
        return 0;
    }
}
