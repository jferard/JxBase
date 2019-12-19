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

package com.github.jferard.jxbase.writer;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GenericWriterTest {
    @Test
    public void test() throws IOException {
        final XBaseMetadataWriter metadataWriter = Mockito.mock(XBaseMetadataWriter.class);
        final XBaseRecordWriter recordWriter = Mockito.mock(XBaseRecordWriter.class);
        final GenericWriter gw = new GenericWriter(metadataWriter, recordWriter);

        Mockito.when(recordWriter.getRecordQty()).thenReturn(10);

        final Map<String, Object> objectByName = new HashMap<>();
        gw.write(objectByName);
        gw.close();

        Mockito.verify(recordWriter).write(objectByName);
        Mockito.verify(recordWriter).close();
        Mockito.verify(metadataWriter).fixMetadata(10);
        Mockito.verify(metadataWriter).close();
    }
}