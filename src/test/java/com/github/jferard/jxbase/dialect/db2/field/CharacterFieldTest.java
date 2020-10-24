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

public class CharacterFieldTest {
    private CLNFieldsAccess access;
    private CharacterField cf;

    @Before
    public void setUp() {
        this.access = DB2Access.create(JxBaseUtils.ASCII_CHARSET);
        this.cf = new CharacterField("char", 20);
    }

    @Test
    public void getCharName() {
        Assert.assertEquals("char", this.cf.getName());
    }

    @Test
    public void getCharByteLength() {
        Assert.assertEquals(20, this.cf.getValueLength(this.access));
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
    public void writeNullCharValue() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.cf.writeValue(this.access, out, null);
        Assert.assertEquals("                    ",
                out.toString(String.valueOf(JxBaseUtils.ASCII_CHARSET)));
    }

    @Test
    public void toCharStringRepresentation() {
        Assert.assertEquals("char,C,20,0", this.cf.toStringRepresentation(this.access));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toCharStringRepresentationException() {
        final CharacterField field = new CharacterField("char", 734);
        field.toStringRepresentation(this.access);
    }

    @Test
    public void testCharToString() {
        Assert.assertEquals("CharacterField[name=char, length=20]", this.cf.toString());
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(3052994, this.cf.hashCode());
        Assert.assertEquals(this.cf, this.cf);
        Assert.assertNotEquals(this.cf, new Object());
        final CharacterField f2 = new CharacterField("char", 18);
        Assert.assertNotEquals(this.cf, f2);
        final CharacterField f3 = new CharacterField("char", 20);
        Assert.assertEquals(this.cf, f3);
    }
}