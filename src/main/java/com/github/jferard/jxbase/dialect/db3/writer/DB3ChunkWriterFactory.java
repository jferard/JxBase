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

package com.github.jferard.jxbase.dialect.db3.writer;

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.dialect.db3.memo.DB3MemoWriter;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.writer.GenericOptionalWriter;
import com.github.jferard.jxbase.writer.XBaseChunkWriterFactory;
import com.github.jferard.jxbase.writer.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.XBaseMetadataWriter;
import com.github.jferard.jxbase.writer.XBaseOptionalWriter;
import com.github.jferard.jxbase.writer.XBaseRecordWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TimeZone;

/**
 * A writer for DB3 chunks.
 */
public class DB3ChunkWriterFactory implements XBaseChunkWriterFactory<DB3Access, DB3Dialect> {
    private final DB3Dialect dialect;
    private final TimeZone timeZone;

    public DB3ChunkWriterFactory(final DB3Dialect dialect, final TimeZone timeZone) {
        this.dialect = dialect;
        this.timeZone = timeZone;
    }

    @Override
    public XBaseMetadataWriter<DB3Access, DB3Dialect> createMetadataWriter(
            final RandomAccessFile file, final OutputStream outputStream, final Charset charset) {
        return new DB3MetadataWriter<DB3Access, DB3Dialect>(this.dialect, file, outputStream,
                charset);
    }

    @Override
    public XBaseFieldDescriptorArrayWriter<DB3Access> createFieldDescriptorArrayWriter(
            final OutputStream outputStream, final XBaseMetadata metadata) {
        return new DB3FieldDescriptorArrayWriter<DB3Access>(this.dialect.getAccess(), outputStream);
    }

    @Override
    public XBaseOptionalWriter<DB3Dialect> createOptionalWriter(final OutputStream outputStream,
                                                                final XBaseMetadata metadata,
                                                                final XBaseFieldDescriptorArray<DB3Access> array) {
        return new GenericOptionalWriter<DB3Access, DB3Dialect>(this.dialect, outputStream,
                metadata, array);
    }

    @Override
    public XBaseRecordWriter<DB3Dialect> createRecordWriter(final OutputStream outputStream,
                                                            final Charset charset,
                                                            final XBaseMetadata metadata,
                                                            final XBaseFieldDescriptorArray<DB3Access> array,
                                                            final XBaseMemoWriter memoWriter,
                                                            final Object optional) {
        return new DB3RecordWriter<DB3Access, DB3Dialect>(this.dialect, outputStream, charset,
                memoWriter, array.getFields());
    }

    @Override
    public XBaseMemoWriter createMemoWriter(final XBaseFileTypeEnum type, final String tableName,
                                            final Map<String, Object> memoHeaderMetadata)
            throws IOException {
        final File memoFile = new File(tableName + type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileOutputStream(memoFile).getChannel();
        return new DB3MemoWriter(memoChannel, memoHeaderMetadata);
    }
}
