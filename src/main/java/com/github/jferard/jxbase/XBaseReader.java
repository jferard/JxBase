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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.core.XBaseRecord;

import java.io.Closeable;
import java.io.IOException;
import java.text.ParseException;

public interface XBaseReader<D extends XBaseDialect<D, A>, A> extends Closeable {
    /**
     * @return the next record, or null if the end of file was reached
     * @throws IOException if an I/O exception occurs
     */
    XBaseRecord read() throws IOException, ParseException;

    XBaseDialect<D, A> getDialect();

    XBaseMetadata getMetadata();

    XBaseFieldDescriptorArray<A> getFieldDescriptorArray();

    XBaseOptional getOptional();

    @Override
    void close() throws IOException;
}
