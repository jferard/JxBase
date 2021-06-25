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

package com.github.jferard.jxbase.reader;

import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.memo.XBaseMemoReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * A factory for various chunk readers. Readers are ordered by their parameters.
 *
 * @param <D> dialect
 * @param <A> access
 */
public interface XBaseChunkReaderFactory<A extends XBaseAccess, D extends XBaseDialect<A, D>> {
    /**
     * @param inputStream the input stream
     * @return the meta data reader
     */
    XBaseMetadataReader createMetadataReader(InputStream inputStream);

    /**
     * @param inputStream the input stream
     * @param metadata the read meta data
     * @return the field descriptor array reader
     */
    XBaseFieldDescriptorArrayReader<A, D> createFieldDescriptorArrayReader(InputStream inputStream,
                                                                           XBaseMetadata metadata);

    /**
     * @param inputStream the input stream
     * @param charset the charset
     * @param metadata the read meta data
     * @param array the read array
     * @return the optional reader
     */
    XBaseOptionalReader createOptionalReader(InputStream inputStream, Charset charset,
                                             XBaseMetadata metadata,
                                             XBaseFieldDescriptorArray<A> array);

    /**
     * @param inputStream the input stream
     * @param charset the charset
     * @param memoReader
     * @param metadata the read meta data
     * @param array the read array
     * @param optional the read optional
     * @return the record reader
     */
    XBaseRecordReader createRecordReader(InputStream inputStream, Charset charset,
                                         XBaseMemoReader memoReader,
                                         XBaseMetadata metadata, XBaseFieldDescriptorArray<A> array,
                                         Object optional);

    XBaseMemoReader createMemoReader(final XBaseFileTypeEnum type, final String tableName,
                                     Charset charset)
            throws IOException;
}
