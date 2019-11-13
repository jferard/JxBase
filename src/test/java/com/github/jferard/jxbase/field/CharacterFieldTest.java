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

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.BasicDialect;
import com.github.jferard.jxbase.dialect.memo.WithMemoDialect;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.writer.internal.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.internal.XBaseRecordWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class CharacterFieldTest {
    private CharacterField f;
    private BasicDialect dialect;
    private XBaseFieldDescriptorArrayWriter aw;
    private XBaseRecordReader r;
    private XBaseRecordWriter w;

    @Before
    public void setUp() throws Exception {
        this.dialect = new WithMemoDialect(XBaseFileTypeEnum.dBASEIV1);
        this.aw = Mockito.mock(XBaseFieldDescriptorArrayWriter.class);
        this.r = Mockito.mock(XBaseRecordReader.class);
        this.w = Mockito.mock(XBaseRecordWriter.class);
        this.f = new CharacterField("char", 20);
    }

    @Test
    public void getName() {
        Assert.assertEquals("char", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(20, this.f.getValueByteLength(this.dialect));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = {0};
        Mockito.when(this.r.getCharacterValue(bytes, 0, 20)).thenReturn("some text");
        Assert.assertEquals("some text", this.f.getValue(this.r, bytes, 0, 20));
    }

    @Test
    public void writeValue() throws IOException {
        this.f.writeValue(this.w, "some text");
        Mockito.verify(this.w).writeCharacterValue("some text", 20);
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("char,C,20,0", this.f.toStringRepresentation(this.dialect));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("CharacterField[name=char, length=20]", this.f.toString());
    }
}