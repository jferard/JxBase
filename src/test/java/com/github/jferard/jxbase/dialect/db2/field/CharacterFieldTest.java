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

import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.DB2CharacterAccess;
import com.github.jferard.jxbase.field.RawRecordReader;
import com.github.jferard.jxbase.field.RawRecordWriter;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CharacterFieldTest {
    private CharacterField f;
    private CharacterAccess access;

    @Before
    public void setUp() {
        this.access = new DB2CharacterAccess(new RawRecordReader(JxBaseUtils.ASCII_CHARSET),
                new RawRecordWriter(JxBaseUtils.ASCII_CHARSET));
        this.f = new CharacterField("char", 20);
    }

    @Test
    public void getName() {
        Assert.assertEquals("char", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(20, this.f.getValueByteLength(this.access));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = "some text           ".getBytes(JxBaseUtils.ASCII_CHARSET);
        Assert.assertEquals("some text", this.f.getValue(this.access, bytes, 0, 20));
    }

    @Test
    public void writeValue() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.f.writeValue(this.access, out, "some text");
        Assert.assertEquals("some text           ",
                out.toString(String.valueOf(JxBaseUtils.ASCII_CHARSET)));
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("char,C,20,0", this.f.toStringRepresentation(this.access));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("CharacterField[name=char, length=20]", this.f.toString());
    }
}