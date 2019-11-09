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

import com.github.jferard.jxbase.core.GenericRecord;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseMemoRecord;
import com.github.jferard.jxbase.core.field.XBaseField;
import com.github.jferard.jxbase.reader.XBaseMemoReader;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.IOUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GenericRecordReader implements XBaseRecordReader {
    private static final CharSequence NUMERIC_OVERFLOW = "*";
    private final InputStream dbfInputStream;
    private final Charset charset;
    private final byte[] recordBuffer;
    private final int recordLength;
    private final Collection<XBaseField> fields;
    private final XBaseDialect dialect;
    private int recordsCounter;
    private final XBaseMemoReader memoReader;

    public GenericRecordReader(final XBaseDialect dialect, final InputStream dbfInputStream, final Charset charset,
                               final XBaseFieldDescriptorArray array,
                               final XBaseMemoReader memoReader) {
        this.dbfInputStream = dbfInputStream;
        this.charset = charset;
        this.recordLength = array.getRecordLength();
        this.fields = array.getFields();
        this.memoReader = memoReader;
        this.dialect = dialect;
        this.recordBuffer = new byte[this.recordLength];
        this.recordsCounter = -1;
    }

    @Override
    public GenericRecord read() throws IOException, ParseException {
        if (IOUtils.isEndOfRecords(this.dbfInputStream, JxBaseUtils.RECORDS_TERMINATOR)) {
            return null;
        }
        Arrays.fill(this.recordBuffer, JxBaseUtils.NULL_BYTE);
        final int readLength = IOUtils.readFully(this.dbfInputStream, this.recordBuffer);

        if (readLength < this.recordLength) {
            throw new IOException("Bad record: " + readLength + " -> " + this.recordBuffer[0]);
        }
        this.recordsCounter++;

        // Check if record is deleted.
        // According to documentation at
        // http://www.dbase.com/Knowledgebase/INT/db7_file_fmt.htm :
        // Data records are preceded by one byte, that is, a space (0x20) if the record is not
        // deleted, an asterisk (0x2A) if the record is deleted.
        // So, if record is preceded by 0x2A - it is considered to be deleted
        // All other cases: record is considered to be not deleted
        final boolean isDeleted = this.recordBuffer[0] == 0x2A;

        final Map<String, Object> valueByFieldName = new HashMap<String, Object>();
        int offset = 1;
        for (final XBaseField field : this.fields) {
            final Object value = field.getValue(this, this.recordBuffer, offset,
                    field.getByteLength(this.dialect));
            final String name = field.getName();
            valueByFieldName.put(name, value);
            offset += field.getByteLength(this.dialect);
        }
        return new GenericRecord(isDeleted, this.recordsCounter + 1, valueByFieldName, null);
    }

    @Override
    public String getCharacterValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.getTrimmedString(recordBuffer, offset, length, this.charset);
    }

    @Override
    public String getTrimmedString(final byte[] recordBuffer, final int offset, final int length,
                                   final Charset charset) {
        int actualOffset = offset;
        int actualLength = length;

        // check for empty strings
        while (actualLength > 0 && (recordBuffer[actualOffset] == JxBaseUtils.EMPTY)) {
            actualOffset++;
            actualLength--;
        }

        while (actualLength > 0 &&
                (recordBuffer[actualOffset + actualLength - 1] == JxBaseUtils.EMPTY)) {
            actualLength--;
        }

        if (actualLength == 0) {
            return null;
        }

        return new String(recordBuffer, actualOffset, actualLength, charset);
    }

    @Override
    public String getTrimmedASCIIString(final byte[] recordBuffer, final int offset,
                                        final int length) {
        return this.getTrimmedString(recordBuffer, offset, length, JxBaseUtils.ASCII_CHARSET);
    }

    @Override
    public Date getDateValue(final byte[] recordBuffer, final int offset, final int length) {
        final String s = this.getTrimmedASCIIString(recordBuffer, offset, length);
        if (s == null) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyyMMdd").parse(s);
        } catch (final ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Date getDatetimeValue(final byte[] recordBuffer, final int offset, final int length) {
        // TODO
        // https://en.wikipedia.org/wiki/Julian_day#Julian_or_Gregorian_calendar_from_Julian_day_number
        return null;
    }

    @Override
    public Long getIntegerValue(final byte[] recordBuffer, final int offset, final int length) {
        assert length == 4;
        return Long.valueOf(BitUtils.makeInt(recordBuffer[offset], recordBuffer[offset + 1],
                recordBuffer[offset + 2], recordBuffer[offset + 3]));
    }

    @Override
    public Boolean getLogicalValue(final byte[] recordBuffer, final int offset, final int length) {
        final String s = this.getTrimmedASCIIString(recordBuffer, offset, length);
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
    public XBaseMemoRecord<?> getMemoValue(final byte[] recordBuffer, final int offset,
                                           final int length) throws IOException {
        final long offsetInBlocks =
                this.dialect.getOffsetInBlocks(this, recordBuffer, offset, length);
        return this.memoReader.read(offsetInBlocks);
    }

    @Override
    public BigDecimal getNumericValue(final byte[] recordBuffer, final int offset, final int length,
                                      final int numberOfDecimalPlaces) {
        final String s = this.getTrimmedASCIIString(recordBuffer, offset, length);
        final MathContext mc = new MathContext(numberOfDecimalPlaces);
        if (s == null || s.contains(GenericRecordReader.NUMERIC_OVERFLOW)) {
            return null;
        }
        return new BigDecimal(s, mc);
    }

    @Override
    public void close() throws IOException {
        this.dbfInputStream.close();
    }
}
