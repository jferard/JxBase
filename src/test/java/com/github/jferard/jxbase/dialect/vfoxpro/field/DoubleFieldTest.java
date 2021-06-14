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

package com.github.jferard.jxbase.dialect.vfoxpro.field;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DoubleFieldTest {
    private DoubleField f;
    private DoubleAccess access;

    @Before
    public void setUp() throws Exception {
        this.access = new FoxProDoubleAccess();
        this.f = new DoubleField("d", 2);
    }

    @Test
    public void getName() {
        Assert.assertEquals("d", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(8, this.f.getValueLength(this.access));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes =
                {0x40, 0x09, 0x21, (byte) 0xFB, 0x54, 0x44, 0x2D, 0x18};
        Assert.assertEquals(3.141592653589793d, (Double) this.f.extractValue(this.access, bytes, 0, 8), 1e-10);
    }

    @Test
    public void writeValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.f.writeValue(this.access, bos, 3.141592653589793d);

        final byte[] bytes =
                {0x40, 0x09, 0x21, (byte) 0xFB, 0x54, 0x44, 0x2D, 0x18};
        Assert.assertArrayEquals(bytes, bos.toByteArray());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("DoubleField[name=d, numberOfDecimalPlaces=2]", this.f.toString());
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("d,B,8,2", this.f.toStringRepresentation(this.access));
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(162, this.f.hashCode());
        Assert.assertEquals(this.f, this.f);
        Assert.assertNotEquals(this.f, new Object());
        final DoubleField f2 = new DoubleField("d", 3);
        Assert.assertNotEquals(this.f, f2);
        final DoubleField f3 = new DoubleField("d", 2);
        Assert.assertEquals(this.f, f3);
    }
}