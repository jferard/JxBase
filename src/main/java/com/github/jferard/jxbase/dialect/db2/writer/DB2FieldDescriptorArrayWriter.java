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

package com.github.jferard.jxbase.dialect.db2.writer;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db2.DB2Utils;
import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.internal.XBaseFieldDescriptorArrayWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

public class DB2FieldDescriptorArrayWriter<A>
        implements XBaseFieldDescriptorArrayWriter<A> {
    final A access;
    final OutputStream out;

    public DB2FieldDescriptorArrayWriter(final A access, final OutputStream out) {
        this.access = access;
        this.out = out;
    }

    @Override
    public int write(final XBaseFieldDescriptorArray<A> array) throws IOException {
        int offset = 0;
        final Collection<XBaseField<? super A>> fields = array.getFields();
        final int fieldCount = Math.min(DB2Utils.DB2_MAX_FIELDS, fields.size());
        final Iterator<XBaseField<? super A>> iterator = fields.iterator();
        for (int i = 0; i < fieldCount; i++) {
            assert iterator.hasNext();
            final XBaseField<? super A> field = iterator.next();
            this.writeField(field.toRepresentation(this.access), offset);
            offset += field.getValueByteLength(this.access);
        }
        assert !iterator.hasNext();
        final int missingCount = DB2Utils.DB2_MAX_FIELDS - fieldCount;
        this.out.write(JxBaseUtils.HEADER_TERMINATOR);
        if (missingCount > 0) {
            BitUtils.writeZeroes(this.out, missingCount * DB2Utils.DB2_FIELD_DESCRIPTOR_LENGTH);
        }
        return offset;
    }

    private void writeField(final FieldRepresentation representation, final int offset)
            throws IOException {
        final byte[] nameBytes = representation.getName().getBytes(JxBaseUtils.ASCII_CHARSET);
        final int nameLength = nameBytes.length;
        if (nameLength > 11) {
            throw new IOException("Name too long");
        }
        this.out.write(nameBytes);
        BitUtils.writeZeroes(this.out, 11 - nameLength);
        this.out.write(representation.getType()); // 11
        BitUtils.writeEmpties(this.out, 2); // 12-13
        this.out.write(representation.getRepLength() & 0xFF); // 14
        this.out.write(representation.getNumberOfDecimalPlaces()); // 15
    }
}