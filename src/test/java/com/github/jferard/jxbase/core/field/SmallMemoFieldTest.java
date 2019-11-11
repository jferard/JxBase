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

import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.memo.TextMemoRecord;
import com.github.jferard.jxbase.core.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.dialect.foxpro.SmallMemoField;
import com.github.jferard.jxbase.dialect.foxpro.FoxProRecordReader;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.dialect.foxpro.FoxProRecordWriter;
import com.github.jferard.jxbase.writer.internal.GenericFieldDescriptorArrayWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class SmallMemoFieldTest {
    private SmallMemoField<TextMemoRecord> f;
    private FoxProDialect dialect;
    private GenericFieldDescriptorArrayWriter aw;
    private FoxProRecordReader r;
    private FoxProRecordWriter w;

    @Before
    public void setUp() {
        this.dialect = new FoxProDialect(XBaseFileTypeEnum.dBASEIV1);
        this.aw = Mockito.mock(GenericFieldDescriptorArrayWriter.class);
        this.r = Mockito.mock(FoxProRecordReader.class);
        this.w = Mockito.mock(FoxProRecordWriter.class);
        this.f = new SmallMemoField<TextMemoRecord>("memo");
    }

    @Test
    public void getName() {
        Assert.assertEquals("memo", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(4, this.f.getValueByteLength(this.dialect));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4};
        final TextMemoRecord record = new TextMemoRecord("a", 1, JxBaseUtils.ASCII_CHARSET);
        Mockito.<XBaseMemoRecord<?>>when(this.r.getSmallMemoValue(bytes, 0, 4)).thenReturn(record);
        Assert.assertEquals(record, this.f.getValue(this.r, bytes, 0, 4));
    }

    @Test
    public void writeValue() throws IOException {
        final TextMemoRecord record = new TextMemoRecord("a", 1, JxBaseUtils.ASCII_CHARSET);
        this.f.writeValue(this.w, record);
        Mockito.verify(this.w).writeSmallMemoValue(record);
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("memo,M,4,0", this.f.toStringRepresentation(this.dialect));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("SmallMemoField[name=memo]", this.f.toString());
    }
}