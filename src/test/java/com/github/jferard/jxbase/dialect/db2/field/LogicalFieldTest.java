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

public class LogicalFieldTest {
    private CLNFieldsAccess access;
    private LogicalField lf;

    @Before
    public void setUp() {
        this.access = DB2Access.create(JxBaseUtils.ASCII_CHARSET);
        this.lf = new LogicalField("bool");
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
    public void testEquals() {
        Assert.assertEquals(this.lf, this.lf);
        Assert.assertEquals(3029738, this.lf.hashCode());
        Assert.assertNotEquals(this.lf, new Object());
        final LogicalField f2 = new LogicalField("char");
        Assert.assertNotEquals(this.lf, f2);
        final LogicalField f3 = new LogicalField("bool");
        Assert.assertEquals(this.lf, f3);
    }
}