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

package com.github.jferard.jxbase.reader.internal;

import com.github.jferard.jxbase.GenericOptional;
import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class GenericOptionalReader implements XBaseOptionalReader {
    private final XBaseDialect dialect;
    private final InputStream inputStream;
    private final XBaseMetadata metadata;
    private final XBaseFieldDescriptorArray array;

    public GenericOptionalReader(final XBaseDialect dialect, final InputStream inputStream,
                                 final XBaseMetadata metadata,
                                 final XBaseFieldDescriptorArray array) {
        this.dialect = dialect;
        this.inputStream = inputStream;
        this.metadata = metadata;
        this.array = array;
    }

    @Override
    public XBaseOptional read() throws IOException {
        final int metaLength = this.dialect.getMetaDataLength();
        final int read = this.array.getArrayLength() + metaLength;
        final int excess = this.metadata.getFullHeaderLength() - read;
        if (excess > 0) {
            Logger.getLogger(this.getClass().getName()).info(String
                    .format("Optional bytes in header: expected: %s, actual: %s + %s",
                            this.metadata.getFullHeaderLength(), this.array.getArrayLength(),
                            metaLength));

            final byte[] bytes = new byte[excess];
            IOUtils.readFully(this.inputStream, bytes);
            return new GenericOptional(bytes);
        } else {
            return GenericOptional.EMPTY;
        }
    }
}
