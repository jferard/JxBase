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
import com.github.jferard.jxbase.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.OutputStream;

public class GenericFieldDescriptorArrayWriter implements XBaseFieldDescriptorArrayWriter {
    final XBaseDialect dialect;
    final OutputStream out;

    public GenericFieldDescriptorArrayWriter(final XBaseDialect dialect, final OutputStream out) {
        this.dialect = dialect;
        this.out = out;
    }

    @Override
    public int write(final XBaseFieldDescriptorArray array) throws IOException {
        int offset = 0;
        for (final XBaseField field : array.getFields()) {
            this.writeField(field.toRepresentation(this.dialect), offset);
            offset += field.getValueByteLength(this.dialect);
        }
        this.out.write(JxBaseUtils.HEADER_TERMINATOR);
        return offset;
    }

    protected void writeField(final String name, final int type, final int length,
                              final int numberOfDecimalPlaces, final int offset)
            throws IOException {
        final byte[] nameBytes = name.getBytes(JxBaseUtils.ASCII_CHARSET);
        final int nameLength = nameBytes.length;
        if (nameLength > 11) {
            throw new IOException("Name too long");
        }
        this.out.write(nameBytes);
        BitUtils.writeZeroes(this.out, 11 - nameLength);
        this.out.write(type); // 11
        BitUtils.writeLEByte4(this.out, offset); // 12-15
        this.out.write(length & 0xFF); // 16
        this.out.write(numberOfDecimalPlaces); // 17
        BitUtils.writeZeroes(this.out, JxBaseUtils.FIELD_DESCRIPTOR_SIZE - 18);
    }

    protected void writeField(final FieldRepresentation dialectField, final int offset)
            throws IOException {
        dialectField.write(this.out, offset);
    }
}
