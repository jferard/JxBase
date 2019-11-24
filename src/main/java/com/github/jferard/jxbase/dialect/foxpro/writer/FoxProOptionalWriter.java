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
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.writer.internal.XBaseOptionalWriter;

import java.io.IOException;
import java.io.OutputStream;

public class FoxProOptionalWriter<D extends XBaseDialect<D, A>, A>
        implements XBaseOptionalWriter<FoxProDialect> {
    private final OutputStream outputStream;

    public FoxProOptionalWriter(final D dialect, final OutputStream outputStream,
                                final XBaseMetadata metadata,
                                final XBaseFieldDescriptorArray<D, A> array) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(final XBaseOptional optional) throws IOException {
        this.outputStream.write(optional.getBytes());
    }
}
