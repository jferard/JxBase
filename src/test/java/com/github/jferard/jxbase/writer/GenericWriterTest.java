/*
* JxBase - Copyright (c) 2019-2021 Julien Férard
* JDBF - Copyright (c) 2012-2018 Ivan Ryndin (https://github.com/iryndin)
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

import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GenericWriterTest {
    @Test
    @SuppressWarnings("unchecked")
    public void test() throws IOException {
        final XBaseMetadataWriter<DB3Access, DB3Dialect> metadataWriter = PowerMock.createMock(XBaseMetadataWriter.class);
        final XBaseRecordWriter<DB3Dialect> recordWriter = PowerMock.createMock(XBaseRecordWriter.class);
        final GenericWriter<DB3Access, DB3Dialect> gw = new GenericWriter<DB3Access, DB3Dialect>(metadataWriter, recordWriter);
        final Map<String, Object> objectByName = new HashMap<String, Object>();
        PowerMock.resetAll();

        EasyMock.expect(recordWriter.getRecordQty()).andReturn(10);
        recordWriter.write(objectByName);
        recordWriter.close();
        metadataWriter.fixMetadata(10);
        metadataWriter.close();
        PowerMock.replayAll();

        gw.write(objectByName);
        gw.close();
        PowerMock.verifyAll();
    }
}