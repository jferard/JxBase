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

package com.github.jferard.jxbase.reader.internal;

import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.IOUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

public class GenericFieldDescriptorArrayReader implements XBaseFieldDescriptorArrayReader {
    private final InputStream inputStream;
    private final int fieldDescriptorLength;
    private final XBaseDialect dialect;

    public GenericFieldDescriptorArrayReader(final XBaseDialect dialect,
                                             final InputStream inputStream,
                                             final XBaseMetadata metadata) {
        this.dialect = dialect;
        this.inputStream = inputStream;
        this.fieldDescriptorLength = dialect.getFieldDescriptorLength();
    }

    @Override
    public XBaseFieldDescriptorArray read() throws IOException {
        final Collection<XBaseField> fields = new ArrayList<XBaseField>();
        final byte[] fieldBytes = new byte[this.fieldDescriptorLength];
        int arrayLength = 0;
        int recordLength = 1; // deletion flag
        while (true) {
            if (IOUtils.readFully(this.inputStream, fieldBytes) != this.fieldDescriptorLength) {
                throw new IOException("The file is corrupted or is not a dbf file");
            }

            final XBaseField field = this.createDbfField(fieldBytes);
            fields.add(field);

            arrayLength += fieldBytes.length;
            recordLength += field.getValueByteLength(this.dialect);

            if (IOUtils.isEndOfFieldArray(this.inputStream, JxBaseUtils.HEADER_TERMINATOR)) {
                arrayLength += 1;
                break;
            }
        }
        return new GenericFieldDescriptorArray(fields, arrayLength, recordLength);
    }

    private XBaseField createDbfField(final byte[] fieldBytes) {
        final String name = this.getName(fieldBytes);
        final byte typeByte = fieldBytes[11];
        // read offset
        final int length = this.getLength(fieldBytes[16]);
        final byte numberOfDecimalPlaces = fieldBytes[17];

        return this.dialect.getXBaseField(name, typeByte, length, numberOfDecimalPlaces);
    }

    private String getName(final byte[] fieldBytes) {
        int nameLength = 0;
        while (nameLength < 11 && fieldBytes[nameLength] != 0x0) {
            nameLength++;
        }
        return new String(fieldBytes, 0, nameLength, JxBaseUtils.ASCII_CHARSET);
    }

    private int getLength(final byte lenByte) {
        int length = lenByte;
        if (length < 0) {
            length += 256;
        }
        return length;
    }
}
