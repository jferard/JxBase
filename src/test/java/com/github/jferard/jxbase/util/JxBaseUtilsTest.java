/*
 * JxBase - Copyright (c) 2019-2020 Julien Férard
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

import org.junit.Assert;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class JxBaseUtilsTest {
    @Test
    public void testJoin() {
        Assert.assertEquals("", JxBaseUtils.join(",", Collections.<String>emptyList()));
        Assert.assertEquals("a", JxBaseUtils.join(",", Collections.singleton("a")));
        Assert.assertEquals("a,b", JxBaseUtils.join(",", Arrays.asList("a", "b")));
    }

    @Test
    public void testGetLength() {
        Assert.assertEquals(10, JxBaseUtils.getLength((byte) 10));
        Assert.assertEquals(246, JxBaseUtils.getLength((byte) -10));
    }

    @Test
    public void testGetName() {
        Assert.assertThrows(ArrayIndexOutOfBoundsException.class, new ThrowingRunnable() {
                    @Override
                    public void run() throws Throwable {
                        JxBaseUtils.getName(new byte[]{'f', 'o', 'o'});
                    }
                });
        Assert.assertEquals("foo", JxBaseUtils.getName(new byte[] {'f', 'o', 'o', 0x0, 'b', 'a', 'r'}));
    }

    @Test
    public void testReadFieldBytesLess() throws IOException {
        final byte[] buf = new byte[2];
        JxBaseUtils.readFieldBytes(new ByteArrayInputStream(new byte[]{'f', 'o', 'o'}), buf);
        Assert.assertArrayEquals(new byte[] { 'f', 'o'}, buf);
    }

    @Test
    public void testReadFieldBytesMore() throws IOException {
        Assert.assertThrows(IOException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                final byte[] buf = new byte[4];
                JxBaseUtils.readFieldBytes(new ByteArrayInputStream(new byte[]{'f', 'o', 'o'}), buf);
            }
        });
    }
    /*

    @Test
    public void testCreateFieldsFromString() {
        final List<XBaseField> fieldsFromString =
                JdbfUtils.createFieldsFromString("x,I,10,2|y,M,4,4");
        Assert.assertEquals(2, fieldsFromString.size());
        Assert.assertEquals("DbfField[name=x, type=Integer, length=10, numberOfDecimalPlaces=2]",
                fieldsFromString.get(0).toString());
        Assert.assertEquals("DbfField[name=y, type=Memo, length=4, numberOfDecimalPlaces=4]",
                fieldsFromString.get(1).toString());
    }

    @Test
    public void testCreateFieldsFromStringWithEmptyOne() {
        final List<XBaseField> fieldsFromString =
                JdbfUtils.createFieldsFromString("x,I,10,2|   |y,M,4,4");
        Assert.assertEquals(2, fieldsFromString.size());
        Assert.assertEquals("DbfField[name=x, type=Integer, length=10, numberOfDecimalPlaces=2]",
                fieldsFromString.get(0).toString());
        Assert.assertEquals("DbfField[name=y, type=Memo, length=4, numberOfDecimalPlaces=4]",
                fieldsFromString.get(1).toString());
    }

    @Test
    public void testCreateDbfFieldFromString() {
        Assert.assertEquals("DbfField[name=x, type=Integer, length=10, numberOfDecimalPlaces=2]",
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
    public void testCompareMapsDifferentSize() {
        final Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("a", 1);
        m1.put("b", 2);
        final Map<String, Object> m2 = new HashMap<String, Object>();
        m2.put("a", 1);
        Assert.assertFalse(JdbfUtils.compareMaps(m1, m2));
    }

    @Test
    public void testCompareMapsDifferentKeys() {
        final Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("a", 1);
        m1.put("b", 2);
        final Map<String, Object> m2 = new HashMap<String, Object>();
        m2.put("a", 1);
        m2.put("c", 3);
        Assert.assertFalse(JdbfUtils.compareMaps(m1, m2));
    }

    @Test
    public void testCompareMapsDifferentValues() {
        final Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("a", 1);
        m1.put("b", 2);
        final Map<String, Object> m2 = new HashMap<String, Object>();
        m2.put("a", 1);
        m2.put("b", 3);
        Assert.assertFalse(JdbfUtils.compareMaps(m1, m2));
    }

    @Test
    public void testCompareMapsEqual() {
        final Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("a", 1);
        m1.put("b", 2);
        final Map<String, Object> m2 = new HashMap<String, Object>();
        m2.put("a", 1);
        m2.put("b", 2);
        Assert.assertTrue(JdbfUtils.compareMaps(m1, m2));
    }

    @Test
    public void testCompareObjects() {
        final Object o = new Object();
        Assert.assertTrue(JdbfUtils.compareObjects(null, null));
        Assert.assertFalse(JdbfUtils.compareObjects(o, null));
        Assert.assertFalse(JdbfUtils.compareObjects(null, o));
        Assert.assertTrue(JdbfUtils.compareObjects(o, o));
    }

    @Test
    public void testWriteJulianDate() {
        Assert.assertArrayEquals(new byte[]{0, 0, -101, -48, 0, 28, -38, -72},
                JdbfUtils.writeJulianDate(new Date(1234567891011L)));
    }

    @Test
    public void testWriteJulianDateHuge() {
        Assert.assertArrayEquals(new byte[]{-84, 74, -104, -34, 0, 102, -115, -24},
                JdbfUtils.writeJulianDate(new Date(1234567891011121314L)));
    }

     */
}