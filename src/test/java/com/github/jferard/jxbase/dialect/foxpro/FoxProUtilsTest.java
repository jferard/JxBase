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

package com.github.jferard.jxbase.dialect.foxpro;

import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class FoxProUtilsTest {
    @Test
    public void testDateToJulianAD() {
        final Calendar cal = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE, Locale.US);
        cal.set(Calendar.ERA, GregorianCalendar.AD);
        cal.set(1, Calendar.JANUARY, 1);
        Assert.assertEquals(1721424, FoxProUtils.dateToJulianDays(cal.getTime()));
        cal.set(1000, Calendar.JANUARY, 1);
        Assert.assertEquals(2086308, FoxProUtils.dateToJulianDays(cal.getTime()));
        cal.set(1600, Calendar.JANUARY, 1);
        Assert.assertEquals(2305448, FoxProUtils.dateToJulianDays(cal.getTime()));
        cal.set(2019, Calendar.DECEMBER, 15);
        Assert.assertEquals(2458833, FoxProUtils.dateToJulianDays(cal.getTime()));
    }

    @Test
    public void testDateToJulianBC() {
        final Calendar cal = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE, Locale.US);
        cal.set(Calendar.ERA, GregorianCalendar.BC);
        cal.set(4713, Calendar.JANUARY, 1);
        Assert.assertEquals(0, FoxProUtils.dateToJulianDays(cal.getTime()));
        cal.set(5, Calendar.JANUARY, 1);
        Assert.assertEquals(1719597, FoxProUtils.dateToJulianDays(cal.getTime()));
        cal.set(4, Calendar.JANUARY, 1);
        Assert.assertEquals(1719963, FoxProUtils.dateToJulianDays(cal.getTime()));
        cal.set(3, Calendar.JANUARY, 1);
        Assert.assertEquals(1720328, FoxProUtils.dateToJulianDays(cal.getTime()));
        cal.set(2, Calendar.JANUARY, 1);
        Assert.assertEquals(1720693, FoxProUtils.dateToJulianDays(cal.getTime()));
        cal.set(1, Calendar.JANUARY, 1);
        Assert.assertEquals(1721058, FoxProUtils.dateToJulianDays(cal.getTime()));
        cal.set(2, Calendar.OCTOBER, 30);
        Assert.assertEquals(1720995, FoxProUtils.dateToJulianDays(cal.getTime()));
        cal.set(-1, Calendar.OCTOBER, 30);
        Assert.assertEquals(1722091, FoxProUtils.dateToJulianDays(cal.getTime()));
        cal.set(-1000, Calendar.OCTOBER, 30);
        Assert.assertEquals(1356111, FoxProUtils.dateToJulianDays(cal.getTime()));
    }

    @Test
    public void testJulianToDateBC() {
        Assert.assertEquals(FoxProUtils.getDate(4714, Calendar.DECEMBER, 31, GregorianCalendar.BC),
                FoxProUtils.julianDaysToDate(-1));
        Assert.assertEquals(FoxProUtils.getDate(4713, Calendar.JANUARY, 1, GregorianCalendar.BC),
                FoxProUtils.julianDaysToDate(0));
        Assert.assertEquals(FoxProUtils.getDate(5, Calendar.JANUARY, 1, GregorianCalendar.BC),
                FoxProUtils.julianDaysToDate(1719597));
        Assert.assertEquals(FoxProUtils.getDate(4, Calendar.JANUARY, 1, GregorianCalendar.BC),
                FoxProUtils.julianDaysToDate(1719963));
        Assert.assertEquals(FoxProUtils.getDate(3, Calendar.JANUARY, 1, GregorianCalendar.BC),
                FoxProUtils.julianDaysToDate(1720328));
        Assert.assertEquals(FoxProUtils.getDate(2, Calendar.OCTOBER, 30, GregorianCalendar.BC),
                FoxProUtils.julianDaysToDate(1720995));
        Assert.assertEquals(FoxProUtils.getDate(2, Calendar.JANUARY, 1, GregorianCalendar.BC),
                FoxProUtils.julianDaysToDate(1720693));
        Assert.assertEquals(FoxProUtils.getDate(1, Calendar.JANUARY, 1, GregorianCalendar.BC),
                FoxProUtils.julianDaysToDate(1721058));
    }

    @Test
    public void testJulianToDateAD() {
        Assert.assertEquals(FoxProUtils.getDate(1, Calendar.JANUARY, 1, GregorianCalendar.AD),
                FoxProUtils.julianDaysToDate(1721424));
        Assert.assertEquals(FoxProUtils.getDate(2019, Calendar.DECEMBER, 15, GregorianCalendar.AD),
                FoxProUtils.julianDaysToDate(2458833));
        Assert.assertEquals(FoxProUtils.getDate(2021, Calendar.JUNE, 25, GregorianCalendar.AD),
                FoxProUtils.julianDaysToDate(2459391));
        Assert.assertEquals(FoxProUtils.getDate(2021, Calendar.JULY, 8, GregorianCalendar.AD),
                FoxProUtils.julianDaysToDate(2459404));
    }

    @Test
    public void testCreateHeaderUpdateDate() {
        final Calendar cal = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        cal.setTimeInMillis(0);
        cal.set(2021, Calendar.JUNE, 25);
        Assert.assertEquals(cal.getTime(), FoxProUtils.createHeaderUpdateDate((byte) 21, (byte) 6, (byte) 25));
    }

    @Test
    public void testDateToJulianDays() {
        final Calendar cal = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        cal.setTimeInMillis(0);
        cal.set(2021, Calendar.JUNE, 25, 22, 7, 49);
        Assert.assertEquals(2459391, FoxProUtils.dateToJulianDays(cal.getTime()));
    }

    @Test
    public void testMillisFromDate() {
        final Calendar cal = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        cal.setTimeInMillis(0);
        cal.set(2021, Calendar.JUNE, 25, 22, 7, 49);
        final int seconds = ((22 * 60) + 7) * 60 + 49;
        Assert.assertEquals(seconds*1000, FoxProUtils.millisFromDate(cal.getTime()));
    }
}