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

import com.github.jferard.jxbase.util.BitUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class FoxProUtils {

    public static final int MONTH_PER_YEAR = 12;
    public static final int GREGORIAN_CHANGE = getDays(1582, 10, 14);
    public static final int GREGORIAN_CHANGE_IN_JULIAN_DAYS = 2299160;
    public static final double DAYS_PER_JULIAN_YEAR = 365.25;
    public static final double DAYS_PER_JULIAN_MONTH_MARCH_TO_DECEMBER = 30.6001;
    public static final int JULIAN_OCT_30_2_BC = 1720995;
    public static final int JULIAN_FEB_28_400_AD = 1867216;
    public static final double JULIAN_SHIFT_IN_DAYS_PER_YEAR = 0.01;
    public static final double ONE_OUT_OF_FOUR = 0.25;
    public static final double DAYS_PER_GREGORIAN_YEAR = 365.2425;
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");

    public static Date createHeaderUpdateDate(final byte yearByte, final byte monthByte,
                                              final byte dayByte) {
        final int year = yearByte + 2000;
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthByte - 1, dayByte, 0, 0, 0);
        return calendar.getTime();
    }

    // cf. https://bowie.gsfc.nasa.gov/time/julian.html
    public static int dateToJulianDays(final Date date) {
        final GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        int years = calendar.get(Calendar.YEAR);
        assert years != 0;
        final int era = calendar.get(Calendar.ERA);
        if (era == GregorianCalendar.BC) {
            years = (-years) + 1;
        }
        final int months = calendar.get(Calendar.MONTH) + 1; // 1..12
        final int days = calendar.get(Calendar.DAY_OF_MONTH);

        return dateToJulianDays(years, months, days);
    }

    private static int dateToJulianDays(int years, int months, final int days) {
        if (months <= 2) { // Jan & Feb: add 1.9512 days
            years--; // remove 365.25 days
            months += MONTH_PER_YEAR; // add 367.2012 days
        }

        // if years months days were in julian calendar
        int julianDays = toInt(Math.floor(DAYS_PER_JULIAN_YEAR * years) +
                Math.floor(DAYS_PER_JULIAN_MONTH_MARCH_TO_DECEMBER * (months + 1)) + days +
                JULIAN_OCT_30_2_BC);

        // remove the shift from julian calendar
        if (afterGregorianChange(years, months, days)) {
            julianDays += 2; // add 10 days, but the shift was 12.7
            final int shift = toInt(JULIAN_SHIFT_IN_DAYS_PER_YEAR * years);
            julianDays -= shift - toInt(ONE_OUT_OF_FOUR * shift); // 100 / 400
        }
        return julianDays;
    }

    private static boolean afterGregorianChange(final int years, final int months, final int days) {
        return getDays(years, months, days) > GREGORIAN_CHANGE;
    }

    private static int getDays(final int years, final int months, final int days) {
        return days + 31 * (months + 12 * years);
    }

    public static int millisFromDate(final Date date) {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        return ((calendar.get(Calendar.HOUR) * 60 + calendar.get(Calendar.MINUTE)) * 60 +
                calendar.get(Calendar.SECOND)) * 1000;
    }

    // cf. https://bowie.gsfc.nasa.gov/time/julian.html
    public static Date julianDaysToDate(final byte b0, final byte b1, final byte b2,
                                        final byte b3) {
        final int julianDays = BitUtils.makeInt(b0, b1, b2, b3);
        return julianDaysToDate(julianDays);
    }

    public static Date julianDaysToDate(int julianDays) {
        if (julianDays > GREGORIAN_CHANGE_IN_JULIAN_DAYS + 1) {
            final int shift = (int) (((julianDays - JULIAN_FEB_28_400_AD) - ONE_OUT_OF_FOUR) /
                    (DAYS_PER_GREGORIAN_YEAR * 100));
            julianDays += 1 + shift - (int) (ONE_OUT_OF_FOUR * shift);
        }
        final int jb = julianDays + 1524;
        final int yplus2 =
                (int) (6680 + (jb - 6680 * DAYS_PER_JULIAN_YEAR - 122.1) / DAYS_PER_JULIAN_YEAR);
        final int jd = (int) (DAYS_PER_JULIAN_YEAR * yplus2);
        final int temp = jb - jd;
        final int mplus2 = (int) (temp / DAYS_PER_JULIAN_MONTH_MARCH_TO_DECEMBER);
        final int D = (temp - (int) (DAYS_PER_JULIAN_MONTH_MARCH_TO_DECEMBER * mplus2));
        int M = mplus2 - 2;
        if (M >= MONTH_PER_YEAR) {
            M -= MONTH_PER_YEAR;
        }
        int Y = yplus2 - 4715; // 4713 + 2
        if (M > Calendar.FEBRUARY) {
            --Y;
        }
        final int era;
        if (Y <= 0) {
            Y = 1 - Y;
            era = GregorianCalendar.BC;
        } else {
            era = GregorianCalendar.AD;
        }

        return getDate(Y, M, D, era);
    }

    public static Date getDate(final int year, final int month, final int day, final int era) {
        final Calendar calendar = Calendar.getInstance(UTC_TIME_ZONE);
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static long toMillis(final byte b0, final byte b1, final byte b2, final byte b3) {
        final long millis = BitUtils.makeInt(b0, b1, b2, b3);
        return millis;
    }

    private static int toInt(final double val) {
        if (val >= 0) {
            return (int) Math.floor(val);
        } else {
            return (int) Math.ceil(val);
        }
    }
}
