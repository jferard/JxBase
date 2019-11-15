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

package com.github.jferard.jxbase.dialect.db4memo;

import com.github.jferard.jxbase.dialect.db3memo.DB3MemoFileHeaderReader;
import com.github.jferard.jxbase.dialect.foxpro.FoxProMemoRecordFactory;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.BitUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;

public class DB4MemoWriter implements XBaseMemoWriter {
    public static DB4MemoWriter fromRandomAccess(final File memoFile, final Charset charset)
            throws IOException {
        final RandomAccessFile randomAccessFile = new RandomAccessFile(memoFile, "rw");
        return new DB4MemoWriter(randomAccessFile.getChannel(),
                new FoxProMemoRecordFactory(charset), 512);
    }

    public static DB4MemoWriter fromChannel(final File memoFile, final Charset charset)
            throws IOException {
        if (memoFile == null) {
            return null;
        }
        final FileOutputStream fileOutputStream = new FileOutputStream(memoFile);
        return new DB4MemoWriter(fileOutputStream.getChannel(),
                new FoxProMemoRecordFactory(charset), 512);
    }

    private final SeekableByteChannel channel;
    private final FoxProMemoRecordFactory dbfMemoRecordFactory;
    private final int blockSize;
    private long curOffsetInBlocks;

    public DB4MemoWriter(final SeekableByteChannel channel,
                         final FoxProMemoRecordFactory dbfMemoRecordFactory, final int blockSize)
            throws IOException {
        this.channel = channel;
        this.dbfMemoRecordFactory = dbfMemoRecordFactory;
        this.blockSize = blockSize;
        this.curOffsetInBlocks = 1;
        this.writeHeader();
    }

    private void writeHeader() throws IOException {
        final byte[] bytes = new byte[DB3MemoFileHeaderReader.MEMO_HEADER_LENGTH];
        bytes[5] = 0x02;
        this.writeBytes(bytes);
    }

    @Override
    public long write(final XBaseMemoRecord memo) throws IOException {
        final int blockSize = 512;
        final int headerSize = 512;
        final int from = 0;
        final long offsetInBlocks = this.curOffsetInBlocks;
        final long start = blockSize * (offsetInBlocks - 1) + headerSize + from;
        this.channel.position(start);
        this.writeBytes(new byte[]{(byte) 0xff, (byte) 0xff, 0x08, 0x00});
        this.writeBytes(BitUtils.makeBEByte4(memo.getLength()));
        final byte[] bytes = memo.getBytes();
        this.writeBytes(bytes);
        this.curOffsetInBlocks += (8 + bytes.length) / blockSize;
        return offsetInBlocks;
    }

    @Override
    public void close() throws IOException {
        this.channel.close();
    }

    @Override
    public void fixMetadata() throws IOException {
        this.channel.position(0);
        this.writeBytes(BitUtils.makeLEByte4((int) this.curOffsetInBlocks));
    }

    private int writeBytes(final byte[] bytes) throws IOException {
        return this.channel.write(ByteBuffer.wrap(bytes));
    }
}
