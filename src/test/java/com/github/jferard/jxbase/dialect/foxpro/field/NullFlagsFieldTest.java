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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NullFlagsFieldTest {
    private NullFlagsField f;
    private NullFlagsAccess access;

    @Before
    public void setUp() throws Exception {
        this.access = new FoxProNullFlagsAccess();
        this.f = new NullFlagsField("nf", 8);
    }

    @Test
    public void getName() {
        Assert.assertEquals("nf", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(8, this.f.getValueLength(this.access));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getValueWrongLength() throws IOException {
        final byte[] bytes = {1, 2, 3, 4};
        Assert.assertArrayEquals(bytes, this.f.getValue(this.access, bytes, 0, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBadLengthValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4};
        Assert.assertArrayEquals(bytes, this.f.getValue(this.access, bytes, 0, 4));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8};
        Assert.assertArrayEquals(bytes, this.f.getValue(this.access, bytes, 0, 8));
    }

    @Test
    public void writeValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8};
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.f.writeValue(this.access, out, bytes);
        Assert.assertArrayEquals(bytes, out.toByteArray());
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("nf,0,8,0", this.f.toStringRepresentation(this.access));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("NullFlagsField[name=nf, length=8]", this.f.toString());
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(3760, this.f.hashCode());
        Assert.assertEquals(this.f, this.f);
        Assert.assertNotEquals(this.f, new Object());
        final NullFlagsField f2 = new NullFlagsField("nf", 9);
        Assert.assertNotEquals(this.f, f2);
        final NullFlagsField f3 = new NullFlagsField("nf", 8);
        Assert.assertEquals(this.f, f3);
    }
}