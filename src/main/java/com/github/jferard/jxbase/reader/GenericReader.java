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

package com.github.jferard.jxbase.reader;

import com.github.jferard.jxbase.XBaseReader;
import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.core.XBaseRecord;
import com.github.jferard.jxbase.memo.XBaseMemoReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;

/**
 * A generic reader: combination of various readers.
 * Instead of reading on demand, everything is initialized at beginning.
 *
 * @param <D> the dialect
 * @param <A> the access
 */
public class GenericReader<A extends XBaseAccess, D extends XBaseDialect<A, D>>
        implements XBaseReader<A, D> {
    private final XBaseFieldDescriptorArray<A> array;
    private final XBaseRecordReader recordReader;
    private final XBaseOptional optional;
    private final D dialect;
    private final InputStream inputStream;
    private final XBaseMetadata metadata;

    /**
     * Warning: read some data.
     *
     * @param dialect       the dialect
     * @param tableName
     * @param inputStream   the input stream
     * @param charset       the charset
     * @param readerFactory the chunk reader factory
     * @throws IOException
     */
    public GenericReader(final D dialect, final String tableName, final InputStream inputStream,
                         final Charset charset,
                         final XBaseChunkReaderFactory<A, D> readerFactory) throws IOException {
        this.dialect = dialect;
        this.inputStream = inputStream;
        this.metadata = readerFactory.createMetadataReader(inputStream).read();
        this.array =
                readerFactory.createFieldDescriptorArrayReader(inputStream, this.metadata).read();
        this.optional =
                readerFactory.createOptionalReader(inputStream, charset, this.metadata, this.array)
                        .read();
        XBaseMemoReader memoReader;
        try {
            memoReader =
                    readerFactory.createMemoReader(dialect.getType(), tableName, charset);
        } catch (final FileNotFoundException e) {
            memoReader = null;
        }
        this.recordReader = readerFactory
                .createRecordReader(inputStream, charset, memoReader, this.metadata, this.array,
                        this.optional);
        this.checkLengths();
    }

    private void checkLengths() throws IOException {
        final int metaLength = this.dialect.getMetaDataLength();
        final int arrayLength = this.array.getArrayLength();
        final int optionalLength = this.optional.getLength();
        final int fullHeaderLength = this.metadata.getFullHeaderLength();

        final int actualHeaderLength =
                arrayLength + metaLength + optionalLength;
        if (actualHeaderLength != fullHeaderLength) {
            throw new IOException(
                    String.format("Bad header length: expected: %s, actual: %s (meta) + %s (array) + %s (opt)",
                            fullHeaderLength, metaLength, arrayLength, optionalLength));
        }
    }

    @Override
    public D getDialect() {
        return this.dialect;
    }

    @Override
    public XBaseMetadata getMetadata() {
        return this.metadata;
    }

    @Override
    public XBaseFieldDescriptorArray<A> getFieldDescriptorArray() {
        return this.array;
    }

    @Override
    public XBaseOptional getOptional() {
        return this.optional;
    }

    @Override
    public XBaseRecord read() throws IOException, ParseException {
        return this.recordReader.read();
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
        this.recordReader.close();
    }
}
