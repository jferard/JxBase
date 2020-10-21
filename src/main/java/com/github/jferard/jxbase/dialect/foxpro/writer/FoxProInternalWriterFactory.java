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

package com.github.jferard.jxbase.dialect.foxpro.writer;

import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db3.writer.DB3FieldDescriptorArrayWriter;
import com.github.jferard.jxbase.dialect.db3.writer.DB3RecordWriter;
import com.github.jferard.jxbase.dialect.db4.writer.DB4MetadataWriter;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.writer.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.XBaseInternalWriterFactory;
import com.github.jferard.jxbase.writer.XBaseMetadataWriter;
import com.github.jferard.jxbase.writer.XBaseOptionalWriter;
import com.github.jferard.jxbase.writer.XBaseRecordWriter;

import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.TimeZone;

public class FoxProInternalWriterFactory
        implements XBaseInternalWriterFactory<FoxProDialect, VisualFoxProAccess> {
    private final FoxProDialect dialect;
    private final TimeZone timeZone;

    public FoxProInternalWriterFactory(final FoxProDialect dialect, final TimeZone timeZone) {
        this.dialect = dialect;
        this.timeZone = timeZone;
    }

    @Override
    public XBaseMetadataWriter<FoxProDialect, VisualFoxProAccess> createMetadataWriter(
            final RandomAccessFile file,
            final OutputStream outputStream,
            final Charset charset) {
        return new DB4MetadataWriter<FoxProDialect, VisualFoxProAccess>(this.dialect, file,
                outputStream, charset);
    }

    @Override
    public XBaseFieldDescriptorArrayWriter<VisualFoxProAccess> createFieldDescriptorArrayWriter(
            final OutputStream outputStream, final XBaseMetadata metadata) {
        return new DB3FieldDescriptorArrayWriter<VisualFoxProAccess>(this.dialect.getAccess(),
                outputStream);
    }

    @Override
    public XBaseOptionalWriter<FoxProDialect> createOptionalWriter(final OutputStream outputStream,
                                                                   final XBaseMetadata metadata,
                                                                   final XBaseFieldDescriptorArray<VisualFoxProAccess> array) {
        return new FoxProOptionalWriter<FoxProDialect, VisualFoxProAccess>(this.dialect,
                outputStream, metadata, array);
    }

    @Override
    public XBaseRecordWriter<FoxProDialect> createRecordWriter(final OutputStream outputStream,
                                                               final Charset charset,
                                                               final XBaseMetadata metadata,
                                                               final XBaseFieldDescriptorArray<VisualFoxProAccess> array,
                                                               final Object optional) {
        return new DB3RecordWriter<FoxProDialect, VisualFoxProAccess>(this.dialect, outputStream,
                charset,
                array.getFields());
    }
}
