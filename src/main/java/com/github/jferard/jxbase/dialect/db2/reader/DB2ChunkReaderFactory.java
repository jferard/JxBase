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

package com.github.jferard.jxbase.dialect.db2.reader;

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.reader.XBaseFieldDescriptorArrayReader;
import com.github.jferard.jxbase.reader.XBaseChunkReaderFactory;
import com.github.jferard.jxbase.reader.XBaseMetadataReader;
import com.github.jferard.jxbase.reader.XBaseOptionalReader;
import com.github.jferard.jxbase.reader.XBaseRecordReader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.TimeZone;

/**
 * A factory to create internal readers.
 */
public class DB2ChunkReaderFactory implements XBaseChunkReaderFactory<DB2Access, DB2Dialect> {
    protected final DB2Dialect dialect;
    protected final TimeZone timezone;

    public DB2ChunkReaderFactory(final DB2Dialect dialect, final TimeZone timezone) {
        this.dialect = dialect;
        this.timezone = timezone;
    }

    @Override
    public XBaseMetadataReader createMetadataReader(final InputStream inputStream) {
        return new DB2MetadataReader(this.dialect, inputStream);
    }

    @Override
    public XBaseFieldDescriptorArrayReader<DB2Access, DB2Dialect> createFieldDescriptorArrayReader(
            final InputStream inputStream, final XBaseMetadata metadata) {
        return new DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect>(this.dialect, inputStream
        );
    }

    @Override
    public XBaseRecordReader createRecordReader(final InputStream inputStream,
                                                final Charset charset,
                                                final XBaseMemoReader memoReader,
                                                final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray<DB2Access> array,
                                                final Object optional) {
        return new DB2RecordReader<DB2Access>(this.dialect.getAccess(), inputStream, charset,
                array);
    }

    @Override
    public XBaseMemoReader createMemoReader(final XBaseFileTypeEnum type, final String tableName,
                                            final Charset charset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public XBaseOptionalReader createOptionalReader(final InputStream inputStream,
                                                    final Charset charset,
                                                    final XBaseMetadata metadata,
                                                    final XBaseFieldDescriptorArray<DB2Access> array) {
        return new DB2OptionalReader<DB2Access, DB2Dialect>(this.dialect, inputStream, metadata,
                array);
    }
}
