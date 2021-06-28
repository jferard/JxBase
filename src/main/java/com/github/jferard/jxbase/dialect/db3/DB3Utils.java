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

package com.github.jferard.jxbase.dialect.db3;

import com.github.jferard.jxbase.memo.MemoFileHeader;
import com.github.jferard.jxbase.util.BytesUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Collections;

public class DB3Utils {
    public static final int DB3_FIELD_DESCRIPTOR_LENGTH = 32;
    public static final int DB3_FIELD_DESCRIPTOR_SIZE = 32;
    public static final int MEMO_HEADER_LENGTH = 512;
    public static final int BLOCK_LENGTH = 512;

    public static final String META_RECORDS_QTY = "recordsQty";
    public static final String META_UPDATE_DATE = "updateDate";

    /**
     * Write the number of records
     * @param out the output
     * @param r the object
     * @throws IOException
     */
    public static void writeRecordQty4(final OutputStream out, final Object r) throws IOException {
        if (r == null) {
            BytesUtils.writeZeroes(out, 4);
        } else if (r instanceof Number) {
            final int recordsQty = ((Number) r).intValue();
            BytesUtils.writeLEByte4(out, recordsQty);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Read the header of memo files.
     *
     * @return the memo header
     */
    public static MemoFileHeader readMemoHeader(final ByteBuffer memoByteBuffer) throws
            IOException {
        final byte[] headerBytes = new byte[MEMO_HEADER_LENGTH];
        try {
            memoByteBuffer.get(headerBytes);
            final int nextFreeBlockLocation =
                    BytesUtils.extractBEInt4(headerBytes, 0);
            return new MemoFileHeader(BLOCK_LENGTH, nextFreeBlockLocation,
                    Collections.<String, Object>emptyMap());
        } catch (final BufferUnderflowException e) {
            throw new IOException("The file is corrupted or is not a dbt file", e);
        }
    }
}
