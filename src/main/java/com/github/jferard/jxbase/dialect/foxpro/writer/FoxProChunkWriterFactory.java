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

package com.github.jferard.jxbase.dialect.foxpro.writer;

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.dialect.db3.writer.DB3FieldDescriptorArrayWriter;
import com.github.jferard.jxbase.dialect.db3.writer.DB3RecordWriter;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.writer.DB4MetadataWriter;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.memo.FoxProMemoWriter;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
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
 * A writer for foxpro chunks
 */
public class FoxProChunkWriterFactory
        implements XBaseChunkWriterFactory<DB4Access, FoxProDialect> {
    private final FoxProDialect dialect;
    private final TimeZone timeZone;

    public FoxProChunkWriterFactory(final FoxProDialect dialect, final TimeZone timeZone) {
        this.dialect = dialect;
        this.timeZone = timeZone;
    }

    @Override
    public XBaseMetadataWriter<DB4Access, FoxProDialect> createMetadataWriter(
            final RandomAccessFile file,
            final OutputStream outputStream,
            final Charset charset) {
        return new DB4MetadataWriter<DB4Access, FoxProDialect>(this.dialect, file,
                outputStream, charset);
    }

    @Override
    public XBaseFieldDescriptorArrayWriter<DB4Access> createFieldDescriptorArrayWriter(
            final OutputStream outputStream, final XBaseMetadata metadata) {
        return new DB3FieldDescriptorArrayWriter<DB4Access>(this.dialect.getAccess(),
                outputStream);
    }

    @Override
    public XBaseOptionalWriter<FoxProDialect> createOptionalWriter(final OutputStream outputStream,
                                                                   final XBaseMetadata metadata,
                                                                   final XBaseFieldDescriptorArray<DB4Access> array) {
        return new FoxProOptionalWriter<DB4Access, FoxProDialect>(this.dialect,
                outputStream, metadata, array);
    }

    @Override
    public XBaseRecordWriter<FoxProDialect> createRecordWriter(final OutputStream outputStream,
                                                               final Charset charset,
                                                               final XBaseMemoWriter memoWriter,
                                                               final XBaseMetadata metadata,
                                                               final XBaseFieldDescriptorArray<DB4Access> array,
                                                               final Object optional) {
        return new DB3RecordWriter<DB4Access, FoxProDialect>(this.dialect, outputStream,
                charset,
                memoWriter, array.getFields());
    }

    @Override
    public XBaseMemoWriter createMemoWriter(final XBaseFileTypeEnum type, final String tableName,
                                            final Map<String, Object> memoHeaderMetadata)
            throws IOException {
        final File memoFile = new File(tableName + type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileOutputStream(memoFile).getChannel();
        return new FoxProMemoWriter(memoChannel, 512, memoHeaderMetadata);
    }
}
