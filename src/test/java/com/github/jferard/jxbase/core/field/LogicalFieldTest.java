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

package com.github.jferard.jxbase.core.field;

import com.github.jferard.jxbase.core.GenericDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.writer.internal.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.internal.XBaseRecordWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class LogicalFieldTest {
    private LogicalField f;
    private GenericDialect dialect;
    private XBaseFieldDescriptorArrayWriter aw;
    private XBaseRecordReader r;
    private XBaseRecordWriter w;

    @Before
    public void setUp() throws Exception {
        this.dialect = new GenericDialect(XBaseFileTypeEnum.dBASEIV1);
        this.aw = Mockito.mock(XBaseFieldDescriptorArrayWriter.class);
        this.r = Mockito.mock(XBaseRecordReader.class);
        this.w = Mockito.mock(XBaseRecordWriter.class);
        this.f = new LogicalField("bool");
    }

    @Test
    public void getName() {
        Assert.assertEquals("bool", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(1, this.f.getValueByteLength(this.dialect));
    }

    @Test
    public void write() throws IOException {
        this.f.write(this.aw, 5);
        Mockito.verify(this.aw).writeLogicalField("bool", 5);
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = {0};
        Mockito.when(this.r.getLogicalValue(bytes, 0, 1)).thenReturn(true);
        Assert.assertTrue(this.f.getValue(this.r, bytes, 0, 1));
    }

    @Test
    public void writeValue() throws IOException {
        this.f.writeValue(this.w, true);
        Mockito.verify(this.w).writeLogicalValue(true);
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("bool,L,1,0", this.f.toStringRepresentation(this.dialect));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("LogicalField[name=bool]", this.f.toString());
    }
}