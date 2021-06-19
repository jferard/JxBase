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

package com.github.jferard.jxbase.dialect.db4.reader;

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.dialect.db2.reader.DB2OptionalReader;
import com.github.jferard.jxbase.dialect.db3.reader.DB3FieldDescriptorArrayReader;
import com.github.jferard.jxbase.dialect.db3.reader.DB3RecordReader;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.dialect.db4.memo.DB4MemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.reader.XBaseChunkReaderFactory;
import com.github.jferard.jxbase.reader.XBaseFieldDescriptorArrayReader;
import com.github.jferard.jxbase.reader.XBaseMetadataReader;
import com.github.jferard.jxbase.reader.XBaseOptionalReader;
import com.github.jferard.jxbase.reader.XBaseRecordReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.TimeZone;

/**
 * A factory for DB4 chunks readers.
 */
public class DB4ChunkReaderFactory implements XBaseChunkReaderFactory<DB4Access, DB4Dialect> {
    protected final DB4Dialect dialect;
    protected final TimeZone timezone;

    public DB4ChunkReaderFactory(final DB4Dialect dialect, final TimeZone timezone) {
        this.dialect = dialect;
        this.timezone = timezone;
    }

    @Override
    public XBaseMetadataReader createMetadataReader(final InputStream inputStream) {
        return new DB4MetadataReader(this.dialect, inputStream);
    }

    @Override
    public XBaseFieldDescriptorArrayReader<DB4Access, DB4Dialect> createFieldDescriptorArrayReader(
            final InputStream inputStream, final XBaseMetadata metadata) {
        return new DB3FieldDescriptorArrayReader<DB4Access, DB4Dialect>(this.dialect, inputStream,
                metadata);
    }

    @Override
    public XBaseRecordReader createRecordReader(final InputStream inputStream,
                                                final Charset charset,
                                                final XBaseMemoReader memoReader,
                                                final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray<DB4Access> array,
                                                final Object optional) {
        return new DB3RecordReader<DB4Access>(this.dialect.getAccess(), inputStream, memoReader,
                charset, array,
                this.timezone);
    }

    @Override
    public XBaseMemoReader createMemoReader(final XBaseFileTypeEnum type, final String tableName,
                                            final Charset charset)
            throws IOException {
        final File memoFile = new File(tableName + type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileInputStream(memoFile).getChannel();
        /* other version:
         * final FileChannel memoChannel = new RandomAccessFile(memoFile, "r").getChannel();
         */
        return DB4MemoReader.create(memoChannel, new DB4MemoFileHeaderReader());
    }


    @Override
    public XBaseOptionalReader createOptionalReader(final InputStream inputStream,
                                                    final Charset charset,
                                                    final XBaseMetadata metadata,
                                                    final XBaseFieldDescriptorArray<DB4Access> array) {
        return new DB2OptionalReader<DB4Access, DB4Dialect>(this.dialect, inputStream, metadata,
                array);
    }
}
