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

package com.github.jferard.jxbase.dialect.basic;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.writer.internal.GenericFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.internal.GenericMetadataWriter;
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

public class BasicInternalWriterFactory implements XBaseInternalWriterFactory {
    public BasicInternalWriterFactory(final XBaseDialect dialect, TimeZone aDefault) {
    }

    @Override
    public XBaseMetadataWriter createMetadataWriter(final XBaseDialect dialect,
                                                    final RandomAccessFile file,
                                                    final OutputStream outputStream,
                                                    final Charset charset) {
        return new GenericMetadataWriter(dialect, file, outputStream, charset);
    }

    @Override
    public XBaseFieldDescriptorArrayWriter createFieldDescriptorArrayWriter(
            final XBaseDialect dialect, final OutputStream outputStream,
            final XBaseMetadata metadata) {
        return new GenericFieldDescriptorArrayWriter(dialect, outputStream);
    }

    @Override
    public XBaseOptionalWriter createOptionalWriter(final XBaseDialect dialect,
                                                    final OutputStream outputStream,
                                                    final XBaseMetadata metadata,
                                                    final XBaseFieldDescriptorArray array) {
        return new GenericOptionalWriter(dialect, outputStream, metadata, array);
    }

    @Override
    public XBaseRecordWriter createRecordWriter(final XBaseDialect dialect,
                                                final OutputStream outputStream,
                                                final Charset charset, final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray array,
                                                final Object optional) {
        return new BasicRecordWriter(dialect, outputStream, charset, array.getFields());
    }
}
