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

import com.github.jferard.jxbase.dialect.memo.ImageMemoRecord;
import com.github.jferard.jxbase.dialect.memo.MemoRecordFactory;
import com.github.jferard.jxbase.dialect.memo.RawMemoReader;
import com.github.jferard.jxbase.dialect.memo.XBaseMemoReader;
import com.github.jferard.jxbase.dialect.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
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
    final static int BLOCK_SIZE = 512;

    public static DB3MemoReader fromRandomAccess(final File memoFile, final Charset charset)
            throws IOException {
        final RandomAccessFile randomAccessFile = new RandomAccessFile(memoFile, "r");
        return new DB3MemoReader(randomAccessFile.getChannel(), new MemoRecordFactory(charset));
    }

    public static DB3MemoReader fromChannel(final File memoFile, final Charset charset)
            throws IOException {
        if (memoFile == null) {
            return null;
        }
        final FileInputStream fileInputStream = new FileInputStream(memoFile);
        return new DB3MemoReader(fileInputStream.getChannel(), new MemoRecordFactory(charset));
    }

    private final ByteBuffer memoByteBuffer;
    private final FileChannel channel;
    private final MemoRecordFactory memoRecordFactory;
    private DB3MemoFileHeader memoHeader;
    private RawMemoReader rawMemoReader;

    public DB3MemoReader(final FileChannel channel, final MemoRecordFactory memoRecordFactory)
            throws IOException {
        this.channel = channel;
        this.memoByteBuffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
        this.memoRecordFactory = memoRecordFactory;
        this.readMetadata();
    }

    private void readMetadata() throws IOException {
        final byte[] headerBytes = new byte[JxBaseUtils.MEMO_HEADER_LENGTH];
        try {
            this.memoByteBuffer.get(headerBytes);
        } catch (final BufferUnderflowException e) {
            throw new IOException("The file is corrupted or is not a dbf file", e);
        }

        this.memoHeader = DB3MemoFileHeader.create(headerBytes);
        this.rawMemoReader =
                new RawMemoReader(this.memoByteBuffer, DB3MemoFileHeader.SIZE, BLOCK_SIZE);
    }

    @Override
    public void close() throws IOException {
        //        this.channel.close();
    }

    /**
     * @param offsetInBlocks the number of the record
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
        } catch (final IllegalArgumentException e) {
            lastBlockSize = BLOCK_SIZE;
        }
        final byte[] bytes = this.mergeBlocks(blocks, lastBlockSize);
        return new ImageMemoRecord(bytes, bytes.length);
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