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

package com.github.jferard.jxbase.core.field;

import com.github.jferard.jxbase.core.XBaseLengths;
import com.github.jferard.jxbase.core.XBaseRepresentations;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.writer.internal.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.internal.XBaseRecordWriter;

import java.io.IOException;

public interface XBaseField {
    String getName();

    /**
     * @return the length of the field (the actual length, not necessarily the third field)
     */
    int getByteLength(XBaseLengths dialect);

    void write(XBaseFieldDescriptorArrayWriter writer, int offset) throws IOException;

    Object getValue(XBaseRecordReader reader, byte[] recordBuffer, int offset, int length)
            throws IOException;

    void writeValue(XBaseRecordWriter writer, Object value) throws IOException;

    String toStringRepresentation(XBaseRepresentations dialect);
}
