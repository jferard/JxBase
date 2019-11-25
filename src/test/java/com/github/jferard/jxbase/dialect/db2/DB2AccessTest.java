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

package com.github.jferard.jxbase.dialect.db2;

import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.LogicalField;
import com.github.jferard.jxbase.dialect.db2.field.NumericField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class DB2AccessTest {
    private CLNFieldsAccess access;
    private CharacterField cf;
    private LogicalField lf;
    private NumericField nf;

    @Before
    public void setUp() {
        this.access = DB2Access.create(JxBaseUtils.ASCII_CHARSET);
        this.cf = new CharacterField("char", 20);
        this.lf = new LogicalField("bool");
        this.nf = new NumericField("num", 10, 2);
    }

    @Test
    public void getCharName() {
        Assert.assertEquals("char", this.cf.getName());
    }

    @Test
    public void getCharByteLength() {
        Assert.assertEquals(20, this.cf.getValueByteLength(this.access));
    }

    @Test
    public void getCharValue() throws IOException {
        final byte[] bytes = "some text           ".getBytes(JxBaseUtils.ASCII_CHARSET);
        Assert.assertEquals("some text", this.cf.getValue(this.access, bytes, 0, 20));
    }

    @Test
    public void writeCharValue() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.cf.writeValue(this.access, out, "some text");
        Assert.assertEquals("some text           ",
                out.toString(String.valueOf(JxBaseUtils.ASCII_CHARSET)));
    }

    @Test
    public void toCharStringRepresentation() {
        Assert.assertEquals("char,C,20,0", this.cf.toStringRepresentation(this.access));
    }

    @Test
    public void testCharToString() {
        Assert.assertEquals("CharacterField[name=char, length=20]", this.cf.toString());
    }

    @Test
    public void getLogicName() {
        Assert.assertEquals("bool", this.lf.getName());
    }

    @Test
    public void getLogicByteLength() {
        Assert.assertEquals(1, this.lf.getValueByteLength(this.access));
    }

    @Test
    public void getLogicValue() throws IOException {
        final byte[] bytes = {'t'};
        Assert.assertTrue(this.lf.getValue(this.access, bytes, 0, 1));
    }

    @Test
    public void writeLogicValue() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.lf.writeValue(this.access, out, false);
        Assert.assertArrayEquals(new byte[]{'F'}, out.toByteArray());
    }

    @Test
    public void toLogicStringRepresentation() {
        Assert.assertEquals("bool,L,1,0", this.lf.toStringRepresentation(this.access));
    }

    @Test
    public void testLogicToString() {
        Assert.assertEquals("LogicalField[name=bool]", this.lf.toString());
    }

    @Test
    public void getNumName() {
        Assert.assertEquals("num", this.nf.getName());
    }

    @Test
    public void getNumByteLength() {
        Assert.assertEquals(10, this.nf.getValueByteLength(this.access));
    }

    @Test
    public void getNumNumberOfDecimalPlaces() {
        Assert.assertEquals(2, this.nf.getNumberOfDecimalPlaces());
    }

    @Test
    public void getNumValue() throws IOException, IOException {
        final byte[] bytes = "     18.90".getBytes(JxBaseUtils.ASCII_CHARSET);
        final BigDecimal v = new BigDecimal("18.90");
        final BigDecimal value = this.nf.getValue(this.access, bytes, 0, 10);
        Assert.assertEquals(v, value);
    }

    @Test
    public void writeNumValue() throws IOException {
        final BigDecimal v = new BigDecimal("18.9");
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.nf.writeValue(this.access, out, v);
        Assert.assertArrayEquals(new byte[]{' ', ' ', ' ', ' ', ' ', '1', '8', '.', '9', '0'},
                out.toByteArray());
    }

    @Test
    public void toNumStringRepresentation() {
        Assert.assertEquals("num,N,10,2", this.nf.toStringRepresentation(this.access));
    }

    @Test
    public void testNumToString() {
        Assert.assertEquals("NumericField[name=num, length=10, numberOfDecimalPlaces=2]",
                this.nf.toString());
    }
}
