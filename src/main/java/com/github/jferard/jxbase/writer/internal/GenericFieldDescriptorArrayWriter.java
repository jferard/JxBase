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
import com.github.jferard.jxbase.core.field.XBaseField;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.JdbfUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

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
            field.write(this, offset);
            offset += field.getByteLength(this.dialect);
        }
        this.out.write(JdbfUtils.HEADER_TERMINATOR);
        return offset;
    }

    @Override
    public void writeCharacterField(final String name, final int length, final int offset)
            throws IOException {
        this.writeField(name, 'C', this.dialect.getCharacterFieldLength(length), 0, offset);
    }

    @Override
    public void writeDateField(final String name, final int offset) throws IOException {
        this.writeField(name, 'D', this.dialect.getLogicalFieldLength(), 0, offset);
    }

    @Override
    public void writeDatetimeField(final String name, final int offset) throws IOException {

    }

    @Override
    public void writeIntegerField(final String name, final int length, final int offset)
            throws IOException {
        this.writeField(name, 'I', this.dialect.getIntegerFieldLength(length), 0, offset);
    }

    @Override
    public void writeLogicalField(final String name, final int offset) throws IOException {
        this.writeField(name, 'L', this.dialect.getLogicalFieldLength(), 0, offset);
    }

    @Override
    public void writeMemoField(final String name, final int offset) throws IOException {
        this.writeField(name, 'M', this.dialect.getMemoFieldLength(), 0, offset);
    }

    public void writeNumericField(final String name, final int length,
                                  final int numberOfDecimalPlaces, final int offset)
            throws IOException {
        this.writeField(name, 'N', this.dialect.getNumericFieldLength(length),
                numberOfDecimalPlaces, offset);
    }

    private void writeField(final String name, final int type, final int length,
                            final int numberOfDecimalPlaces, final int offset) throws IOException {
        final byte[] nameBytes = name.getBytes(JdbfUtils.ASCII_CHARSET);
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
        BitUtils.writeZeroes(this.out, JdbfUtils.FIELD_DESCRIPTOR_SIZE - 18);
    }
}
