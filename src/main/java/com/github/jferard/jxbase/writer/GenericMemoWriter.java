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

import com.github.jferard.jxbase.core.MemoRecordFactory;
import com.github.jferard.jxbase.core.XBaseMemoRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class GenericMemoWriter implements XBaseMemoWriter {
    public static GenericMemoWriter fromRandomAccess(final File memoFile, final Charset charset)
            throws IOException {
        final RandomAccessFile randomAccessFile = new RandomAccessFile(memoFile, "rw");
        return new GenericMemoWriter(randomAccessFile.getChannel(),
                new MemoRecordFactory(charset));
    }

    public static GenericMemoWriter fromChannel(final File memoFile, final Charset charset)
            throws IOException {
        if (memoFile == null) {
            return null;
        }
        final FileOutputStream fileOutputStream = new FileOutputStream(memoFile);
        return new GenericMemoWriter(fileOutputStream.getChannel(),
                new MemoRecordFactory(charset));
    }

    private final FileChannel channel;
    private final MemoRecordFactory dbfMemoRecordFactory;

    public GenericMemoWriter(final FileChannel channel, final MemoRecordFactory dbfMemoRecordFactory)
            throws IOException {
        this.channel = channel;
        this.dbfMemoRecordFactory = dbfMemoRecordFactory;
        this.writeHeader();
    }

    private void writeHeader() throws IOException {
        final byte[] bytes = new byte[512];
        bytes[5] = 0x02;
        this.channel.write(ByteBuffer.wrap(bytes));
    }

    @Override
    public void write(final long offsetInBlocks, final XBaseMemoRecord<?> memo) throws IOException {
        final int blockSize = 512;
        final int headerSize = 512;
        final int from = 0;
        final long start = blockSize * (offsetInBlocks - 1) + headerSize + from;
        this.channel.position(start);
        this.channel.write(ByteBuffer.wrap(memo.getBytes()));
    }
}
