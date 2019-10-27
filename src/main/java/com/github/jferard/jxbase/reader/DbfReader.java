/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

import com.github.jferard.jxbase.core.DbfMetadata;
import com.github.jferard.jxbase.core.DbfRecord;
import com.github.jferard.jxbase.util.DbfMetadataUtils;
import com.github.jferard.jxbase.util.IOUtils;
import com.github.jferard.jxbase.util.JdbfUtils;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class DbfReader implements Closeable {

    private InputStream dbfInputStream;
    private MemoReader memoReader;
    private DbfMetadata metadata;
    private byte[] oneRecordBuffer;
    private int recordsCounter = 0;

    public DbfReader(File dbfFile) throws IOException {
        this(new FileInputStream(dbfFile));
    }

    public DbfReader(File dbfFile, File memoFile) throws IOException {
        this(new FileInputStream(dbfFile), new FileInputStream(memoFile));
    }

    public DbfReader(InputStream dbfInputStream) throws IOException {
        this.dbfInputStream = new BufferedInputStream(dbfInputStream, DbfMetadataUtils.BUFFER_SIZE);
        readMetadata();
    }

    public DbfReader(InputStream dbfInputStream, FileInputStream memoInputStream)
            throws IOException {
        this.dbfInputStream = new BufferedInputStream(dbfInputStream, DbfMetadataUtils.BUFFER_SIZE);
        this.memoReader = new MemoReader(memoInputStream);
        readMetadata();
    }

    public DbfMetadata getMetadata() {
        return metadata;
    }

    private void readMetadata() throws IOException {
        this.dbfInputStream.mark(1024 * 1024);
        this.metadata = new DbfMetadataReader().read(this.dbfInputStream);
        oneRecordBuffer = new byte[metadata.getOneRecordLength()];
    }

    @Override
    public void close() throws IOException {
        if (memoReader != null) {
            memoReader.close();
            memoReader = null;
        }
        if (dbfInputStream != null) {
            dbfInputStream.close();
            dbfInputStream = null;
        }
        metadata = null;
        recordsCounter = 0;
    }

    public DbfRecord read() throws IOException {
        Arrays.fill(oneRecordBuffer, JdbfUtils.NULL_BYTE);
        int readLength = IOUtils.readFully(dbfInputStream, oneRecordBuffer);

        if (readLength < metadata.getOneRecordLength()) {
            return null;
            // throw new IOException("Can't read record");
        }

        return createDbfRecord();
    }

    private DbfRecord createDbfRecord() {
        return new DbfRecord(oneRecordBuffer, metadata, memoReader, ++recordsCounter);
    }
}
