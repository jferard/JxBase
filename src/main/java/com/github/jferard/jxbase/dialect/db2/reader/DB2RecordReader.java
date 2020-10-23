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

package com.github.jferard.jxbase.dialect.db2.reader;

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseRecord;
import com.github.jferard.jxbase.dialect.db2.DB2Utils;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.reader.XBaseRecordReader;
import com.github.jferard.jxbase.util.IOUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DB2RecordReader<A> implements XBaseRecordReader {
    protected final InputStream dbfInputStream;
    protected final Charset charset;
    protected final byte[] recordBuffer;
    protected final int recordLength;
    protected final Collection<XBaseField<? super A>> fields;
    protected final A access;
    protected int recordsCounter;

    public DB2RecordReader(final A access, final InputStream dbfInputStream, final Charset charset,
                           final XBaseFieldDescriptorArray<A> array) {
        this.dbfInputStream = dbfInputStream;
        this.charset = charset;
        this.recordLength = array.getRecordLength();
        this.recordBuffer = new byte[this.recordLength];
        this.fields = array.getFields();
        this.access = access;
        this.recordsCounter = -1;
    }

    public XBaseRecord read() throws IOException {
        if (IOUtils.isEndOfRecords(this.dbfInputStream, JxBaseUtils.RECORDS_TERMINATOR)) {
            return null;
        }
        Arrays.fill(this.recordBuffer, JxBaseUtils.NULL_BYTE);
        final int readLength = IOUtils.readFully(this.dbfInputStream, this.recordBuffer);

        if (readLength < this.recordLength) {
            throw new IOException("Bad record length: " + readLength + " -> " + this.recordLength);
        }
        this.recordsCounter++;

        final boolean isDeleted = this.recordBuffer[0] == DB2Utils.DB2_DELETED_RECORD_HEADER;

        final Map<String, Object> valueByFieldName = new HashMap<String, Object>();
        int offset = 1;
        for (final XBaseField<? super A> field : this.fields) {
            final Object value = field.getValue(this.access, this.recordBuffer, offset,
                    field.getValueByteLength(this.access));
            final String name = field.getName();
            valueByFieldName.put(name, value);
            offset += field.getValueByteLength(this.access);
        }
        return new XBaseRecord(isDeleted, this.recordsCounter + 1, valueByFieldName);
    }

    @Override
    public void close() throws IOException {
        this.dbfInputStream.close();
    }
}
