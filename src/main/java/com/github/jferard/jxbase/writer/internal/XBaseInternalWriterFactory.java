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

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;

import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public interface XBaseInternalWriterFactory<D extends XBaseDialect<D, A>, A> {
    XBaseMetadataWriter<D, A> createMetadataWriter(RandomAccessFile file, OutputStream outputStream,
                                                   Charset charset);

    XBaseFieldDescriptorArrayWriter<D, A> createFieldDescriptorArrayWriter(OutputStream outputStream,
                                                                        XBaseMetadata metadata);

    XBaseOptionalWriter<D> createOptionalWriter(OutputStream outputStream, XBaseMetadata metadata,
                                                XBaseFieldDescriptorArray<D, A> array);

    XBaseRecordWriter<D> createRecordWriter(OutputStream outputStream, Charset charset,
                                            XBaseMetadata metadata,
                                            XBaseFieldDescriptorArray<D, A> array, Object optional);
}
