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

package com.github.jferard.jxbase.writer;

import com.github.jferard.jxbase.memo.MemoRecordFactory;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.util.BitUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;

public class GenericMemoWriter implements XBaseMemoWriter {
    public static GenericMemoWriter fromRandomAccess(final File memoFile, final Charset charset)
            throws IOException {
        final RandomAccessFile randomAccessFile = new RandomAccessFile(memoFile, "rw");
        return new GenericMemoWriter(randomAccessFile.getChannel(), new MemoRecordFactory(charset));
    }

    public static GenericMemoWriter fromChannel(final File memoFile, final Charset charset)
            throws IOException {
        if (memoFile == null) {
            return null;
        }
        final FileOutputStream fileOutputStream = new FileOutputStream(memoFile);
        return new GenericMemoWriter(fileOutputStream.getChannel(), new MemoRecordFactory(charset));
    }

    private final SeekableByteChannel channel;
    private final MemoRecordFactory dbfMemoRecordFactory;
    private long curOffsetInBlocks;

    public GenericMemoWriter(final SeekableByteChannel channel,
                             final MemoRecordFactory dbfMemoRecordFactory) throws IOException {
        this.channel = channel;
        this.dbfMemoRecordFactory = dbfMemoRecordFactory;
        this.curOffsetInBlocks = 1;
        this.writeHeader();
    }

    private void writeHeader() throws IOException {
        final byte[] bytes = new byte[512];
        bytes[5] = 0x02;
        this.writeBytes(bytes);
    }

    @Override
    public long write(final XBaseMemoRecord<?> memo) throws IOException {
        final int blockSize = 512;
        final int headerSize = 512;
        final int from = 0;
        final long offsetInBlocks = this.curOffsetInBlocks;
        final long start = blockSize * (offsetInBlocks - 1) + headerSize + from;
        this.channel.position(start);
        this.writeBytes(BitUtils.makeByte4(memo.getMemoType().getType()));
        this.writeBytes(BitUtils.makeByte4(memo.getLength()));
        final byte[] bytes = memo.getBytes();
        this.writeBytes(bytes);
        this.curOffsetInBlocks += (8 + bytes.length) / blockSize;
        return offsetInBlocks;
    }

    private int writeBytes(final byte[] bytes) throws IOException {
        return this.channel.write(ByteBuffer.wrap(bytes));
    }
}
