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

package com.github.jferard.jxbase.dialect.db3.writer;

import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.writer.internal.GenericOptionalWriter;
import com.github.jferard.jxbase.writer.internal.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.internal.XBaseInternalWriterFactory;
import com.github.jferard.jxbase.writer.internal.XBaseMetadataWriter;
import com.github.jferard.jxbase.writer.internal.XBaseOptionalWriter;
import com.github.jferard.jxbase.writer.internal.XBaseRecordWriter;

import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.TimeZone;

public class DB3InternalWriterFactory implements XBaseInternalWriterFactory<DB3Dialect, DB3Access> {
    private final DB3Dialect dialect;
    private final TimeZone timeZone;

    public DB3InternalWriterFactory(final DB3Dialect dialect, final TimeZone timeZone) {
        this.dialect = dialect;
        this.timeZone = timeZone;
    }

    @Override
    public XBaseMetadataWriter<DB3Dialect, DB3Access> createMetadataWriter(
            final RandomAccessFile file, final OutputStream outputStream, final Charset charset) {
        return new DB3MetadataWriter<DB3Dialect, DB3Access>(this.dialect, file, outputStream,
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
        return new GenericOptionalWriter<DB3Dialect, DB3Access>(this.dialect, outputStream,
                metadata, array);
    }

    @Override
    public XBaseRecordWriter<DB3Dialect> createRecordWriter(final OutputStream outputStream,
                                                            final Charset charset,
                                                            final XBaseMetadata metadata,
                                                            final XBaseFieldDescriptorArray<DB3Access> array,
                                                            final Object optional) {
        return new DB3RecordWriter<DB3Dialect, DB3Access>(this.dialect, outputStream, charset,
                array.getFields());
    }
}
