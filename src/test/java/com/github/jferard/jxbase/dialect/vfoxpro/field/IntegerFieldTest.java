/*
* JxBase - Copyright (c) 2019-2021 Julien Férard
* JDBF - Copyright (c) 2012-2018 Ivan Ryndin (https://github.com/iryndin)
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

public class IntegerFieldTest {
    private IntegerField f;
    private IntegerAccess access;

    @Before
    public void setUp() {
        this.access = new FoxProIntegerAccess();
        this.f = new IntegerField("i");
    }

    @Test
    public void getName() {
        Assert.assertEquals("i", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(4, this.f.getValueLength(this.access));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4};
        Assert.assertEquals(Long.valueOf(67305985), this.f.extractValue(this.access, bytes, 0, 4));
    }

    @Test
    public void writeValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.f.writeValue(this.access, bos, 67305985L);
        final byte[] bytes = {1, 2, 3, 4};
        Assert.assertArrayEquals(bytes, bos.toByteArray());
    }

    @Test
    public void writeNullValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.f.writeValue(this.access, bos, null);
        final byte[] bytes = {0x20, 0x20, 0x20, 0x20};
        Assert.assertArrayEquals(bytes, bos.toByteArray());
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("i,I,4,0", this.f.toStringRepresentation(this.access));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("IntegerField[name=i]", this.f.toString());
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(105, this.f.hashCode());
        Assert.assertEquals(this.f, this.f);
        Assert.assertNotEquals(this.f, new Object());
        final IntegerField f2 = new IntegerField("i");
        Assert.assertEquals(this.f, f2);
    }
}