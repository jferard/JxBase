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

import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseMetadata;

import java.io.Closeable;
import java.io.IOException;

/**
 * A writer for the meta data (first chunk of the file).
 *
 * @param <D> the dialect
 * @param <A> the access
 */
public interface XBaseMetadataWriter<A extends XBaseAccess, D extends XBaseDialect<A, D>>
        extends Closeable {
    /**
     * Write the meta data.
     *
     * @param metadata the meta data
     * @throws IOException
     */
    void write(XBaseMetadata metadata) throws IOException;

    /**
     * We know the record number *after* writing the records. Hence we need
     * to fix the meta data before closing the file
     *
     * @param recordQty
     * @throws IOException
     */
    void fixMetadata(int recordQty) throws IOException;
}
