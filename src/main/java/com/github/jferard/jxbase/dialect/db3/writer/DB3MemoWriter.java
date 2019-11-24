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

package com.github.jferard.jxbase.dialect.db3.writer;

import com.github.jferard.jxbase.dialect.db3.reader.DB3MemoFileHeaderReader;
import com.github.jferard.jxbase.dialect.db3.reader.DB3MemoReader;
import com.github.jferard.jxbase.memo.RawMemoWriter;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.BitUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.util.Map;

public class DB3MemoWriter implements XBaseMemoWriter {
    public static DB3MemoWriter fromRandomAccess(final File memoFile, final Charset charset,
                                                 final Map<String, Object> headerMeta)
            throws IOException {
        final RandomAccessFile randomAccessFile = new RandomAccessFile(memoFile, "rw");
        return new DB3MemoWriter(randomAccessFile.getChannel(), headerMeta);
    }

    public static DB3MemoWriter fromChannel(final File memoFile, final Charset charset,
                                            final Map<String, Object> headerMeta)
            throws IOException {
        final FileOutputStream fileOutputStream = new FileOutputStream(memoFile);
        return new DB3MemoWriter(fileOutputStream.getChannel(), headerMeta);
    }

    private final RawMemoWriter rawMemoWriter;
    private long curOffsetInBlocks;

    public DB3MemoWriter(final SeekableByteChannel channel, final Map<String, Object> headerMeta)
            throws IOException {
        this.rawMemoWriter = new RawMemoWriter(channel, DB3MemoFileHeaderReader.BLOCK_LENGTH,
                DB3MemoReader.BLOCK_SIZE);
        this.writeHeader(headerMeta);
    }

    private void writeHeader(final Map<String, Object> headerMeta) throws IOException {
        final byte[] headerBytes = new byte[DB3MemoFileHeaderReader.MEMO_HEADER_LENGTH];
        headerBytes[16] = 0x03;
        this.rawMemoWriter.write(0, 0, headerBytes);
        this.curOffsetInBlocks = 1;
    }

    @Override
    public long write(final XBaseMemoRecord memo) throws IOException {
        this.curOffsetInBlocks =
                this.rawMemoWriter.write(this.curOffsetInBlocks, 0, memo.getBytes());
        return this.curOffsetInBlocks;
    }

    @Override
    public void close() throws IOException {
        this.rawMemoWriter.close();
    }

    @Override
    public void fixMetadata() throws IOException {
        this.rawMemoWriter.write(0, 0, BitUtils.makeLEByte4((int) this.curOffsetInBlocks));
    }
}
