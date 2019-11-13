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

import com.github.jferard.jxbase.dialect.memo.MemoRecordFactory;
import com.github.jferard.jxbase.dialect.memo.MemoRecordTypeEnum;
import com.github.jferard.jxbase.dialect.memo.RawMemoReader;
import com.github.jferard.jxbase.dialect.memo.XBaseMemoReader;
import com.github.jferard.jxbase.dialect.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.util.BitUtils;
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
    public static DB4MemoReader fromRandomAccess(final File memoFile, final Charset charset)
            throws IOException {
        final RandomAccessFile randomAccessFile = new RandomAccessFile(memoFile, "r");
        return new DB4MemoReader(randomAccessFile.getChannel(), new MemoRecordFactory(charset));
    }

    public static DB4MemoReader fromChannel(final File memoFile, final Charset charset)
            throws IOException {
        if (memoFile == null) {
            return null;
        }
        final FileInputStream fileInputStream = new FileInputStream(memoFile);
        return new DB4MemoReader(fileInputStream.getChannel(), new MemoRecordFactory(charset));
    }

    private final ByteBuffer memoByteBuffer;
    private final FileChannel channel;
    private final MemoRecordFactory memoRecordFactory;
    private DB4MemoFileHeader memoHeader;
    private RawMemoReader rawMemoReader;

    public DB4MemoReader(final FileChannel channel, final MemoRecordFactory memoRecordFactory)
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

        this.memoHeader = DB4MemoFileHeader.create(headerBytes);
        this.rawMemoReader =
                new RawMemoReader(this.memoByteBuffer, 512, this.memoHeader.getBlockSize());
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
        final byte[] recordHeaderBytes = this.rawMemoReader.read(offsetInBlocks, 0, 8);
        final MemoRecordTypeEnum memoRecordType = MemoRecordTypeEnum.fromInt(
                BitUtils.makeInt(recordHeaderBytes[3], recordHeaderBytes[2], recordHeaderBytes[1],
                        recordHeaderBytes[0]));
        final int memoRecordLength =
                BitUtils.makeInt(recordHeaderBytes[7], recordHeaderBytes[6], recordHeaderBytes[5],
                        recordHeaderBytes[4]);
        final byte[] dataBytes = this.rawMemoReader.read(offsetInBlocks, 8, memoRecordLength);
        return this.memoRecordFactory
                .create(dataBytes, memoRecordType, memoRecordLength, offsetInBlocks);
    }
}