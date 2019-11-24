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

package com.github.jferard.jxbase.dialect.db3.reader;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.reader.internal.XBaseFieldDescriptorArrayReader;
import com.github.jferard.jxbase.util.IOUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

public class DB3FieldDescriptorArrayReader<D extends XBaseDialect<D, A>, A>
        implements XBaseFieldDescriptorArrayReader<D, A> {
    private final InputStream inputStream;
    private final int fieldDescriptorLength;
    private final D dialect;

    public DB3FieldDescriptorArrayReader(final D dialect, final InputStream inputStream,
                                         final XBaseMetadata metadata) {
        this.dialect = dialect;
        this.inputStream = inputStream;
        this.fieldDescriptorLength = dialect.getFieldDescriptorLength();
    }

    @Override
    public XBaseFieldDescriptorArray<D, A> read() throws IOException {
        final Collection<XBaseField<? super A>> fields = new ArrayList<XBaseField<? super A>>();
        final byte[] fieldBytes = new byte[this.fieldDescriptorLength];
        int arrayLength = 0;
        int recordLength = 1; // deletion flag
        while (true) {
            JxBaseUtils.readFieldBytes(this.inputStream, fieldBytes);
            final XBaseField<? super A> field = this.createDbfField(fieldBytes);
            fields.add(field);

            arrayLength += fieldBytes.length;
            recordLength += field.getValueByteLength(this.dialect.getAccess());

            if (IOUtils.isEndOfFieldArray(this.inputStream, JxBaseUtils.HEADER_TERMINATOR)) {
                return new GenericFieldDescriptorArray<D, A>(fields, arrayLength + 1, recordLength);
            }
        }
    }

    private XBaseField<? super A> createDbfField(final byte[] fieldBytes) {
        final String name = JxBaseUtils.getName(fieldBytes);
        final byte typeByte = fieldBytes[11];
        // 12-15: RAM address
        final int length = JxBaseUtils.getLength(fieldBytes[16]);
        final byte numberOfDecimalPlaces = fieldBytes[17];
        // 18-19: Reserved for db3+ on a LAN
        // 20: Work area ID
        // 21-22: Reserved for db3+ on a LAN
        // 23: SET FIELDS flags
        // 24-31: Reserved
        return this.dialect.getXBaseField(name, typeByte, length, numberOfDecimalPlaces);
    }
}
