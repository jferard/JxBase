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

package com.github.jferard.jxbase.dialect.db3.field;

public class MemoFieldTest {
    /*
    private MemoField<TextMemoRecord> f;
    private FoxProDialect dialect;
    private XBaseFieldDescriptorArrayWriter<D> aw;
    private WithMemoRecordReader r;
    private DB3MemoRecordWriter w;

    @Before
    public void setUp() throws Exception {
        this.dialect = new FoxProDialect(XBaseFileTypeEnum.dBASE4SQLTable);
        this.aw = Mockito.mock(XBaseFieldDescriptorArrayWriter.class);
        this.r = Mockito.mock(WithMemoRecordReader.class);
        this.w = Mockito.mock(DB3MemoRecordWriter.class);
        this.f = new MemoField<TextMemoRecord>("memo");
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
        final TextMemoRecord record = new TextMemoRecord("a", JxBaseUtils.ASCII_CHARSET);
        Mockito.<XBaseMemoRecord>when(this.r.getMemoValue(bytes, 0, 4)).thenReturn(record);
        Assert.assertEquals(record, this.f.getValue(this.r, bytes, 0, 4));
    }

    @Test
    public void writeValue() throws IOException {
        final TextMemoRecord record = new TextMemoRecord("a", JxBaseUtils.ASCII_CHARSET);
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

     */
}