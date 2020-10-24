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

package com.github.jferard.jxbase.dialect.db4.field;

import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class FloatFieldTest {
    private FloatField f;
    private FloatAccess access;

    @Before
    public void setUp() {
        final RawRecordReadHelper readHelper = new RawRecordReadHelper(JxBaseUtils.ASCII_CHARSET);
        final RawRecordWriteHelper writeHelper =
                new RawRecordWriteHelper(JxBaseUtils.ASCII_CHARSET);
        this.access = new DB4FloatAccess(readHelper, writeHelper);
        this.f = new FloatField("float");
    }

    @Test
    public void getName() {
        Assert.assertEquals("float", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(20, this.f.getValueLength(this.access));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = "3.141592653589793".getBytes(JxBaseUtils.ASCII_CHARSET);
        Assert.assertEquals(new BigDecimal("3.14"), this.f.getValue(this.access, bytes, 0, 4));
    }

    @Test
    public void getNullEmptyValue() throws IOException {
        final byte[] bytes = "    ".getBytes(JxBaseUtils.ASCII_CHARSET);
        Assert.assertNull(this.f.getValue(this.access, bytes, 0, 4));
    }

    @Test
    public void getNullOverflowValue() throws IOException {
        final byte[] bytes = "*   ".getBytes(JxBaseUtils.ASCII_CHARSET);
        Assert.assertNull(this.f.getValue(this.access, bytes, 0, 4));
    }

    @Test
    public void writeValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.f.writeValue(this.access, bos, 3.141592653589793f);
        final byte[] bytes = "           3.1415927".getBytes(JxBaseUtils.ASCII_CHARSET);
        Assert.assertArrayEquals(bytes, bos.toByteArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeLongValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.f.writeValue(this.access, bos, new BigDecimal("1415926535897933.141592653589793"));
        final byte[] bytes = "           3.1415927".getBytes(JxBaseUtils.ASCII_CHARSET);
        Assert.assertArrayEquals(bytes, bos.toByteArray());
    }


    @Test
    public void writeNullValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.f.writeValue(this.access, bos, null);
        final byte[] bytes = "                    ".getBytes(JxBaseUtils.ASCII_CHARSET);
        Assert.assertArrayEquals(bytes, bos.toByteArray());
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("float,F,20,0", this.f.toStringRepresentation(this.access));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("FloatField[name=float]", this.f.toString());
    }


    @Test
    public void testEquals() {
        Assert.assertEquals(97526364, this.f.hashCode());
        Assert.assertEquals(this.f, this.f);
        Assert.assertNotEquals(this.f, new Object());
        final FloatField f2 = new FloatField("int");
        Assert.assertNotEquals(this.f, f2);
        final FloatField f3 = new FloatField("float");
        Assert.assertEquals(this.f, f3);
    }

}