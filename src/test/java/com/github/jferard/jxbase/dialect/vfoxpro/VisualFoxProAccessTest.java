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

package com.github.jferard.jxbase.dialect.vfoxpro;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class VisualFoxProAccessTest {
    private VisualFoxProAccess access;

    @Before
    public void setUp() {
        this.access = VisualFoxProDialectFactory
                .createAccess(JxBaseUtils.ASCII_CHARSET, JxBaseUtils.UTC_TIME_ZONE);
    }

    @Test
    public void testGetDatetimeValueLength() {
        Assert.assertEquals(8, this.access.getDatetimeValueLength());
    }

    @Test
    public void testExtractDatetimeValue() {
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(0L);
        cal.set(2021, Calendar.JUNE, 25, 21, 56, 15);
        Assert.assertEquals(cal.getTime(), this.access
                .extractDatetimeValue(new byte[]{-1, -122, 37, 0, 24, 16, -75, 4}, 0, 8));
    }

    @Test
    public void testWriteDatetimeValue() throws IOException {
        final Calendar cal = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        cal.setTimeInMillis(0L);
        cal.set(2021, Calendar.JUNE, 25, 21, 56, 15);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.access.writeDatetimeValue(bos, cal.getTime());
        Assert.assertArrayEquals(new byte[]{-1, -122, 37, 0, 24, 16, -75, 4}, bos.toByteArray());
    }

    @Test
    public void testGetDatetimeRepresentation() {
        Assert.assertEquals(new FieldRepresentation("foo", 'T', 8, 0),
                this.access.getDatetimeRepresentation("foo"));
    }

    @Test
    public void testGetIntegerValueLength() {
        Assert.assertEquals(4, this.access.getIntegerValueLength());
    }

    @Test
    public void testExtractIntegerValue() {
        Assert.assertEquals(67305985,
                (long) this.access.extractIntegerValue(new byte[]{1, 2, 3, 4}, 0, 4));
    }

    @Test
    public void testWriteIntegerValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.access.writeIntegerValue(bos, 67305985L);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, bos.toByteArray());
    }

    @Test
    public void testGetIntegerFieldRepresentation() {
        Assert.assertEquals(new FieldRepresentation("foo", 'I', 4, 0),
                this.access.getIntegerFieldRepresentation("foo"));
    }

    @Test
    public void testGetNullFlagsFieldLength() {
        Assert.assertEquals(10, this.access.getNullFlagsFieldLength(10));
    }

    @Test
    public void testExtractNullFlagsValue() {
        Assert.assertArrayEquals(new byte[]{1, 5},
                this.access.extractNullFlagsValue(new byte[]{1, 5}, 0, 2));
    }

    @Test
    public void testWriteNullFlagsValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.access.writeNullFlagsValue(bos, new byte[]{1, 5}, 2);
        Assert.assertArrayEquals(new byte[]{1, 5}, bos.toByteArray());
    }

    @Test
    public void testGetNullFlagsFieldRepresentation() {
        Assert.assertEquals(new FieldRepresentation("foo", '0', 10, 0),
                this.access.getNullFlagsFieldRepresentation("foo", 10));
    }

    @Test
    public void testGetDoubleValueLength() {
        Assert.assertEquals(8, this.access.getDoubleValueLength());
    }

    @Test
    public void testExtractDoubleValue() {
        Assert.assertEquals(106.64d, this.access
                .extractDoubleValue(new byte[]{64, 90, -88, -11, -62, -113, 92, 41}, 0, 8), 0.01d);
    }

    @Test
    public void testWriteDoubleValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.access.writeDoubleValue(bos, 106.64d);
        Assert.assertArrayEquals(new byte[]{64, 90, -88, -11, -62, -113, 92, 41},
                bos.toByteArray());
    }

    @Test
    public void testGetDoubleFieldRepresentation() {
        Assert.assertEquals(new FieldRepresentation("foo", 'B', 8, 4),
                this.access.getDoubleFieldRepresentation("foo", 4));
    }

    @Test
    public void testGetCurrencyValueLength() {
        Assert.assertEquals(8, this.access.getCurrencyValueLength());
    }

    @Test
    public void testExtractCurrencyValue() {
        Assert.assertEquals(319, this.access
                .extractCurrencyValue(new byte[]{0, 0, 0, 0, 63, 1, 0, 0}, 0, 8));
    }

    @Test
    public void testWriteCurrencyValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.access.writeCurrencyValue(bos, 319);
        Assert.assertArrayEquals(new byte[]{0, 0, 0, 0, 63, 1, 0, 0},
                bos.toByteArray());
    }

    @Test
    public void testGetCurrencyRepresentation() {
        Assert.assertEquals(new FieldRepresentation("foo", 'Y', 8, 0),
                this.access.getCurrencyRepresentation("foo"));
    }
}