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
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseOptional;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A generic writer for the optional chunk after the descriptor array.
 * Do not use the metadata or the array.
 * @param <D> the dialect
 * @param <A> the access
 */
public class GenericOptionalWriter<A extends XBaseAccess, D extends XBaseDialect<A, D>> implements XBaseOptionalWriter<D> {
    private final D dialect;
    private final OutputStream outputStream;
    private final XBaseMetadata metadata;
    private final XBaseFieldDescriptorArray<A> array;

    public GenericOptionalWriter(final D dialect, final OutputStream outputStream,
                                 final XBaseMetadata metadata,
                                 final XBaseFieldDescriptorArray<A> array) {
        this.dialect = dialect;
        this.outputStream = outputStream;
        this.metadata = metadata;
        this.array = array;
    }

    @Override
    public void write(final XBaseOptional optional) throws IOException {
        final int length = this.dialect.getOptionalLength();
        if (length > 0) {
            final byte[] bytes = new byte[length];
            this.outputStream.write(bytes);
        }
    }
}
