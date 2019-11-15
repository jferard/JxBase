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

package com.github.jferard.jxbase.dialect.db3memo;

import com.github.jferard.jxbase.memo.MemoFileHeader;
import com.github.jferard.jxbase.util.BitUtils;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Collections;

/**
 *
 */
public class DB3MemoFileHeaderReader {
    public static final int MEMO_HEADER_LENGTH = 512;
    static final int BLOCK_LENGTH = 512;

    /**
     * @return the memo header
     */
    public static MemoFileHeader read(final ByteBuffer memoByteBuffer) throws IOException {
        final byte[] headerBytes = new byte[DB3MemoFileHeaderReader.MEMO_HEADER_LENGTH];
        try {
            memoByteBuffer.get(headerBytes);
            final int nextFreeBlockLocation =
                    BitUtils.makeInt(headerBytes[3], headerBytes[2], headerBytes[1],
                            headerBytes[0]);
            return new MemoFileHeader(BLOCK_LENGTH, nextFreeBlockLocation,
                    Collections.<String, Object>emptyMap());
        } catch (final BufferUnderflowException e) {
            throw new IOException("The file is corrupted or is not a dbt file", e);
        }
    }
}
