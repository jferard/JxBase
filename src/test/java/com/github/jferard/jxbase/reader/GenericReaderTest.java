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

package com.github.jferard.jxbase.reader;

public class GenericReaderTest {
    /*
    private WithMemoInternalReaderFactory rf;
    private InputStream is;
    private GenericReader gr;
    private XBaseMemoReader mr;
    private DB3MemoDialect dialect;
    private XBaseMetadataReader mdr;
    private XBaseMetadata metadata;
    private XBaseFieldDescriptorArray array;
    private XBaseFieldDescriptorArrayReader ar;
    private XBaseOptionalReader or;
    private XBaseOptional optional;
    private XBaseRecordReader rr;

    @Before
    public void setUp() throws Exception {
        this.is = Mockito.mock(InputStream.class);
        this.mdr = Mockito.mock(XBaseMetadataReader.class);
        this.metadata = Mockito.mock(XBaseMetadata.class);
        this.ar = Mockito.mock(XBaseFieldDescriptorArrayReader.class);
        this.array = Mockito.mock(XBaseFieldDescriptorArray.class);
        this.or = Mockito.mock(XBaseOptionalReader.class);
        this.optional = Mockito.mock(XBaseOptional.class);
        this.rr = Mockito.mock(XBaseRecordReader.class);

        this.dialect = Mockito.mock(DB3MemoDialect.class);
        this.rf = Mockito.mock(WithMemoInternalReaderFactory.class);
        this.mr = Mockito.mock(XBaseMemoReader.class);

        Mockito.when(this.rf.createMetadataReader(this.is)).thenReturn(this.mdr);
        Mockito.when(this.mdr.read()).thenReturn(this.metadata);
        Mockito.when(this.rf.createFieldDescriptorArrayReader(this.is, this.metadata))
                .thenReturn(this.ar);
        Mockito.when(this.ar.read()).thenReturn(this.array);
        Mockito.when(this.rf.createOptionalReader(this.is, JxBaseUtils.ASCII_CHARSET, this.metadata,
                this.array)).thenReturn(this.or);
        Mockito.when(this.or.read()).thenReturn(this.optional);
        Mockito.when(this.rf.createRecordReader(this.is, JxBaseUtils.ASCII_CHARSET, this.metadata,
                this.array, this.optional)).thenReturn(this.rr);

        this.gr = new GenericReader(this.dialect, this.is, JxBaseUtils.ASCII_CHARSET, this.rf);
    }

    @Test
    public void getDialect() {
        Assert.assertEquals(this.dialect, this.gr.getDialect());
    }

    @Test
    public void getMetadata() {
        Assert.assertEquals(this.metadata, this.gr.getMetadata());
    }

    @Test
    public void getFieldDescriptorArray() {
        Assert.assertEquals(this.array, this.gr.getFieldDescriptorArray());
    }

    @Test
    public void getOptional() {
        Assert.assertEquals(this.optional, this.gr.getOptional());
    }

    @Test
    public void read() throws IOException, ParseException {
        this.gr.read();
        Mockito.verify(this.rr).read();
    }

    @Test
    public void close() throws IOException {
        this.gr.close();
        Mockito.verify(this.is).close();
    }

     */
}