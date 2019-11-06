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

public class IntegerField implements XBaseField {
    private final String name;
    private final int length;

    public IntegerField(final String name, final int length) {
        this.name = name;
        this.length = length;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getByteLength(final XBaseLengths dialect) {
        return dialect.getIntegerFieldLength(this.length);
    }

    @Override
    public void write(final XBaseFieldDescriptorArrayWriter writer, final int offset)
            throws IOException {
        writer.writeIntegerField(this.name, this.length, offset);
    }

    @Override
    public Long getValue(final XBaseRecordReader reader, final byte[] recordBuffer,
                         final int offset, final int length) throws IOException {
        return reader.getIntegerValue(recordBuffer, offset, length);
    }

    @Override
    public void writeValue(final XBaseRecordWriter writer, final Object value) throws IOException {
        writer.writeIntegerValue((Long) value, this.length);
    }

    @Override
    public String toStringRepresentation(final XBaseRepresentations dialect) {
        return dialect.integerFieldToStringRepresentation(this.name, this.length);
    }

    @Override
    public String toString() {
        return "IntegerField[name=" + this.name + ", length=" + this.length + "]";
    }
}
