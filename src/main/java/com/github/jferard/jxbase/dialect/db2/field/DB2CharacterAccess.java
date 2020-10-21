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

package com.github.jferard.jxbase.dialect.db2.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;

import java.io.IOException;
import java.io.OutputStream;

public class DB2CharacterAccess implements CharacterAccess {
    final RawRecordReadHelper rawRecordReader;
    final RawRecordWriteHelper rawRecordWriter;

    public DB2CharacterAccess(final RawRecordReadHelper rawRecordReader,
                              final RawRecordWriteHelper rawRecordWriter) {
        this.rawRecordReader = rawRecordReader;
        this.rawRecordWriter = rawRecordWriter;
    }

    @Override
    public int getCharacterValueLength(final int dataSize) {
        return dataSize;
    }

    @Override
    public String extractCharacterValue(final byte[] recordBuffer, final int offset,
                                        final int length) {
        return this.rawRecordReader.extractTrimmedString(recordBuffer, offset, length);
    }

    @Override
    public void writeCharacterValue(final OutputStream out, final String value, final int length)
            throws IOException {
        if (value == null) {
            this.rawRecordWriter.writeEmpties(out, length);
        } else {
            this.rawRecordWriter.write(out, value, length);
        }
    }

    @Override
    public FieldRepresentation getCharacterFieldRepresentation(final String name,
                                                               final int dataSize) {
        if (dataSize >= 254) {
            throw new IllegalArgumentException(
                    String.format("Use FoxPro for long character fields: %s,C,%d,%d", name,
                            dataSize % 256, dataSize / 256));
        }
        return new FieldRepresentation(name, 'C', dataSize, 0);
    }
}
