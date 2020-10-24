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
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A logical access in DB2.
 */
public class DB2LogicalAccess implements LogicalAccess {
    private final RawRecordReadHelper rawRecordReader;
    private final RawRecordWriteHelper rawRecordWriter;

    public DB2LogicalAccess(final RawRecordReadHelper rawRecordReader,
                            final RawRecordWriteHelper rawRecordWriter) {
        this.rawRecordReader = rawRecordReader;
        this.rawRecordWriter = rawRecordWriter;
    }

    @Override
    public int getLogicalValueLength() {
        return 1;
    }

    @Override
    public Boolean extractLogicalValue(final byte[] recordBuffer, final int offset,
                                       final int length) {
        final String s =
                this.rawRecordReader.extractTrimmedASCIIString(recordBuffer, offset, length);
        if (s == null) {
            return null;
        }
        if (s.equalsIgnoreCase("t")) {
            return Boolean.TRUE;
        } else if (s.equalsIgnoreCase("f")) {
            return Boolean.FALSE;
        } else {
            return null;
        }
    }

    @Override
    public void writeLogicalValue(final OutputStream out, final Boolean value) throws IOException {
        final byte[] bytes;
        if (value == null) {
            bytes = "?".getBytes(JxBaseUtils.ASCII_CHARSET);
        } else {
            final String s = value ? "T" : "F";
            bytes = s.getBytes();
        }
        this.rawRecordWriter.write(out, bytes);
    }

    @Override
    public FieldRepresentation getLogicalFieldRepresentation(final String fieldName) {
        return new FieldRepresentation(fieldName, 'L', 1, 0);
    }
}
