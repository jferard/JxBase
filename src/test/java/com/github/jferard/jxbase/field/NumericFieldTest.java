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

import com.github.jferard.jxbase.dialect.memo.WithMemoDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.writer.internal.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.internal.XBaseRecordWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;

public class NumericFieldTest {
    private NumericField f;
    private WithMemoDialect dialect;
    private XBaseFieldDescriptorArrayWriter aw;
    private XBaseRecordReader r;
    private XBaseRecordWriter w;

    @Before
    public void setUp() throws Exception {
        this.dialect = new WithMemoDialect(XBaseFileTypeEnum.dBASEIV1);
        this.aw = Mockito.mock(XBaseFieldDescriptorArrayWriter.class);
        this.r = Mockito.mock(XBaseRecordReader.class);
        this.w = Mockito.mock(XBaseRecordWriter.class);
        this.f = new NumericField("num", 10, 2);
    }

    @Test
    public void getName() {
        Assert.assertEquals("num", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(10, this.f.getValueByteLength(this.dialect));
    }

    @Test
    public void getNumberOfDecimalPlaces() {
        Assert.assertEquals(2, this.f.getNumberOfDecimalPlaces());
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = {0};
        final BigDecimal v = new BigDecimal(18.9);
        Mockito.when(this.r.getNumericValue(bytes, 0, 10, 2)).thenReturn(v);
        Assert.assertEquals(v, this.f.getValue(this.r, bytes, 0, 10));
    }

    @Test
    public void writeValue() throws IOException {
        final BigDecimal v = new BigDecimal(18.9);
        this.f.writeValue(this.w, v);
        Mockito.verify(this.w).writeNumericValue(v, 10, 2);
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("num,N,10,2", this.f.toStringRepresentation(this.dialect));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("NumericField[name=num, length=10, numberOfDecimalPlaces=2]",
                this.f.toString());
    }
}