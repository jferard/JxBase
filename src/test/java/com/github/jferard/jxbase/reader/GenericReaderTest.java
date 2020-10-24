/*
 * JxBase - Copyright (c) 2019-2020 Julien Férard
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

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.core.XBaseRecord;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public class GenericReaderTest {
    private XBaseChunkReaderFactory<DB3Dialect, DB3Access> rf;
    private InputStream is;
    private DB3Dialect dialect;
    private XBaseMetadataReader mdr;
    private XBaseMetadata metadata;
    private XBaseFieldDescriptorArray<DB3Access> array;
    private XBaseFieldDescriptorArrayReader<DB3Dialect, DB3Access> ar;
    private XBaseOptionalReader or;
    private XBaseOptional optional;
    private XBaseRecordReader rr;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        this.is = PowerMock.createMock(InputStream.class);
        this.mdr = PowerMock.createMock(XBaseMetadataReader.class);
        this.metadata = PowerMock.createMock(XBaseMetadata.class);
        this.ar = PowerMock.createMock(XBaseFieldDescriptorArrayReader.class);
        this.array = PowerMock.createMock(XBaseFieldDescriptorArray.class);
        this.or = PowerMock.createMock(XBaseOptionalReader.class);
        this.optional = PowerMock.createMock(XBaseOptional.class);
        this.rr = PowerMock.createMock(XBaseRecordReader.class);

        this.dialect = PowerMock.createMock(DB3Dialect.class);
        this.rf = PowerMock.createMock(XBaseChunkReaderFactory.class);
    }

    private void init() throws IOException {
        EasyMock.expect(this.rf.createMetadataReader(this.is)).andReturn(this.mdr);
        EasyMock.expect(this.mdr.read()).andReturn(this.metadata);
        EasyMock.expect(this.rf.createFieldDescriptorArrayReader(this.is, this.metadata))
                .andReturn(this.ar);
        EasyMock.expect(this.ar.read()).andReturn(this.array);
        EasyMock.expect(this.rf.createOptionalReader(this.is, JxBaseUtils.ASCII_CHARSET, this.metadata,
                this.array)).andReturn(this.or);
        EasyMock.expect(this.or.read()).andReturn(this.optional);
        EasyMock.expect(this.rf.createRecordReader(this.is, JxBaseUtils.ASCII_CHARSET, this.metadata,
                this.array, this.optional)).andReturn(this.rr);
        EasyMock.expect(this.dialect.getMetaDataLength()).andReturn(512);
        EasyMock.expect(this.array.getArrayLength()).andReturn(512).times(1);
        EasyMock.expect(this.optional.getLength()).andReturn(512).times(1);
        EasyMock.expect(this.metadata.getFullHeaderLength()).andReturn(512*3).times(1);
    }

    private GenericReader<DB3Dialect, DB3Access> getGenericReader() throws IOException {
        return new GenericReader<DB3Dialect, DB3Access>(this.dialect, this.is, JxBaseUtils.ASCII_CHARSET, this.rf);
    }

    @Test
    public void getDialect() throws IOException {
        PowerMock.resetAll();

        this.init();
        PowerMock.replayAll();

        final DB3Dialect dialect = this.getGenericReader().getDialect();
        PowerMock.verifyAll();

        Assert.assertEquals(this.dialect, dialect);
    }

    @Test
    public void getMetadata() throws IOException {
        PowerMock.resetAll();

        this.init();
        PowerMock.replayAll();

        final XBaseMetadata metadata = this.getGenericReader().getMetadata();
        PowerMock.verifyAll();

        Assert.assertEquals(this.metadata, metadata);
    }

    @Test
    public void getFieldDescriptorArray() throws IOException {
        PowerMock.resetAll();

        this.init();
        PowerMock.replayAll();

        final XBaseFieldDescriptorArray<DB3Access> fieldDescriptorArray =
                this.getGenericReader().getFieldDescriptorArray();
        PowerMock.verifyAll();

        Assert.assertEquals(this.array, fieldDescriptorArray);
    }

    @Test
    public void getOptional() throws IOException {
        PowerMock.resetAll();

        this.init();
        PowerMock.replayAll();

        final XBaseOptional optional = this.getGenericReader().getOptional();
        PowerMock.verifyAll();

        Assert.assertEquals(this.optional, optional);
    }

    @Test
    public void read() throws IOException, ParseException {
        final XBaseRecord record = PowerMock.createMock(XBaseRecord.class);
        PowerMock.resetAll();

        this.init();
        EasyMock.expect(this.rr.read()).andReturn(record);
        PowerMock.replayAll();

        final XBaseRecord rec = this.getGenericReader().read();
        PowerMock.verifyAll();

        Assert.assertEquals(record, rec);
    }

    @Test
    public void testClose() throws IOException {
        PowerMock.resetAll();

        this.init();
        this.is.close();
        PowerMock.replayAll();

        this.getGenericReader().close();
        PowerMock.verifyAll();
    }
}