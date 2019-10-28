/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

package com.github.jferard.jxbase.core;

import com.github.jferard.jxbase.reader.MemoReader;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.JdbfUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Collection;
import java.util.IllegalFormatException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DbfRecord {

    private final int recordNumber;
    private byte[] bytes;
    private DbfMetadata metadata;
    private MemoReader memoReader;

    public DbfRecord(byte[] source, DbfMetadata metadata, MemoReader memoReader, int recordNumber) {
        this.recordNumber = recordNumber;
        this.bytes = new byte[source.length];
        System.arraycopy(source, 0, this.bytes, 0, source.length);
        this.metadata = metadata;
        this.memoReader = memoReader;
    }

    /**
     * Check if record is deleted.
     * According to documentation at
     * http://www.dbase.com/Knowledgebase/INT/db7_file_fmt.htm :
     * Data records are preceded by one byte, that is, a space (0x20) if the record is not
     * deleted, an asterisk (0x2A) if the record is deleted.
     * So, if record is preceded by 0x2A - it is considered to be deleted
     * All other cases: record is considered to be not deleted
     *
     * @return
     */
    public boolean isDeleted() {
        return this.bytes[0] == 0x2A;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public String getASCIIString(String fieldName) {
        return getString(fieldName, JdbfUtils.ASCII_CHARSET);
    }

    /**
     * Return the string value for this field. The string is either non empty or null.
     *
     * @param fieldName the field name
     * @param charset   the charset
     * @return the trimmed string or null if the trimmed string is empty.
     */
    public String getString(String fieldName, Charset charset) {
        OffsetDbfField<?> of = metadata.getOffsetField(fieldName);
        int actualOffset = of.getOffset();
        int actualLength = of.getLength();

        // check for empty strings
        while ((actualLength > 0) && (bytes[actualOffset] == JdbfUtils.EMPTY)) {
            actualOffset++;
            actualLength--;
        }

        while ((actualLength > 0) && (bytes[actualOffset + actualLength - 1] == JdbfUtils.EMPTY)) {
            actualLength--;
        }

        if (actualLength == 0) {
            return null;
        }

        return new String(bytes, actualOffset, actualLength, charset);
    }

    public byte[] getMemoAsBytes(String fieldName) throws IOException {
        int offsetInBlocks = this.getOffsetInBlocks(fieldName);
        if (offsetInBlocks == 0) {
            return new byte[] {};
        }
        return memoReader.read(offsetInBlocks).getValue();
    }

    public String getMemoAsString(String fieldName, Charset charset) throws IOException {
        int offsetInBlocks = this.getOffsetInBlocks(fieldName);
        if (offsetInBlocks == 0) {
            return "";
        }
        return memoReader.read(offsetInBlocks).getValueAsString(charset);
    }

    private int getOffsetInBlocks(String fieldName) throws IllegalFormatException {
        OffsetDbfField<?> of = metadata.getOffsetField(fieldName);
        if (of.getType() != DbfFieldTypeEnum.Memo) {
            throw new IllegalArgumentException("Field '" + fieldName + "' is not MEMO field!");
        }
        final int length = of.getLength();
        if (length == 10) {
            return getBigDecimal(fieldName).intValueExact();
        } else {
            // throw new IllegalArgumentException("A memo field has a length of 10");
            final int offset = of.getOffset();
            return BitUtils.makeInt(bytes[offset], bytes[offset + 1], bytes[offset + 2],
                    bytes[offset + 3]);
        }
    }

    private BigDecimal getBigDecimal(String fieldName) {
        String s = this.getASCIIString(fieldName);

        if (s == null || s.contains(NumericDbfField.NUMERIC_OVERFLOW)) {
            return null;
        }
        return new BigDecimal(s);
    }

    public void setBoolean(String fieldName, Boolean value) {
        OffsetDbfField<?> of = metadata.getOffsetField(fieldName);
        // TODO: write boolean
    }

    public byte[] getBytes(String fieldName) {
        OffsetDbfField<?> of = metadata.getOffsetField(fieldName);
        byte[] b = new byte[of.getLength()];
        System.arraycopy(bytes, of.getOffset(), b, 0, of.getLength());
        return b;
    }

    public void setBytes(String fieldName, byte[] fieldBytes) {
        OffsetDbfField<?> of = metadata.getOffsetField(fieldName);
        // TODO:
        // assert fieldBytes.length = f.getLength()
        System.arraycopy(fieldBytes, 0, bytes, of.getOffset(), of.getLength());
    }

    public Collection<DbfField<?>> getFields() {
        return metadata.getFields();
    }

    public String getStringRepresentation(final Charset charset) throws Exception {
        StringBuilder sb = new StringBuilder(bytes.length * 10);
        for (DbfField<?> f : getFields()) {
            sb.append(f.getName()).append("=").append(f.getValue(this, charset));
            sb.append(", ");
        }
        return sb.toString();
    }

    public Map<String, Object> toMap(final Charset charset) throws ParseException {
        Map<String, Object> map = new LinkedHashMap<String, Object>(getFields().size() * 2);

        for (DbfField<?> f : getFields()) {
            String name = f.getName();
            final Object value = f.getValue(this, charset);
            if (value != null) {
                map.put(name, value);
            }
        }

        return map;
    }
}
