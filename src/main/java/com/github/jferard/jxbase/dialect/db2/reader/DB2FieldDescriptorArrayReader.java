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

package com.github.jferard.jxbase.dialect.db2.reader;

import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db2.DB2Utils;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.reader.XBaseFieldDescriptorArrayReader;
import com.github.jferard.jxbase.util.IOUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A reader for DB2 array descriptor.
 *
 * @param <D> the dialect
 * @param <A> the access
 */
public class DB2FieldDescriptorArrayReader<A extends XBaseAccess, D extends XBaseDialect<A, D>>
        implements XBaseFieldDescriptorArrayReader<A, D> {
    private final InputStream inputStream;
    private final int fieldDescriptorLength;
    private final D dialect;

    public DB2FieldDescriptorArrayReader(final D dialect, final InputStream inputStream) {
        this.dialect = dialect;
        this.inputStream = inputStream;
        this.fieldDescriptorLength = dialect.getFieldDescriptorLength();
    }

    @Override
    public XBaseFieldDescriptorArray<A> read() throws IOException {
        // DB2 has a max of 32 fields
        final Collection<XBaseField<? super A>> fields =
                new ArrayList<XBaseField<? super A>>(DB2Utils.DB2_MAX_FIELDS);
        final byte[] fieldBytes = new byte[this.fieldDescriptorLength];
        int recordLength = 1; // deletion flag
        int i;
        for (i = 0; i < DB2Utils.DB2_MAX_FIELDS; i++) {
            JxBaseUtils.readFieldBytes(this.inputStream, fieldBytes);
            final XBaseField<? super A> field = this.createDbfField(fieldBytes);
            fields.add(field);
            recordLength += field.getValueLength(this.dialect.getAccess());

            if (IOUtils.isEndOfFieldArray(this.inputStream, JxBaseUtils.HEADER_TERMINATOR)) {
                break;
            }
        }
        if (i == DB2Utils.DB2_MAX_FIELDS) { // not terminated
            throw new IOException("The file is corrupted or is not a DB2 file");
        } else if (i == DB2Utils.DB2_MAX_FIELDS - 1) { // terminated
            /* do nothing */
        } else {
            // pos = (i+1)*16 + 1
            for (int j = i+1; j < DB2Utils.DB2_MAX_FIELDS; j++) {
                JxBaseUtils.readFieldBytes(this.inputStream, fieldBytes);
                if (!Arrays.equals(DB2Utils.DB2_BLANK_FIELD, fieldBytes)) {
                    throw new IOException("The file is corrupted or is not a DB2 file");
                }
            }
            // pos = 32*16 + 1
            // pos = 32*16 + 1
        }
        return new GenericFieldDescriptorArray<A>(fields,
                DB2Utils.DB2_MAX_FIELDS * DB2Utils.DB2_FIELD_DESCRIPTOR_LENGTH + 1, recordLength);
    }

    private XBaseField<? super A> createDbfField(final byte[] fieldBytes) {
        final String name = JxBaseUtils.getName(fieldBytes);
        final byte typeByte = fieldBytes[11];
        final int length = JxBaseUtils.getLength(fieldBytes[12]);
        // 13-14: RAM address
        final byte numberOfDecimalPlaces = fieldBytes[15];
        return this.dialect.createXBaseField(name, typeByte, length, numberOfDecimalPlaces);
    }

}
