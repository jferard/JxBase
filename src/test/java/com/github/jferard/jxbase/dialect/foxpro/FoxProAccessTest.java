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

package com.github.jferard.jxbase.dialect.foxpro;

import com.github.jferard.jxbase.dialect.vfoxpro.CDDtFILMN0FieldsAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialectBuilder;
import com.github.jferard.jxbase.dialect.vfoxpro.field.DatetimeField;
import com.github.jferard.jxbase.dialect.vfoxpro.field.IntegerField;
import com.github.jferard.jxbase.dialect.vfoxpro.field.NullFlagsField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class FoxProAccessTest {
    private IntegerField inf;
    private CDDtFILMN0FieldsAccess access;
    private NullFlagsField nf;
    private DatetimeField df;

    @Before
    public void setUp() throws Exception {
        this.access = VisualFoxProDialectBuilder.create(null, JxBaseUtils.ASCII_CHARSET,
                JxBaseUtils.UTC_TIME_ZONE).build().getAccess();
        this.inf = new IntegerField("int");
        this.nf = new NullFlagsField("nf", 8);
        this.df = new DatetimeField("dt");
    }

    @Test
    public void getDTName() {
        Assert.assertEquals("dt", this.df.getName());
    }

    @Test
    public void getDTByteLength() {
        Assert.assertEquals(8, this.df.getValueLength(this.access));
    }

    @Test
    public void getDTValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8};
//        Assert.assertEquals(new Date(1098477320L), this.df.getValue(this.access, bytes, 0, 8));
    }

    @Test
    public void writeDTValue() throws IOException {
        final Calendar calendar = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        calendar.setTimeInMillis(1234567891011L);

        final byte[] bytes = {92, 117, 37, 0, 56, 26, 121, 2};
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.df.writeValue(this.access, out, calendar.getTime());
//        Assert.assertArrayEquals(bytes, out.toByteArray());
    }

    @Test
    public void toDTStringRepresentation() {
        Assert.assertEquals("dt,T,8,0", this.df.toStringRepresentation(this.access));
    }

    @Test
    public void testToSTString() {
        Assert.assertEquals("DatetimeField[name=dt]", this.df.toString());
    }

    @Test
    public void getNullName() {
        Assert.assertEquals("nf", this.nf.getName());
    }

    @Test
    public void getNullByteLength() {
        Assert.assertEquals(8, this.nf.getValueLength(this.access));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullValueWrongLength() throws IOException {
        final byte[] bytes = {1, 2, 3, 4};
        Assert.assertArrayEquals(bytes, this.nf.extractValue(this.access, bytes, 0, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullBadLengthValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4};
        Assert.assertArrayEquals(bytes, this.nf.extractValue(this.access, bytes, 0, 4));
    }

    @Test
    public void getNullValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8};
        Assert.assertArrayEquals(bytes, this.nf.extractValue(this.access, bytes, 0, 8));
    }

    @Test
    public void writeNullValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8};
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.nf.writeValue(this.access, out, bytes);
        Assert.assertArrayEquals(bytes, out.toByteArray());
    }

    @Test
    public void toNullStringRepresentation() {
        Assert.assertEquals("nf,0,8,0", this.nf.toStringRepresentation(this.access));
    }

    @Test
    public void testToNullString() {
        Assert.assertEquals("NullFlagsField[name=nf, length=8]", this.nf.toString());
    }

    @Test
    public void getIntName() {
        Assert.assertEquals("int", this.inf.getName());
    }

    @Test
    public void getIntByteLength() {
        Assert.assertEquals(4, this.inf.getValueLength(this.access));
    }

    @Test
    public void getIntValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4};
        Assert.assertEquals(Long.valueOf(67305985), this.inf.extractValue(this.access, bytes, 0, 4));
    }

    @Test
    public void writeIntValue() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.inf.writeValue(this.access, out, 67305985L);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4}, out.toByteArray());
    }

    @Test
    public void toIntStringRepresentation() {
        Assert.assertEquals("int,I,4,0", this.inf.toStringRepresentation(this.access));
    }

    @Test
    public void testIntToString() {
        Assert.assertEquals("IntegerField[name=int]", this.inf.toString());
    }
}