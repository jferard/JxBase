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

import com.github.jferard.jxbase.core.memo.TextMemoRecord;
import com.github.jferard.jxbase.core.FoxProDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.internal.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.internal.XBaseRecordWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class MemoFieldTest {
    private MemoField<TextMemoRecord> f;
    private FoxProDialect dialect;
    private XBaseFieldDescriptorArrayWriter aw;
    private XBaseRecordReader r;
    private XBaseRecordWriter w;

    @Before
    public void setUp() throws Exception {
        this.dialect = new FoxProDialect(XBaseFileTypeEnum.dBASEIV1);
        this.aw = Mockito.mock(XBaseFieldDescriptorArrayWriter.class);
        this.r = Mockito.mock(XBaseRecordReader.class);
        this.w = Mockito.mock(XBaseRecordWriter.class);
        this.f = new MemoField<TextMemoRecord>("memo");
    }

    @Test
    public void getName() {
        Assert.assertEquals("memo", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(10, this.f.getValueByteLength(this.dialect));
    }

    @Test
    public void write() throws IOException {
        this.f.write(this.aw, 5);
        Mockito.verify(this.aw).writeMemoField("memo", 5);
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4};
        final TextMemoRecord record = new TextMemoRecord("a", 1, JxBaseUtils.ASCII_CHARSET);
        Mockito.<XBaseMemoRecord<?>>when(this.r.getMemoValue(bytes, 0, 4)).thenReturn(record);
        Assert.assertEquals(record, this.f.getValue(this.r, bytes, 0, 4));
    }

    @Test
    public void writeValue() throws IOException {
        final TextMemoRecord record = new TextMemoRecord("a", 1, JxBaseUtils.ASCII_CHARSET);
        this.f.writeValue(this.w, record);
        Mockito.verify(this.w).writeMemoValue(record);
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("memo,M,10,0", this.f.toStringRepresentation(this.dialect));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("MemoField[name=memo]", this.f.toString());
    }
}