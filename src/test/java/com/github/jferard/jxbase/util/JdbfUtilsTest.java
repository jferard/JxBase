/*
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

package com.github.jferard.jxbase.util;

import com.github.jferard.jxbase.core.DbfField;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbfUtilsTest {
    @Test
    public void testCreateFieldsFromString() {
        final List<DbfField> fieldsFromString =
                JdbfUtils.createFieldsFromString("x,I,10,2|y,M,4,4");
        Assert.assertEquals(2, fieldsFromString.size());
        Assert.assertEquals(
                "DbfField [\n" + "  name=x, \n" + "  type=Integer, \n" + "  length=10, \n" +
                        "  numberOfDecimalPlaces=2, \n" + "  offset=0\n" + "]",
                fieldsFromString.get(0).toString());
        Assert.assertEquals("DbfField [\n" + "  name=y, \n" + "  type=Memo, \n" + "  length=4, \n" +
                        "  numberOfDecimalPlaces=4, \n" + "  offset=0\n" + "]",
                fieldsFromString.get(1).toString());
    }

    @Test
    public void testCreateDbfFieldFromString() {
        Assert.assertEquals(
                "DbfField [\n" + "  name=x, \n" + "  type=Integer, \n" + "  length=10, \n" +
                        "  numberOfDecimalPlaces=2, \n" + "  offset=0\n" + "]",
                JdbfUtils.createDbfFieldFromString("x,I,10,2").toString());
    }

    @Test
    public void testWriteDateForHeader() {
        Assert.assertArrayEquals(new byte[]{9, 2, 6},
                JdbfUtils.writeDateForHeader(new Date(1234567891011L)));
    }

    @Test
    public void testWriteDate() {
        Assert.assertEquals("20090214", new String(JdbfUtils.writeDate(new Date(1234567891011L)),
                Charset.forName("ASCII")));
    }

    @Test
    public void testParseDate() throws ParseException {
        Assert.assertEquals(new Date(1234566000000L), JdbfUtils.parseDate("20090214"));
    }

    @Test
    public void testCompareMaps() {
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("a", 1);
        m1.put("b", 2);
        Map<String, Object> m2 = new HashMap<String, Object>();
        m1.put("a", 1);
        m1.put("c", 3);
        Assert.assertFalse(JdbfUtils.compareMaps(m1, m2));
    }

    @Test
    public void testWriteJulianDate() {
        Assert.assertArrayEquals(new byte[]{0, 0, -101, -48, 0, 28, -38, -72},
                JdbfUtils.writeJulianDate(new Date(1234567891011L)));
    }
}