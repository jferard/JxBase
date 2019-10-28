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
import com.github.jferard.jxbase.core.DbfMemoRecord;
import com.github.jferard.jxbase.core.MemoRecordTypeEnum;
import com.github.jferard.jxbase.core.XBaseMemoRecord;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.JdbfUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
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
public class DbfMemoReader implements XBaseMemoReader<DbfMemoRecord> {
    public static DbfMemoReader fromRandomAccess(File memoFile) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(memoFile, "r");
        return new DbfMemoReader(randomAccessFile.getChannel());
    }

    public static DbfMemoReader fromChannel(File memoFile) throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(memoFile);
        return new DbfMemoReader(fileInputStream.getChannel());
    }
    private MemoFileHeader memoHeader;
    private ByteBuffer memoByteBuffer;
    private FileChannel channel;
    private RawMemoReader rawMemoReader;

    public DbfMemoReader(FileChannel channel) throws IOException {
        this.channel = channel;
        this.memoByteBuffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
        this.readMetadata();
    }

    private void readMetadata() throws IOException {
        byte[] headerBytes = new byte[JdbfUtils.MEMO_HEADER_LENGTH];
        try {
            this.memoByteBuffer.get(headerBytes);
        } catch (BufferUnderflowException e) {
            throw new IOException("The file is corrupted or is not a dbf file", e);
        }

        this.memoHeader = MemoFileHeader.create(headerBytes);
        this.rawMemoReader =
                new RawMemoReader(this.memoByteBuffer, this.memoHeader.getNextFreeBlockLocation(),
                        this.memoHeader.getBlockSize());
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
    public DbfMemoRecord read(int offsetInBlocks) {
        byte[] recordBytes = this.rawMemoReader.read(offsetInBlocks);
        System.out.println(Arrays.toString(recordBytes));
        MemoRecordTypeEnum memoRecordType = MemoRecordTypeEnum.fromInt(BitUtils.makeInt(recordBytes[3], recordBytes[2], recordBytes[1],
                recordBytes[0]));
        int memoRecordLength = BitUtils.makeInt(recordBytes[7], recordBytes[6], recordBytes[5],
                recordBytes[4]);
        byte[] dataBytes = new byte[memoRecordLength];
        System.arraycopy(recordBytes, 8, dataBytes, 0, memoRecordLength);
        return new DbfMemoRecord(dataBytes, memoRecordType, memoRecordLength, offsetInBlocks);
    }
}