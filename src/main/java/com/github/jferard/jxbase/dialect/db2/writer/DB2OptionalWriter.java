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

package com.github.jferard.jxbase.dialect.db2.writer;

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.writer.XBaseOptionalWriter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes nothing.
 * @param <D> the dialect
 * @param <A> the access
 */
public class DB2OptionalWriter<D extends XBaseDialect<D, A>, A> implements XBaseOptionalWriter<D> {
    private final D dialect;
    private final OutputStream outputStream;
    private final XBaseMetadata metadata;
    private final XBaseFieldDescriptorArray<A> array;

    public DB2OptionalWriter(final D dialect, final OutputStream outputStream,
                             final XBaseMetadata metadata,
                             final XBaseFieldDescriptorArray<A> array) {
        this.dialect = dialect;
        this.outputStream = outputStream;
        this.metadata = metadata;
        this.array = array;
    }

    @Override
    public void write(final XBaseOptional optional) throws IOException {
        // write nothing.
    }
}
