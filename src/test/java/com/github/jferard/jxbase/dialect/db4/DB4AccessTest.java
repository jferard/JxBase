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

package com.github.jferard.jxbase.dialect.db4;

import com.github.jferard.jxbase.dialect.db4.field.FloatField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class DB4AccessTest {
    private FloatField f;
    private CDFLMNFieldsAccess access;

    @Before
    public void setUp() {
        this.access =
                DB4DialectFactory.create(null, JxBaseUtils.ASCII_CHARSET, JxBaseUtils.UTC_TIME_ZONE)
                        .build().getAccess();
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
        final byte[] bytes = "               18.94".getBytes(JxBaseUtils.ASCII_CHARSET);
        Assert.assertEquals(new BigDecimal("18.94"), this.f.getValue(this.access, bytes, 0, 20));
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeBadValue() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.f.writeValue(this.access, out, "some text");
    }

    @Test
    public void writeValue() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.f.writeValue(this.access, out, 18.94);
        Assert.assertEquals("               18.94",
                out.toString(String.valueOf(JxBaseUtils.ASCII_CHARSET)));
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("float,F,20,0", this.f.toStringRepresentation(this.access));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("FloatField[name=float]", this.f.toString());
    }
}