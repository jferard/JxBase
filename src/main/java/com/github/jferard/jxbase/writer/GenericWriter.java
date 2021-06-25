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

import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.XBaseWriter;

import java.io.IOException;
import java.util.Map;

/**
 * The generic writer: a combination of chunk writers.
 * @param <D> the dialect
 * @param <A> the access
 */
public class GenericWriter<A extends XBaseAccess, D extends XBaseDialect<A, D>> implements XBaseWriter {
    private final XBaseMetadataWriter<A, D> metadataWriter;
    private final XBaseRecordWriter<D> recordWriter;

    public GenericWriter(final XBaseMetadataWriter<A, D> metadataWriter,
                         final XBaseRecordWriter<D> recordWriter) {
        this.metadataWriter = metadataWriter;
        this.recordWriter = recordWriter;
    }

    @Override
    public void write(final Map<String, Object> objectByName) throws IOException {
        this.recordWriter.write(objectByName);
    }

    @Override
    public void close() throws IOException {
        this.recordWriter.close();
        this.metadataWriter.fixMetadata(this.recordWriter.getRecordQty());
        this.metadataWriter.close();
    }
}
