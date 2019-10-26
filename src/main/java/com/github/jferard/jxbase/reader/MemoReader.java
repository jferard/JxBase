/*
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

package com.github.jferard.jxbase.reader;

import com.github.jferard.jxbase.core.MemoFileHeader;
import com.github.jferard.jxbase.core.MemoRecord;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.JdbfUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * Reader of memo files (tested of *.FPT files - Visual FoxPro)
 * See links:
 * <p>
 * Visual FoxPro file formats:
 * http://msdn.microsoft.com/en-us/library/aa977077(v=vs.71).aspx
 * <p>
 * DBase file formats:
 * http://www.dbase.com/Knowledgebase/INT/db7_file_fmt.htm
 *
 * See: https://www.clicketyclick.dk/databases/xbase/format/fpt.html
 */
public class MemoReader implements Closeable {
    private MemoFileHeader memoHeader;
    private ByteBuffer memoByteBuffer;

    public MemoReader(File memoFile) throws IOException {
        this(new FileInputStream(memoFile));
    }

    public MemoReader(FileInputStream inputStream) throws IOException {
        FileChannel channel = inputStream.getChannel();
        this.memoByteBuffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
        readMetadata();
    }

    private void readMetadata() throws IOException {
        byte[] headerBytes = new byte[JdbfUtils.MEMO_HEADER_LENGTH];
        try {
            this.memoByteBuffer.get(headerBytes);
        } catch (BufferUnderflowException e) {
            throw new IOException("The file is corrupted or is not a dbf file", e);
        }

        this.memoHeader = MemoFileHeader.create(headerBytes);
    }

    @Override
    public void close() throws IOException {
        this.memoByteBuffer = null;
    }

    public MemoFileHeader getMemoHeader() {
        return memoHeader;
    }

    /**
     * @param offsetInBlocks the number of the record
     * @return the record
     * @throws IOException
     */
    public MemoRecord read(int offsetInBlocks) throws IOException {
        byte[] recordHeader = new byte[JdbfUtils.RECORD_HEADER_LENGTH];

        int start = memoHeader.getBlockSize() * offsetInBlocks;
        this.memoByteBuffer.position(start);
        this.memoByteBuffer.get(recordHeader);
        // record type is at 0-3
        int memoRecordLength = BitUtils.makeInt(recordHeader[7], recordHeader[6], recordHeader[5],
                recordHeader[4]);

        byte[] recordBody = new byte[memoRecordLength];
        this.memoByteBuffer.get(recordBody);
        return new MemoRecord(recordHeader, recordBody, memoHeader.getBlockSize(), offsetInBlocks);
    }
}