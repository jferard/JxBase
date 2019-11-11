/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

import com.github.jferard.jxbase.core.GenericRecord;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.reader.internal.XBaseInternalReaderFactory;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.logging.Logger;

public class GenericReader implements XBaseReader<XBaseMemoRecord<Void>> {
    private final XBaseFieldDescriptorArray array;
    private final XBaseRecordReader recordReader;
    private final XBaseOptional optional;
    private final XBaseDialect dialect;
    private final InputStream inputStream;
    private final XBaseMetadata metadata;

    /**
     * @param dialect
     * @param inputStream
     * @param charset
     * @param readerFactory
     * @param memoReader may be null
     * @throws IOException
     */
    public GenericReader(final XBaseDialect dialect, final InputStream inputStream,
                         final Charset charset, final XBaseInternalReaderFactory readerFactory,
                         final XBaseMemoReader memoReader) throws IOException {
        this.dialect = dialect;
        this.inputStream = inputStream;
        this.metadata = readerFactory.createMetadataReader(inputStream).read();
        this.array =
                readerFactory.createFieldDescriptorArrayReader(inputStream, this.metadata).read();
        this.optional =
                readerFactory.createOptionalReader(inputStream, charset, this.metadata, this.array)
                        .read();
        this.recordReader = readerFactory
                .createRecordReader(inputStream, charset, this.metadata, this.array, this.optional,
                        memoReader);
        this.checkLengths();
    }

    private void checkLengths() throws IOException {
        final int metaLength = this.dialect.getMetaDataLength();
        final int actualHeaderLength =
                this.array.getArrayLength() + metaLength + this.optional.getLength();
        if (actualHeaderLength != this.metadata.getFullHeaderLength()) {
            Logger.getLogger(GenericReader.class.getName()).severe(String
                    .format("Bad header length: expected: %s, actual: %s + %s + %s",
                            this.metadata.getFullHeaderLength(), this.array.getArrayLength(),
                            metaLength, this.optional.getLength()));
        }
    }

    @Override
    public XBaseDialect getDialect() {
        return this.dialect;
    }

    @Override
    public XBaseMetadata getMetadata() {
        return this.metadata;
    }

    @Override
    public XBaseFieldDescriptorArray getFieldDescriptorArray() {
        return this.array;
    }

    @Override
    public XBaseOptional getOptional() {
        return this.optional;
    }

    @Override
    public GenericRecord read() throws IOException, ParseException {
        return this.recordReader.read();
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }
}
