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

package com.github.jferard.jxbase.dialect.db2.writer;

import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.XBaseChunkWriterFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

public class DB2ChunkWriterFactoryTest {
    @Test
    public void test() throws IOException {
        final DB2Dialect dialect =
                DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET);
        final XBaseChunkWriterFactory<DB2Access, DB2Dialect> factory =
                dialect.getInternalWriterFactory();
        factory.createFieldDescriptorArrayWriter(null, null);
        factory.createMemoWriter(null, null, null);
        factory.createMetadataWriter(null, null, null);
        factory.createFieldDescriptorArrayWriter(null, null);
        final GenericFieldDescriptorArray<DB2Access> array =
                new GenericFieldDescriptorArray<DB2Access>(
                        Collections.<XBaseField<? super DB2Access>>emptyList(), 0, 0);
        factory.createRecordWriter(null, null, null, null, array, null);
        factory.createOptionalWriter(null, null, array);
    }
}