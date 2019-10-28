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

package com.github.jferard.jxbase.reader;

import com.github.jferard.jxbase.core.DbfMetadata;
import com.github.jferard.jxbase.core.XBaseRecord;
import com.github.jferard.jxbase.core.XBaseMemoRecord;

import java.io.Closeable;
import java.io.IOException;

public interface XBaseReader<T extends XBaseMemoRecord> extends Closeable {
    DbfMetadata getMetadata();

    /**
     * @return the next record, or null if the end of file was reached
     * @throws IOException if an I/O exception occurs
     */
    XBaseRecord<T> read() throws IOException;

    @Override
    void close() throws IOException;
}
