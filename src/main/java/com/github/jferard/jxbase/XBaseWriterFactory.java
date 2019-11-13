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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.core.XBaseLengths;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.writer.GenericWriter;
import com.github.jferard.jxbase.writer.internal.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.internal.XBaseInternalWriterFactory;
import com.github.jferard.jxbase.writer.internal.XBaseMetadataWriter;
import com.github.jferard.jxbase.writer.internal.XBaseOptionalWriter;
import com.github.jferard.jxbase.writer.internal.XBaseRecordWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

public class XBaseWriterFactory {
    public static XBaseWriter createWriter(final XBaseFileTypeEnum type,
                                             final String databaseName, final Charset charset,
                                             final Map<String, Object> meta,
                                             final Collection<XBaseField> fields,
                                             final XBaseOptional optional) throws IOException {
        return new XBaseWriterFactory().create(type, databaseName, charset, meta, fields, optional);
    }

    public XBaseWriter create(final XBaseFileTypeEnum type, final String databaseName,
                                final Charset charset, final Map<String, Object> meta,
                                final Collection<XBaseField> fields, final XBaseOptional optional)
            throws IOException {
        final XBaseDialect dialect = XBaseFileTypeEnum.getDialect(type);
        final XBaseInternalWriterFactory writerFactory =
                dialect.getInternalWriterFactory(databaseName, charset);
        final File dbfFile = new File(databaseName + ".dbf");

        final RandomAccessFile file = new RandomAccessFile(dbfFile, "rw");
        final OutputStream out = new BufferedOutputStream(new FileOutputStream(file.getFD()));

        final XBaseFieldDescriptorArray array = this.getFieldDescriptorArray(dialect, fields);
        final XBaseMetadata initialMetadata = this.getInitialMetadata(type, dialect, meta, array);

        final XBaseMetadataWriter metadataWriter =
                this.writeHeader(dialect, file, out, charset, writerFactory, initialMetadata, array,
                        optional);
        final XBaseRecordWriter recordWriter = writerFactory
                .createRecordWriter(dialect, out, charset, initialMetadata, array, optional);
        return new GenericWriter(metadataWriter, recordWriter);
    }

    private XBaseMetadataWriter writeHeader(final XBaseDialect dialect, final RandomAccessFile file,
                                            final OutputStream out, final Charset charset,
                                            final XBaseInternalWriterFactory writerFactory,
                                            final XBaseMetadata initialMetadata,
                                            final XBaseFieldDescriptorArray array,
                                            final XBaseOptional optional) throws IOException {
        final XBaseMetadataWriter metadataWriter =
                writerFactory.createMetadataWriter(dialect, file, out, charset);
        metadataWriter.write(initialMetadata);
        final XBaseFieldDescriptorArrayWriter fieldDescriptorArrayWriter =
                writerFactory.createFieldDescriptorArrayWriter(dialect, out, initialMetadata);
        fieldDescriptorArrayWriter.write(array);
        final XBaseOptionalWriter optionalWriter =
                writerFactory.createOptionalWriter(dialect, out, initialMetadata, array);
        optionalWriter.write(optional);
        return metadataWriter;
    }

    private XBaseMetadata getInitialMetadata(final XBaseFileTypeEnum type,
                                             final XBaseDialect dialect,
                                             final Map<String, Object> meta,
                                             final XBaseFieldDescriptorArray array) {
        final int optionalLength = dialect.getOptionalLength();
        final int metaLength = dialect.getMetaDataLength();
        final int fullHeaderLength = metaLength + array.getArrayLength() + optionalLength;
        return new GenericMetadata(type.toByte(), fullHeaderLength, array.getRecordLength(), meta);
    }

    private XBaseFieldDescriptorArray getFieldDescriptorArray(final XBaseDialect dialect,
                                                              final Collection<XBaseField> fields) {
        final int arrayLength = fields.size() * dialect.getFieldDescriptorLength() + 1;
        final int oneRecordLength = this.calculateOneRecordLength(fields, dialect);
        return new GenericFieldDescriptorArray(fields, arrayLength, oneRecordLength);
    }

    private int calculateOneRecordLength(final Iterable<XBaseField> fields,
                                         final XBaseLengths dialect) {
        int result = 0;
        for (final XBaseField field : fields) {
            result += field.getValueByteLength(dialect);
        }
        return result + 1;
    }
}
