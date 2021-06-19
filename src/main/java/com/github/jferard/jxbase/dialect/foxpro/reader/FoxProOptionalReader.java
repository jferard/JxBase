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

package com.github.jferard.jxbase.dialect.foxpro.reader;

import com.github.jferard.jxbase.core.GenericOptional;
import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.reader.XBaseOptionalReader;
import com.github.jferard.jxbase.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * A fox pro optional chunk reader (263 bytes)
 * @param <D> the dialect
 * @param <A> the access
 */
public class FoxProOptionalReader<A extends XBaseAccess, D extends XBaseDialect<A, D>> implements XBaseOptionalReader {
    public static final int DATABASE_CONTAINER_LENGTH = 263;
    private final InputStream inputStream;

    public FoxProOptionalReader(final D dialect, final InputStream inputStream,
                                final XBaseMetadata metadata,
                                final XBaseFieldDescriptorArray<A> array) {
        this.inputStream = inputStream;
    }

    @Override
    public XBaseOptional read() throws IOException {
        final byte[] bytes = new byte[DATABASE_CONTAINER_LENGTH];
        if (IOUtils.readFully(this.inputStream, bytes) != DATABASE_CONTAINER_LENGTH) {
            throw new IOException(
                    "File header should have a database container of " + DATABASE_CONTAINER_LENGTH +
                            " bytes");
        }
        return new GenericOptional(bytes);
    }
}
