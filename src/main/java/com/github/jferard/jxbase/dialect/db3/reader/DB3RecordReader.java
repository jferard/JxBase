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

package com.github.jferard.jxbase.dialect.db3.reader;

import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseRecord;
import com.github.jferard.jxbase.dialect.db2.DB2Utils;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
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
import java.util.TimeZone;

/**
 * A reader for DB3 records.
 *
 * @param <A> the access
 */
public class DB3RecordReader<A extends XBaseAccess> implements XBaseRecordReader {
    protected final InputStream dbfInputStream;
    private final XBaseMemoReader memoReader;
    protected final Charset charset;
    protected final byte[] recordBuffer;
    protected final int recordLength;
    protected final Collection<XBaseField<? super A>> fields;
    protected final A access;
    protected final TimeZone timezone;
    protected int recordsCounter;

    public DB3RecordReader(final A access, final InputStream dbfInputStream,
                           final XBaseMemoReader memoReader,
                           final Charset charset,
                           final XBaseFieldDescriptorArray<A> array, final TimeZone timezone) {
        this.dbfInputStream = dbfInputStream;
        this.memoReader = memoReader;
        this.charset = charset;
        this.recordLength = array.getRecordLength();
        this.recordBuffer = new byte[this.recordLength];
        this.fields = array.getFields();
        this.access = access;
        this.timezone = timezone;
        this.recordsCounter = -1;
    }

    @Override
    public XBaseRecord read() throws IOException {
        if (IOUtils.isEndOfRecords(this.dbfInputStream, JxBaseUtils.RECORDS_TERMINATOR)) {
            return null;
        }
        Arrays.fill(this.recordBuffer, JxBaseUtils.NULL_BYTE);
        final int readLength = IOUtils.readFully(this.dbfInputStream, this.recordBuffer);

        if (readLength < this.recordLength) {
            throw new IOException("Bad record: " + readLength + " bytes available vs " + this.recordBuffer.length + " needed");
        }
        this.recordsCounter++;

        // Check if record is deleted.
        // According to documentation at
        // http://www.dbase.com/Knowledgebase/INT/db7_file_fmt.htm :
        // Data records are preceded by one byte, that is, a space (0x20) if the record is not
        // deleted, an asterisk (0x2A) if the record is deleted.
        // So, if record is preceded by 0x2A - it is considered to be deleted
        // All other cases: record is considered to be not deleted
        final boolean isDeleted = this.recordBuffer[0] == DB2Utils.DB2_DELETED_RECORD_HEADER;

        final Map<String, Object> valueByFieldName = new HashMap<String, Object>();
        int offset = 1;
        for (final XBaseField<? super A> field : this.fields) {
            final String name= field.getName();
            final Object value;
            if (field instanceof MemoField) {
                final MemoAccess memoAccess = (MemoAccess) this.access;
                value = memoAccess.extractMemoValue(this.memoReader, this.recordBuffer, offset,
                        memoAccess.getMemoValueLength());
            } else {
                value = field.extractValue(this.access, this.recordBuffer, offset,
                        field.getValueLength(this.access));
            }
            valueByFieldName.put(name, value);
            offset += field.getValueLength(this.access);
        }
        return new XBaseRecord(isDeleted, this.recordsCounter + 1, valueByFieldName);
    }


    @Override
    public void close() throws IOException {
        this.dbfInputStream.close();
        if (this.memoReader != null) {
            this.memoReader.close();
        }
    }
}
