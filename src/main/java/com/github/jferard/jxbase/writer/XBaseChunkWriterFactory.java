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

package com.github.jferard.jxbase.writer;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;

import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

/**
 * A factory to create writer for various chunks.
 * @param <D> the dialect
 * @param <A> the accesss
 */
public interface XBaseChunkWriterFactory<D extends XBaseDialect<D, A>, A> {
    /**
     * Create a writer for meta data.
     * @param file              we need the file as random access file to fix metadata
     * @param outputStream      the output stream
     * @param charset           the charset
     * @return                  the meta data writer
     */
    XBaseMetadataWriter<D, A> createMetadataWriter(RandomAccessFile file, OutputStream outputStream,
                                                   Charset charset);

    /**
     * Create a writer for the descriptor array.
     * @param outputStream      the output stream
     * @param metadata          the written meta data
     * @return                  the writer
     */
    XBaseFieldDescriptorArrayWriter<A> createFieldDescriptorArrayWriter(OutputStream outputStream,
                                                                        XBaseMetadata metadata);

    /**
     * Create a writer for the optional chunk.
     * @param outputStream      the output stream
     * @param metadata          the written meta data
     * @param array             the written array
     * @return                  the writer
     */
    XBaseOptionalWriter<D> createOptionalWriter(OutputStream outputStream, XBaseMetadata metadata,
                                                XBaseFieldDescriptorArray<A> array);

    /**
     * Create a writer for the records.
     * @param outputStream      the output stream
     * @param metadata          the written meta data
     * @param array             the written array
     * @param optional          the written optional
     * @return                  the writer
     */
    XBaseRecordWriter<D> createRecordWriter(OutputStream outputStream, Charset charset,
                                            XBaseMetadata metadata,
                                            XBaseFieldDescriptorArray<A> array, Object optional);
}
