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

import com.github.jferard.jxbase.dialect.db2.field.DB2LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LogicalFieldTest {
    private LogicalField f;
    private LogicalAccess access;

    @Before
    public void setUp() throws Exception {
        this.access = new DB2LogicalAccess(new RawRecordReader(JxBaseUtils.ASCII_CHARSET),
                new RawRecordWriter(JxBaseUtils.ASCII_CHARSET));
        this.f = new LogicalField("bool");
    }

    @Test
    public void getName() {
        Assert.assertEquals("bool", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(1, this.f.getValueByteLength(this.access));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = {'t'};
        Assert.assertTrue(this.f.getValue(this.access, bytes, 0, 1));
    }

    @Test
    public void writeValue() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.f.writeValue(this.access, out, false);
        Assert.assertArrayEquals(new byte[] {'F'}, out.toByteArray());
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("bool,L,1,0", this.f.toStringRepresentation(this.access));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("LogicalField[name=bool]", this.f.toString());
    }
}