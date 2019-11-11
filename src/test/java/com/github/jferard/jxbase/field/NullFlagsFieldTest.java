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

import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.foxpro.NullFlagsField;
import com.github.jferard.jxbase.dialect.foxpro.FoxProRecordReader;
import com.github.jferard.jxbase.dialect.foxpro.FoxProRecordWriter;
import com.github.jferard.jxbase.writer.internal.GenericFieldDescriptorArrayWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class NullFlagsFieldTest {
    private NullFlagsField f;
    private FoxProDialect dialect;
    private GenericFieldDescriptorArrayWriter aw;
    private FoxProRecordReader r;
    private FoxProRecordWriter w;

    @Before
    public void setUp() throws Exception {
        this.dialect = new FoxProDialect(XBaseFileTypeEnum.dBASEIV1);
        this.aw = Mockito.mock(GenericFieldDescriptorArrayWriter.class);
        this.r = Mockito.mock(FoxProRecordReader.class);
        this.w = Mockito.mock(FoxProRecordWriter.class);
        this.f = new NullFlagsField("nf", 8);
    }

    @Test
    public void getName() {
        Assert.assertEquals("nf", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(8, this.f.getValueByteLength(this.dialect));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4};
        Mockito.when(this.r.getNullFlagsValue(bytes, 0, 4)).thenReturn(bytes);
        Assert.assertEquals(bytes, this.f.getValue(this.r, bytes, 0, 4));
    }

    @Test
    public void writeValue() throws IOException {
        final byte[] bytes = {1};
        this.f.writeValue(this.w, bytes);
        Mockito.verify(this.w).writeNullFlagsValue(bytes, 8);
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("nf,0,8,0", this.f.toStringRepresentation(this.dialect));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("NullFlagsField[name=nf, length=8]", this.f.toString());
    }
}