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

package com.github.jferard.jxbase.dialect.foxpro.writer;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.foxpro.FoxProAccess;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialectFactory;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

public class FoxProInternalWriterFactoryTest {

    private FoxProInternalWriterFactory factory;

    @Before
    public void setUp() throws IOException {
        final FoxProDialectFactory f =
                new FoxProDialectFactory(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE);
        final XBaseDialect<FoxProDialect, FoxProAccess> dialect = f.build();
        this.factory =
                new FoxProInternalWriterFactory((FoxProDialect) dialect, JxBaseUtils.UTC_TIME_ZONE);
    }

    @Test
    public void test() {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final XBaseFieldDescriptorArray array = Mockito.mock(XBaseFieldDescriptorArray.class);

        Mockito.when(array.getFields()).thenReturn(Collections.emptyList());

        this.factory.createFieldDescriptorArrayWriter(bos, null);
        this.factory.createOptionalWriter(bos, null, null);
        this.factory.createRecordWriter(bos, null, null, array, null);
        this.factory.createMetadataWriter(null, null, null);
    }
}