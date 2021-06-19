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
import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.GenericWriter;
import com.github.jferard.jxbase.writer.XBaseChunkWriterFactory;
import com.github.jferard.jxbase.writer.XBaseFieldDescriptorArrayWriter;
import com.github.jferard.jxbase.writer.XBaseMetadataWriter;
import com.github.jferard.jxbase.writer.XBaseOptionalWriter;
import com.github.jferard.jxbase.writer.XBaseRecordWriter;
import org.powermock.reflect.exceptions.FieldNotFoundException;

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
 *
 * @param <A> The access
 * @param <D> The dialect
 */
public class XBaseWriterFactory<A extends XBaseAccess, D extends XBaseDialect<A, D>> {
    /**
     * Create a new writer.
     *
     * @param type           the type/flavour of the dbf file
     * @param tableName      the name of the table (without .dbf)
     * @param charset        the charset
     * @param meta           the meta information as a map
     * @param fields         list of fields
     * @param optional       optional 263 bytes data. Might contain the relative path of a DBC
     *                       file
     * @param memoHeaderMeta the memo meta information as a map
     * @param <E>            a dialect.
     * @param <F>            an access.
     * @return a writer on the table.
     * @throws IOException
     */
    public static <F extends XBaseAccess, E extends XBaseDialect<F, E>> XBaseWriter createWriter(
            final XBaseFileTypeEnum type, final String tableName, final Charset charset,
            final Map<String, Object> meta, final Collection<XBaseField<? super F>> fields,
            final XBaseOptional optional, final Map<String, Object> memoHeaderMeta)
            throws IOException {
        return new XBaseWriterFactory<F, E>()
                .create(type, tableName, charset, meta, fields, optional, memoHeaderMeta);
    }

    private XBaseWriter create(final XBaseFileTypeEnum type, final String tableName,
                               final Charset charset, final Map<String, Object> meta,
                               final Collection<XBaseField<? super A>> fields,
                               final XBaseOptional optional,
                               final Map<String, Object> memoHeaderMeta)
            throws IOException {
        @SuppressWarnings("unchecked") final D dialect = (D) DialectFactory
                .getDialect(type, JxBaseUtils.UTF8_CHARSET);
        final XBaseChunkWriterFactory<A, D> writerFactory =
                dialect.getInternalWriterFactory();
        final File dbfFile = new File(tableName + ".dbf");
        dbfFile.delete();

        final RandomAccessFile file = new RandomAccessFile(dbfFile, "rw");
        final OutputStream out = new BufferedOutputStream(new FileOutputStream(file.getFD()));

        final XBaseFieldDescriptorArray<A> array = this.getFieldDescriptorArray(dialect, fields);
        final XBaseMetadata initialMetadata = this.getInitialMetadata(type, dialect, meta, array);

        final XBaseMetadataWriter<A, D> metadataWriter =
                this.writeHeader(dialect, file, out, charset, writerFactory, initialMetadata, array,
                        optional);
        XBaseMemoWriter memoWriter;
        try {
            memoWriter = writerFactory.createMemoWriter(type, tableName, memoHeaderMeta);
        } catch (final FieldNotFoundException e) {
            memoWriter = null;
        }
        final XBaseRecordWriter<D> recordWriter =
                writerFactory.createRecordWriter(out, charset, initialMetadata, array,
                        memoWriter, optional);
        return new GenericWriter<A, D>(metadataWriter, recordWriter);
    }

    private XBaseMetadataWriter<A, D> writeHeader(final D dialect, final RandomAccessFile file,
                                                  final OutputStream out, final Charset charset,
                                                  final XBaseChunkWriterFactory<A, D> writerFactory,
                                                  final XBaseMetadata initialMetadata,
                                                  final XBaseFieldDescriptorArray<A> array,
                                                  final XBaseOptional optional) throws IOException {
        final XBaseMetadataWriter<A, D> metadataWriter =
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
            result += field.getValueLength(dialect.getAccess());
        }
        return result + 1;
    }
}
