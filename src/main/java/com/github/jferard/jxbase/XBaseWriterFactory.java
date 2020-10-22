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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.GenericWriter;
import com.github.jferard.jxbase.writer.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.XBaseInternalWriterFactory;
import com.github.jferard.jxbase.writer.XBaseMetadataWriter;
import com.github.jferard.jxbase.writer.XBaseOptionalWriter;
import com.github.jferard.jxbase.writer.XBaseRecordWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/**
 * A factory for writers
 * @param <D> The dialect
 * @param <A> The access
 */
public class XBaseWriterFactory<D extends XBaseDialect<D, A>, A> {
    /**
     * Create a new writer.
     *
     * @param type              the type/flavour of the dbf file
     * @param tableName         the name of the table (without .dbf)
     * @param charset           the charset
     * @param meta              the meta information as a map
     * @param fields            list of fields
     * @param optional          optional 263 bytes data. Might contain the relative path of a DBC
     *                          file
     * @param memoHeaderMeta    the memo meta information as a map
     * @param <E>               a dialect.
     * @param <F>               an access.
     * @return                  a writer on the table.
     * @throws IOException
     */
    public static <E extends XBaseDialect<E, F>, F> XBaseWriter createWriter(
            final XBaseFileTypeEnum type, final String tableName, final Charset charset,
            final Map<String, Object> meta, final Collection<XBaseField<? super F>> fields,
            final XBaseOptional optional, final Map<String, Object> memoHeaderMeta) throws IOException {
        return new XBaseWriterFactory<E, F>()
                .create(type, tableName, charset, meta, fields, optional, memoHeaderMeta);
    }

    private XBaseWriter create(final XBaseFileTypeEnum type, final String tableName,
                              final Charset charset, final Map<String, Object> meta,
                              final Collection<XBaseField<? super A>> fields,
                              final XBaseOptional optional, final Map<String, Object> memoHeaderMeta)
            throws IOException {
        final D dialect = (D) DialectFactory
                .getDialect(type, tableName, JxBaseUtils.UTF8_CHARSET, memoHeaderMeta);
        final XBaseInternalWriterFactory<D, A> writerFactory =
                dialect.getInternalWriterFactory(tableName, charset, memoHeaderMeta);
        final File dbfFile = new File(tableName + ".dbf");
        dbfFile.delete();

        final RandomAccessFile file = new RandomAccessFile(dbfFile, "rw");
        final OutputStream out = new BufferedOutputStream(new FileOutputStream(file.getFD()));

        final XBaseFieldDescriptorArray<A> array = this.getFieldDescriptorArray(dialect, fields);
        final XBaseMetadata initialMetadata = this.getInitialMetadata(type, dialect, meta, array);

        final XBaseMetadataWriter<D, A> metadataWriter =
                this.writeHeader(dialect, file, out, charset, writerFactory, initialMetadata, array,
                        optional);
        final XBaseRecordWriter<D> recordWriter =
                writerFactory.createRecordWriter(out, charset, initialMetadata, array, optional);
        return new GenericWriter<D, A>(metadataWriter, recordWriter);
    }

    private XBaseMetadataWriter<D, A> writeHeader(final D dialect, final RandomAccessFile file,
                                               final OutputStream out, final Charset charset,
                                               final XBaseInternalWriterFactory<D, A> writerFactory,
                                               final XBaseMetadata initialMetadata,
                                               final XBaseFieldDescriptorArray<A> array,
                                               final XBaseOptional optional) throws IOException {
        final XBaseMetadataWriter<D, A> metadataWriter =
                writerFactory.createMetadataWriter(file, out, charset);
        metadataWriter.write(initialMetadata);
        final XBaseFieldDescriptorArrayWriter<A> fieldDescriptorArrayWriter =
                writerFactory.createFieldDescriptorArrayWriter(out, initialMetadata);
        fieldDescriptorArrayWriter.write(array);
        final XBaseOptionalWriter<D> optionalWriter =
                writerFactory.createOptionalWriter(out, initialMetadata, array);
        optionalWriter.write(optional);
        return metadataWriter;
    }

    private XBaseMetadata getInitialMetadata(final XBaseFileTypeEnum type, final D dialect,
                                             final Map<String, Object> meta,
                                             final XBaseFieldDescriptorArray<A> array) {
        final int optionalLength = dialect.getOptionalLength();
        final int metaLength = dialect.getMetaDataLength();
        final int fullHeaderLength = metaLength + array.getArrayLength() + optionalLength;
        return new GenericMetadata(type.toByte(), fullHeaderLength, array.getRecordLength(), meta);
    }

    private XBaseFieldDescriptorArray<A> getFieldDescriptorArray(final D dialect,
                                                                 final Collection<XBaseField<?
                                                                            super A>> fields) {
        final int arrayLength = fields.size() * dialect.getFieldDescriptorLength() + 1;
        final int oneRecordLength = this.calculateOneRecordLength(fields, dialect);
        return new GenericFieldDescriptorArray<A>(fields, arrayLength, oneRecordLength);
    }

    private int calculateOneRecordLength(final Iterable<XBaseField<? super A>> fields,
                                         final D dialect) {
        int result = 0;
        for (final XBaseField<? super A> field : fields) {
            result += field.getValueByteLength(dialect.getAccess());
        }
        return result + 1;
    }
}
