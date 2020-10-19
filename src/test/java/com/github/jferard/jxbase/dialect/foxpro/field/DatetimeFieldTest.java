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

package com.github.jferard.jxbase.dialect.foxpro.field;

import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

public class DatetimeFieldTest {
    private DatetimeField f;
    private DatetimeAccess access;

    @Before
    public void setUp() throws Exception {
        this.access = new FoxProDatetimeAccess();
        this.f = new DatetimeField("dt");
    }

    @Test
    public void getName() {
        Assert.assertEquals("dt", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(8, this.f.getValueByteLength(this.access));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = {0, 0x65, (byte) 0x25, 0, 0, 0, 0, 0};
        final Calendar cal = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        cal.set(1997, Calendar.AUGUST, 27, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Assert.assertEquals(cal.getTime(), this.f.getValue(this.access, bytes, 0, 8));
    }

    @Test
    public void writeValue() throws IOException {
        final Calendar cal = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        cal.set(1997, Calendar.AUGUST, 27, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.f.writeValue(this.access, bos, cal.getTime());
        final byte[] bytes = {0, 0x65, (byte) 0x25, 0, 0, 0, 0, 0};
        Assert.assertArrayEquals(bytes, bos.toByteArray());
    }

    @Test
    public void writeNullValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.f.writeValue(this.access, bos, null);
        final byte[] bytes = {0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20};
        Assert.assertArrayEquals(bytes, bos.toByteArray());
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("dt,T,8,0", this.f.toStringRepresentation(this.access));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("DatetimeField[name=dt]", this.f.toString());
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(3216, this.f.hashCode());
        Assert.assertEquals(this.f, this.f);
        Assert.assertNotEquals(this.f, new Object());
        final DatetimeField f2 = new DatetimeField("dt");
        Assert.assertEquals(this.f, f2);
    }
}