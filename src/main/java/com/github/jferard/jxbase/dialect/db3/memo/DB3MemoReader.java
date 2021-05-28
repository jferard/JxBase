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

package com.github.jferard.jxbase.dialect.db3.memo;

import com.github.jferard.jxbase.dialect.db3.DB3Utils;
import com.github.jferard.jxbase.memo.ByteMemoRecord;
import com.github.jferard.jxbase.memo.MemoFileHeader;
import com.github.jferard.jxbase.memo.RawMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader of memo files (tested of *.FPT files - Visual FoxPro)
 * See links:
 * <p>
 * Visual FoxPro file formats:
 * http://msdn.microsoft.com/en-us/library/aa977077(v=vs.71).aspx
 * <p>
 * DBase file formats:
 * http://www.dbase.com/Knowledgebase/INT/db7_file_fmt.htm
 * <p>
 * See: https://www.clicketyclick.dk/databases/xbase/format/fpt.html
 */
public class DB3MemoReader implements XBaseMemoReader {
    public final static int BLOCK_SIZE = 512;

    public static DB3MemoReader create(final FileChannel channel) throws IOException {
        final ByteBuffer memoByteBuffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
        final MemoFileHeader header = DB3Utils.readMemoHeader(memoByteBuffer);
        final RawMemoReader rawMemoReader =
                new RawMemoReader(memoByteBuffer, memoByteBuffer.position(),
                        header.getBlockLength(), channel);
        return new DB3MemoReader(header, rawMemoReader);
    }

    private final MemoFileHeader header;
    private final RawMemoReader rawMemoReader;

    DB3MemoReader(final MemoFileHeader header, final RawMemoReader rawMemoReader) {
        this.header = header;
        this.rawMemoReader = rawMemoReader;
    }

    /**
     * @return the header of the memo file.
     */
    public MemoFileHeader getHeader() {
        return this.header;
    }


    @Override
    public void close() throws IOException {
        this.rawMemoReader.close();
    }

    /**
     * @param offsetInBlocks the block number of the record (0 is the header, 1 is the first block)
     * @return the record
     */
    @Override
    public XBaseMemoRecord read(final long offsetInBlocks) {
        final List<byte[]> blocks = new ArrayList<byte[]>();
        int lastBlockSize;
        try {
            do {
                final byte[] blockBytes =
                        this.rawMemoReader.read(offsetInBlocks + blocks.size(), 0, BLOCK_SIZE);
                blocks.add(blockBytes);
                lastBlockSize = this.findTerminator(blockBytes);
            } while (lastBlockSize == -1);
        } catch (final BufferUnderflowException e) {
            lastBlockSize = BLOCK_SIZE;
        }
        final byte[] bytes = this.mergeBlocks(blocks, lastBlockSize);
        return new ByteMemoRecord(bytes, bytes.length);
    }

    private int findTerminator(final byte[] blockBytes) {
        for (int i = 0; i < BLOCK_SIZE; i++) {
            if (blockBytes[i] == JxBaseUtils.RECORDS_TERMINATOR) {
                return i;
            }
        }
        return -1;
    }

    private byte[] mergeBlocks(final List<byte[]> blocks, final int lastBlockSize) {
        final int blockCountMinusOne = blocks.size() - 1;
        if (blockCountMinusOne < 0) {
            return new byte[]{};
        }
        final int size = blockCountMinusOne * BLOCK_SIZE + lastBlockSize;
        final byte[] dst = new byte[size];
        for (int b = 0; b < blockCountMinusOne; b++) {
            System.arraycopy(blocks.get(b), 0, dst, b * BLOCK_SIZE, BLOCK_SIZE);
        }
        System.arraycopy(blocks.get(blockCountMinusOne), 0, dst, blockCountMinusOne * BLOCK_SIZE,
                lastBlockSize);
        return dst;
    }
}