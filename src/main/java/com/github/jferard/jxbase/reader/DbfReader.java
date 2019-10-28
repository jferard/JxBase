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

import com.github.jferard.jxbase.core.DbfMemoRecord;
import com.github.jferard.jxbase.core.DbfMetadata;
import com.github.jferard.jxbase.core.XBaseRecord;
import com.github.jferard.jxbase.util.DbfMetadataUtils;
import com.github.jferard.jxbase.util.IOUtils;
import com.github.jferard.jxbase.util.JdbfUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class DbfReader implements XBaseReader {
    public static XBaseReader create(File dbfFile) throws IOException {
        return new DbfReader(new FileInputStream(dbfFile), null);
    }

    public static XBaseReader create(File dbfFile, File memoFile) throws IOException {
        return new DbfReader(new FileInputStream(dbfFile), DbfMemoReader.fromRandomAccess(memoFile));
    }

    private InputStream dbfInputStream;
    private XBaseMemoReader<DbfMemoRecord> memoReader;
    private DbfMetadata metadata;
    private byte[] oneRecordBuffer;
    private int recordsCounter = 0;

    public DbfReader(InputStream dbfInputStream, XBaseMemoReader<DbfMemoRecord> memoReader) throws IOException {
        this.dbfInputStream = new BufferedInputStream(dbfInputStream, DbfMetadataUtils.BUFFER_SIZE);
        this.memoReader = memoReader;
        this.readMetadata();
    }

    @Override
    public DbfMetadata getMetadata() {
        return metadata;
    }

    private void readMetadata() throws IOException {
        this.metadata = DbfMetadataReader.create().read(this.dbfInputStream);
        oneRecordBuffer = new byte[metadata.getOneRecordLength()];
    }

    @Override
    public XBaseRecord<DbfMemoRecord> read() throws IOException {
        if (IOUtils.isEndOfFieldArray(this.dbfInputStream, JdbfUtils.RECORDS_TERMINATOR)) {
            return null;
        }
        Arrays.fill(oneRecordBuffer, JdbfUtils.NULL_BYTE);
        int readLength = IOUtils.readFully(dbfInputStream, oneRecordBuffer);

        if (readLength < metadata.getOneRecordLength()) {
            throw new IOException("Bad record: " + readLength + " -> " + oneRecordBuffer[0]);
        }

        return new XBaseRecord<DbfMemoRecord>(oneRecordBuffer, metadata, memoReader, ++recordsCounter);
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

}
