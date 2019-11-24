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

package com.github.jferard.jxbase.field;

import com.github.jferard.jxbase.dialect.db2.field.DB2NumericAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class NumericFieldTest {
    private NumericField f;
    private NumericAccess access;

    @Before
    public void setUp() throws Exception {
        this.access = new DB2NumericAccess(new RawRecordReader(JxBaseUtils.ASCII_CHARSET),
                new RawRecordWriter(JxBaseUtils.ASCII_CHARSET));
        this.f = new NumericField("num", 10, 2);
    }

    @Test
    public void getName() {
        Assert.assertEquals("num", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(10, this.f.getValueByteLength(this.access));
    }

    @Test
    public void getNumberOfDecimalPlaces() {
        Assert.assertEquals(2, this.f.getNumberOfDecimalPlaces());
    }

    @Test
    public void getValue() throws IOException, IOException {
        final byte[] bytes = "     18.90".getBytes(JxBaseUtils.ASCII_CHARSET);
        final BigDecimal v = new BigDecimal("18.90");
        final BigDecimal value = this.f.getValue(this.access, bytes, 0, 10);
        Assert.assertEquals(v, value);
    }

    @Test
    public void writeValue() throws IOException {
        final BigDecimal v = new BigDecimal("18.9");
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.f.writeValue(this.access, out, v);
        Assert.assertArrayEquals(new byte[]{' ', ' ', ' ', ' ', ' ', '1', '8', '.', '9', '0'},
                out.toByteArray());
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("num,N,10,2", this.f.toStringRepresentation(this.access));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("NumericField[name=num, length=10, numberOfDecimalPlaces=2]",
                this.f.toString());
    }
}