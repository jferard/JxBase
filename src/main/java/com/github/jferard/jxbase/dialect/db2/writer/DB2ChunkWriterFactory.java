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

package com.github.jferard.jxbase.dialect.db2.writer;

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.writer.GenericRecordWriter;
import com.github.jferard.jxbase.writer.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.XBaseChunkWriterFactory;
import com.github.jferard.jxbase.writer.XBaseMetadataWriter;
import com.github.jferard.jxbase.writer.XBaseOptionalWriter;
import com.github.jferard.jxbase.writer.XBaseRecordWriter;

import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TimeZone;

/**
 * A chunk writer factory for DB2.
 */
public class DB2ChunkWriterFactory implements XBaseChunkWriterFactory<DB2Access, DB2Dialect> {
    private final DB2Dialect dialect;
    private final TimeZone timeZone;

    public DB2ChunkWriterFactory(final DB2Dialect dialect, final TimeZone timeZone) {
        this.dialect = dialect;
        this.timeZone = timeZone;
    }

    @Override
    public XBaseMetadataWriter<DB2Access, DB2Dialect> createMetadataWriter(
            final RandomAccessFile file, final OutputStream outputStream, final Charset charset) {
        return new DB2MetadataWriter<DB2Access, DB2Dialect>(this.dialect, file, outputStream,
                charset);
    }

    @Override
    public XBaseFieldDescriptorArrayWriter<DB2Access> createFieldDescriptorArrayWriter(
            final OutputStream outputStream, final XBaseMetadata metadata) {
        return new DB2FieldDescriptorArrayWriter<DB2Access>(this.dialect.getAccess(), outputStream);
    }

    @Override
    public XBaseOptionalWriter<DB2Dialect> createOptionalWriter(final OutputStream outputStream,
                                                                final XBaseMetadata metadata,
                                                                final XBaseFieldDescriptorArray<DB2Access> array) {
        return new DB2OptionalWriter<DB2Access, DB2Dialect>(this.dialect, outputStream, metadata,
                array);
    }

    @Override
    public XBaseRecordWriter<DB2Dialect> createRecordWriter(final OutputStream outputStream,
                                                            final Charset charset,
                                                            final XBaseMemoWriter memoWriter,
                                                            final XBaseMetadata metadata,
                                                            final XBaseFieldDescriptorArray<DB2Access> array,
                                                            final Object optional) {
        return GenericRecordWriter.create(this.dialect, outputStream, charset,
                array.getFields());
    }

    @Override
    public XBaseMemoWriter createMemoWriter(final XBaseFileTypeEnum type, final String tableName,
                                            final Map<String, Object> memoHeaderMetadata) {
        return null;
    }
}
