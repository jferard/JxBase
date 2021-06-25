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

package com.github.jferard.jxbase.dialect.db4.memo;

import com.github.jferard.jxbase.dialect.db4.DB4Utils;
import com.github.jferard.jxbase.dialect.db4.reader.MemoFileHeaderReader;
import com.github.jferard.jxbase.memo.ByteMemoRecord;
import com.github.jferard.jxbase.memo.MemoFileHeader;
import com.github.jferard.jxbase.memo.RawMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.util.BytesUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Arrays;

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
public class DB4MemoReader implements XBaseMemoReader {
    /**
     * Create a new memo reader
     * @param channel  the file channel
     * @param memoFileHeaderReader the reader of the header
     * @return the reader
     * @throws IOException
     */
    public static XBaseMemoReader create(final FileChannel channel,
                                         final MemoFileHeaderReader memoFileHeaderReader)
            throws IOException {
        final ByteBuffer memoByteBuffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
        final MemoFileHeader memoHeader = memoFileHeaderReader.read(memoByteBuffer);
        final RawMemoReader rawMemoReader =
                new RawMemoReader(memoByteBuffer, memoByteBuffer.position(),
                        memoHeader.getBlockLength(), channel);
        return new DB4MemoReader(rawMemoReader);
    }

    private final RawMemoReader rawMemoReader;

    public DB4MemoReader(final RawMemoReader rawMemoReader) {
        this.rawMemoReader = rawMemoReader;
    }

    @Override
    public void close() throws IOException {
        this.rawMemoReader.close();
    }

    @Override
    public XBaseMemoRecord read(final long offsetInBlocks) {
        final byte[] recordHeaderBytes = this.rawMemoReader.read(offsetInBlocks, 0, 8);
        if (DB4Utils.MEMO_FIELD_RESERVED_BYTES[0] != recordHeaderBytes[0] ||
                DB4Utils.MEMO_FIELD_RESERVED_BYTES[1] != recordHeaderBytes[1] ||
                DB4Utils.MEMO_FIELD_RESERVED_BYTES[2] != recordHeaderBytes[2] ||
                DB4Utils.MEMO_FIELD_RESERVED_BYTES[3] != recordHeaderBytes[3]) {
            throw new IllegalArgumentException(
                    String.format("Not a DB4 memo at offset %d blocks. Expected %s, got %s.",
                            offsetInBlocks, Arrays.toString(DB4Utils.MEMO_FIELD_RESERVED_BYTES),
                            Arrays.toString(recordHeaderBytes)));
        }
        final int memoRecordLength =
                BytesUtils.extractLEInt4(recordHeaderBytes, 4);
        final byte[] dataBytes = this.rawMemoReader.read(offsetInBlocks, 8, memoRecordLength);
        return new ByteMemoRecord(dataBytes, memoRecordLength);
    }
}