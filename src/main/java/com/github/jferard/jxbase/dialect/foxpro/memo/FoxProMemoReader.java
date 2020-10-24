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

package com.github.jferard.jxbase.dialect.foxpro.memo;

import com.github.jferard.jxbase.dialect.db4.reader.MemoFileHeaderReader;
import com.github.jferard.jxbase.memo.MemoFileHeader;
import com.github.jferard.jxbase.memo.MemoRecordType;
import com.github.jferard.jxbase.memo.RawMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.util.BytesUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * A fox pro memo reader.
 */
public class FoxProMemoReader implements XBaseMemoReader {
    public static XBaseMemoReader create(final FileChannel channel, final FoxProMemoRecordFactory memoRecordFactory,
                                         final MemoFileHeaderReader memoFileHeaderReader) throws IOException {
        final ByteBuffer memoByteBuffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
        final MemoFileHeader memoHeader = memoFileHeaderReader.read(memoByteBuffer);
        final RawMemoReader rawMemoReader = new RawMemoReader(memoByteBuffer, memoByteBuffer.position(),
                memoHeader.getBlockLength());
        return new FoxProMemoReader(channel, memoRecordFactory, rawMemoReader);
    }

    private final FileChannel channel;
    private final FoxProMemoRecordFactory memoRecordFactory;
    private final RawMemoReader rawMemoReader;

    public FoxProMemoReader(final FileChannel channel, final FoxProMemoRecordFactory memoRecordFactory,
                            final RawMemoReader rawMemoReader) {
        this.channel = channel;
        this.memoRecordFactory = memoRecordFactory;
        this.rawMemoReader = rawMemoReader;
    }

    @Override
    public void close() throws IOException {
        this.channel.close();
    }

    /**
     * @param offsetInBlocks the number of the record
     * @return the record
     */
    @Override
    public XBaseMemoRecord read(final long offsetInBlocks) {
        final byte[] recordHeaderBytes = this.rawMemoReader.read(offsetInBlocks, 0, 8);
        final MemoRecordType memoRecordType = MemoRecordType.fromInt(
                BytesUtils.makeLEInt(recordHeaderBytes[3], recordHeaderBytes[2], recordHeaderBytes[1],
                        recordHeaderBytes[0]));
        final int memoRecordLength =
                BytesUtils.makeLEInt(recordHeaderBytes[7], recordHeaderBytes[6], recordHeaderBytes[5],
                        recordHeaderBytes[4]);
        final byte[] dataBytes = this.rawMemoReader.read(offsetInBlocks, 8, memoRecordLength);
        return this.memoRecordFactory
                .create(dataBytes, memoRecordType, memoRecordLength);
    }
}