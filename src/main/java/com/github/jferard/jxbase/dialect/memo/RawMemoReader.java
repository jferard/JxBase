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

package com.github.jferard.jxbase.dialect.memo;

import java.nio.ByteBuffer;

/**
 * A memo file has a header and a sequence of blocks terminated by a terminator
 */
public class RawMemoReader {
    private final ByteBuffer memoByteBuffer;
    private final int headerSize;
    private final int blockSize;

    public RawMemoReader(final ByteBuffer memoByteBuffer, final int headerSize,
                         final int blockSize) {
        this.memoByteBuffer = memoByteBuffer;
        this.headerSize = headerSize;
        this.blockSize = blockSize;
    }

    /**
     * @param offsetInBlocks the number of the record, 0 = header, 1 = first block, ...
     * @return the record
     */
    public byte[] read(final long offsetInBlocks) {
        return this.read(offsetInBlocks, 0, this.blockSize);
    }

    /**
     * @param offsetInBlocks the number of the record, 0 = header, 1 = first block, ...
     * @return the record
     */
    public byte[] read(final long offsetInBlocks, final int from, final int length) {
        assert offsetInBlocks > 0;
        final long start = this.blockSize * (offsetInBlocks - 1) + this.headerSize + from;
        final byte[] memoBlock = new byte[length];
        this.memoByteBuffer.position((int) start);
        this.memoByteBuffer.get(memoBlock);
        return memoBlock;
    }
}