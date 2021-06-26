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

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DB2LogicalAccessTest {
    private LogicalAccess access;

    @Before
    public void setUp() {
        this.access =
                new DB2LogicalAccess(new RawRecordWriteHelper(JxBaseUtils.ASCII_CHARSET));
    }

    @Test
    public void getLogicalValueLength() {
        Assert.assertEquals(1, this.access.getLogicalValueLength());
    }

    @Test
    public void extractLogicalValue() {
        Assert.assertTrue(this.access.extractLogicalValue(new byte[] {'T'}, 0, 1));
        Assert.assertFalse(this.access.extractLogicalValue(new byte[] {'F'}, 0, 1));
        Assert.assertNull(this.access.extractLogicalValue(new byte[] {'?'}, 0, 1));
        Assert.assertNull(this.access.extractLogicalValue(new byte[] {' '}, 0, 1));
    }

    @Test
    public void writeLogicalValueTrue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.access.writeLogicalValue(bos, true);
        Assert.assertArrayEquals(new byte[] {'T'}, bos.toByteArray());
    }

    @Test
    public void writeLogicalValueFalse() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.access.writeLogicalValue(bos, false);
        Assert.assertArrayEquals(new byte[] {'F'}, bos.toByteArray());
    }

    @Test
    public void writeLogicalValueNull() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.access.writeLogicalValue(bos, null);
        Assert.assertArrayEquals(new byte[] {'?'}, bos.toByteArray());
    }

    @Test
    public void getLogicalFieldRepresentation() {
        Assert.assertEquals(new FieldRepresentation("foo", 'L', 1, 0),
                this.access.getLogicalFieldRepresentation("foo"));
    }
}