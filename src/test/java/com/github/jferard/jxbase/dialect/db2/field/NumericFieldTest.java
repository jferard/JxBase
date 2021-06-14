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

package com.github.jferard.jxbase.dialect.db2.field;

import com.github.jferard.jxbase.dialect.db2.CLNFieldsAccess;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class NumericFieldTest {
    private CLNFieldsAccess access;
    private NumericField nf;

    @Before
    public void setUp() {
        this.access = DB2Access.create(JxBaseUtils.ASCII_CHARSET);
        this.nf = new NumericField("num", 10, 2);
    }

    @Test
    public void getNumericName() {
        Assert.assertEquals("num", this.nf.getName());
    }

    @Test
    public void getNumericByteLength() {
        Assert.assertEquals(10, this.nf.getValueLength(this.access));
    }

    @Test
    public void getNumericNumberOfDecimalPlaces() {
        Assert.assertEquals(2, this.nf.getNumberOfDecimalPlaces());
    }

    @Test
    public void getNumericValue() throws IOException, IOException {
        final byte[] bytes = "     18.90".getBytes(JxBaseUtils.ASCII_CHARSET);
        final BigDecimal v = new BigDecimal("18.90");
        final BigDecimal value = this.nf.extractValue(this.access, bytes, 0, 10);
        Assert.assertEquals(v, value);
    }

    @Test
    public void writeNumericValue() throws IOException {
        final BigDecimal v = new BigDecimal("18.9");
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.nf.writeValue(this.access, out, v);
        Assert.assertArrayEquals(new byte[]{' ', ' ', ' ', ' ', ' ', '1', '8', '.', '9', '0'},
                out.toByteArray());
    }

    @Test
    public void toNumericStringRepresentation() {
        Assert.assertEquals("num,N,10,2", this.nf.toStringRepresentation(this.access));
    }

    @Test
    public void testNumericToString() {
        Assert.assertEquals("NumericField[name=num, length=10, numberOfDecimalPlaces=2]",
                this.nf.toString());
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(this.nf, this.nf);
        Assert.assertEquals(119118, this.nf.hashCode());
        Assert.assertNotEquals(this.nf, new Object());
        final NumericField f2 = new NumericField("char", 18, 2);
        Assert.assertNotEquals(this.nf, f2);
        final NumericField f3 = new NumericField("num", 10, 2);
        Assert.assertEquals(this.nf, f3);
    }
}