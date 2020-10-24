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

package com.github.jferard.jxbase.dialect.db3.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.BytesUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Access (read/write) for DB3 memos.
 */
public class DB3MemoAccess implements MemoAccess {
    private final XBaseMemoReader memoReader;
    private final XBaseMemoWriter memoWriter;
    private final RawRecordReadHelper rawRecordReader;

    public DB3MemoAccess(final XBaseMemoReader memoReader, final XBaseMemoWriter memoWriter,
                         final RawRecordReadHelper rawRecordReader) {
        this.memoReader = memoReader;
        this.memoWriter = memoWriter;
        this.rawRecordReader = rawRecordReader;
    }

    /**
     * https://www.clicketyclick.dk/databases/xbase/format/data_types.html:
     * > Pointer to ASCII text field in memo file 10 digits representing a pointer to a DBT block
     * (default is blanks).
     * @return 10
     */
    @Override
    public int getMemoValueLength() {
        return 10;
    }

    @Override
    public XBaseMemoRecord extractMemoValue(final byte[] recordBuffer, final int offset,
                                            final int length) throws IOException {
        final long offsetInBlocks = this.getOffsetInBlocks(recordBuffer, offset, length);
        if (offsetInBlocks == 0) {
            return null;
        }
        return this.memoReader.read(offsetInBlocks);
    }

    /**
     * Get the block offset in the memo file.
     * @param recordBuffer the record buffer
     * @param offset the offset
     * @param length the length
     * @return the offset
     */
    protected long getOffsetInBlocks(final byte[] recordBuffer, final int offset, final int length) {
        final String s =
                this.rawRecordReader.extractTrimmedASCIIString(recordBuffer, offset, length);
        if (s == null) {
            return 0;
        }
        return Long.parseLong(s);
    }

    @Override
    public void writeMemoValue(final OutputStream out, final XBaseMemoRecord value)
            throws IOException {
        final int length = this.getMemoValueLength();
        if (value == null) {
            BytesUtils.writeEmpties(out, length);
        } else {
            final long offsetInBlocks = this.memoWriter.write(value);
            final String s = String.format("%10d", offsetInBlocks);
            out.write(s.getBytes(JxBaseUtils.ASCII_CHARSET));
        }
    }

    @Override
    public FieldRepresentation getMemoFieldRepresentation(final String fieldName) {
        return new FieldRepresentation(fieldName, 'M', 10, 0);
    }
}
