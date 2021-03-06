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

package com.github.jferard.jxbase.dialect.db2.reader;

import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.reader.XBaseChunkReaderFactory;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.IOException;
import java.util.Collections;

public class DB2ChunkReaderFactoryTest {
    @Test
    public void test() throws IOException {
        final DB2Dialect dialect =
                DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET);
        final XBaseChunkReaderFactory<DB2Access, DB2Dialect> factory =
                dialect.getInternalReaderFactory();
        factory.createFieldDescriptorArrayReader(null, null);
        Assert.assertThrows(UnsupportedOperationException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                factory.createMemoReader(null, null, null);
            }
        });
        factory.createMetadataReader(null);
        factory.createFieldDescriptorArrayReader(null, null);
        final GenericFieldDescriptorArray<DB2Access> array =
                new GenericFieldDescriptorArray<DB2Access>(
                        Collections.<XBaseField<? super DB2Access>>emptyList(), 0, 0);
        factory.createRecordReader(null, null, null, null,
                array, null);
        factory.createOptionalReader(null, null, null, array);
    }
}