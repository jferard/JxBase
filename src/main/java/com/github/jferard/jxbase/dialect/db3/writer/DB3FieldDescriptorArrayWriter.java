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

package com.github.jferard.jxbase.dialect.db3.writer;

import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db3.DB3Utils;
import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.BytesUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.XBaseFieldDescriptorArrayWriter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A writer for DB3 field descriptor array
 * @param <A> the access
 */
public class DB3FieldDescriptorArrayWriter<A extends XBaseAccess> implements XBaseFieldDescriptorArrayWriter<A> {
    final A access;
    final OutputStream out;

    public DB3FieldDescriptorArrayWriter(final A access, final OutputStream out) {
        this.access = access;
        this.out = out;
    }

    @Override
    public int write(final XBaseFieldDescriptorArray<A> array) throws IOException {
        int offset = 0;
        for (final XBaseField<? super A> field : array.getFields()) {
            this.writeField(field.toRepresentation(this.access));
            offset += field.getValueLength(this.access);
        }
        this.out.write(JxBaseUtils.HEADER_TERMINATOR);
        return offset;
    }

    private void writeField(final FieldRepresentation fieldRepresentation) throws IOException {
        final byte[] nameBytes = fieldRepresentation.getName().getBytes(JxBaseUtils.ASCII_CHARSET);
        final int nameLength = nameBytes.length;
        if (nameLength > JxBaseUtils.MAX_FIELD_NAME_SIZE) {
            throw new IOException("Name too long");
        }
        this.out.write(nameBytes);
        BytesUtils.writeZeroes(this.out, JxBaseUtils.MAX_FIELD_NAME_SIZE - nameLength);
        this.out.write(fieldRepresentation.getType()); // 11
        // 12-15: RAM address
        BytesUtils.writeZeroes(this.out, 4);
        this.out.write(fieldRepresentation.getFieldLength() & 0xFF); // 16
        this.out.write(fieldRepresentation.getNumberOfDecimalPlaces()); // 17
        // 18-19: Reserved for db3+ on a LAN
        // 20: Work area ID
        // 21-22: Reserved for db3+ on a LAN
        // 23: SET FIELDS flags
        // 24-31: Reserved
        BytesUtils.writeZeroes(this.out, DB3Utils.DB3_FIELD_DESCRIPTOR_SIZE - 18);
    }
}
