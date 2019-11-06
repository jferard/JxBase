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

package com.github.jferard.jxbase.writer.internal;

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.writer.XBaseMemoWriter;

import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public interface XBaseInternalWriterFactory {
    XBaseMetadataWriter createMetadataWriter(final XBaseDialect dialect, RandomAccessFile file,
                                             OutputStream outputStream, Charset charset);

    XBaseFieldDescriptorArrayWriter createFieldDescriptorArrayWriter(final XBaseDialect dialect,
                                                                     OutputStream outputStream,
                                                                     XBaseMetadata metadata);

    XBaseOptionalWriter createOptionalWriter(final XBaseDialect dialect, OutputStream outputStream,
                                             XBaseMetadata metadata,
                                             XBaseFieldDescriptorArray array);

    XBaseRecordWriter createRecordWriter(final XBaseDialect dialect, OutputStream outputStream,
                                         Charset charset, XBaseMetadata metadata,
                                         XBaseFieldDescriptorArray array, Object optional,
                                         XBaseMemoWriter memoWriter);
}