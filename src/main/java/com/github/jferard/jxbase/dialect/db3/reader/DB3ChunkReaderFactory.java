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

package com.github.jferard.jxbase.dialect.db3.reader;

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.dialect.db2.reader.DB2OptionalReader;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.dialect.db3.memo.DB3MemoReader;
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
 * A factory to create readers for various chunks.
 */
public class DB3ChunkReaderFactory implements XBaseChunkReaderFactory<DB3Access, DB3Dialect> {
    protected final DB3Dialect dialect;
    protected final TimeZone timezone;

    public DB3ChunkReaderFactory(final DB3Dialect dialect, final TimeZone timezone) {
        this.dialect = dialect;
        this.timezone = timezone;
    }

    @Override
    public XBaseMetadataReader createMetadataReader(final InputStream inputStream) {
        return new DB3MetadataReader(this.dialect, inputStream);
    }

    @Override
    public XBaseFieldDescriptorArrayReader<DB3Access, DB3Dialect> createFieldDescriptorArrayReader(
            final InputStream inputStream, final XBaseMetadata metadata) {
        return new DB3FieldDescriptorArrayReader<DB3Access, DB3Dialect>(this.dialect, inputStream,
                metadata);
    }

    @Override
    public XBaseRecordReader createRecordReader(final InputStream inputStream,
                                                final Charset charset,
                                                final XBaseMemoReader memoReader,
                                                final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray<DB3Access> array,
                                                final Object optional) {
        return new DB3RecordReader<DB3Access>(this.dialect.getAccess(), inputStream, memoReader,
                charset, array, this.timezone);
    }

    @Override
    public XBaseOptionalReader createOptionalReader(final InputStream inputStream,
                                                    final Charset charset,
                                                    final XBaseMetadata metadata,
                                                    final XBaseFieldDescriptorArray<DB3Access> array) {
        return new DB2OptionalReader<DB3Access, DB3Dialect>(this.dialect, inputStream, metadata,
                array);
    }

    @Override
    public XBaseMemoReader createMemoReader(final XBaseFileTypeEnum type, final String tableName,
                                            final Charset charset)
            throws IOException {
        final File memoFile = new File(tableName + type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileInputStream(memoFile).getChannel();
        return DB3MemoReader.create(memoChannel);
    }
}
