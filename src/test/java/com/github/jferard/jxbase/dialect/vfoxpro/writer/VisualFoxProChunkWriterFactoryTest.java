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

package com.github.jferard.jxbase.dialect.vfoxpro.writer;

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialectFactory;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class VisualFoxProChunkWriterFactoryTest {

    private VisualFoxProChunkWriterFactory factory;

    @Before
    public void setUp() {
        final XBaseDialect<VisualFoxProAccess, VisualFoxProDialect> dialect = VisualFoxProDialectFactory
                .create(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE);
        this.factory =
                new VisualFoxProChunkWriterFactory((VisualFoxProDialect) dialect,
                        JxBaseUtils.UTC_TIME_ZONE);
    }

    @Test
    public void test() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        @SuppressWarnings("unchecked")
        final XBaseFieldDescriptorArray<VisualFoxProAccess> array =
                (XBaseFieldDescriptorArray<VisualFoxProAccess>) PowerMock
                        .createMock(XBaseFieldDescriptorArray.class);
        final XBaseMemoWriter memoWriter = PowerMock.createMock(XBaseMemoWriter.class);
        PowerMock.resetAll();

        EasyMock.expect(array.getFields()).andReturn(
                Collections.<XBaseField<? super VisualFoxProAccess>>emptyList());
        PowerMock.replayAll();

        this.factory.createFieldDescriptorArrayWriter(bos, null);
        this.factory.createOptionalWriter(bos, null, null);
        this.factory.createRecordWriter(bos, null, memoWriter, null, array, null);
        this.factory.createMetadataWriter(null, null, null);
        final File memo = File.createTempFile("table", "");
        memo.deleteOnExit();
        this.factory.createMemoWriter(XBaseFileTypeEnum.dBASE4Memo,
                memo.getAbsolutePath(), null);
        PowerMock.verifyAll();
    }
}