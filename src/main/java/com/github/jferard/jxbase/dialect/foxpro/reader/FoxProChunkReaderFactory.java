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

package com.github.jferard.jxbase.dialect.foxpro.reader;

import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db2.reader.DB2OptionalReader;
import com.github.jferard.jxbase.dialect.db3.reader.DB3FieldDescriptorArrayReader;
import com.github.jferard.jxbase.dialect.db3.reader.DB3RecordReader;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.reader.XBaseFieldDescriptorArrayReader;
import com.github.jferard.jxbase.reader.XBaseChunkReaderFactory;
import com.github.jferard.jxbase.reader.XBaseMetadataReader;
import com.github.jferard.jxbase.reader.XBaseOptionalReader;
import com.github.jferard.jxbase.reader.XBaseRecordReader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.TimeZone;

/**
 * A factory for foxpro chunks readers.
 */
public class FoxProChunkReaderFactory implements
        XBaseChunkReaderFactory<FoxProDialect, DB4Access> {
    protected final FoxProDialect dialect;
    protected final TimeZone timezone;

    public FoxProChunkReaderFactory(final FoxProDialect dialect, final TimeZone timezone) {
        this.dialect = dialect;
        this.timezone = timezone;
    }

    @Override
    public XBaseMetadataReader createMetadataReader(final InputStream inputStream) {
        return new FoxProMetadataReader(this.dialect, inputStream);
    }

    @Override
    public XBaseFieldDescriptorArrayReader<FoxProDialect, DB4Access> createFieldDescriptorArrayReader(
            final InputStream inputStream, final XBaseMetadata metadata) {
        return new DB3FieldDescriptorArrayReader<FoxProDialect, DB4Access>(this.dialect, inputStream,
                metadata);
    }

    @Override
    public XBaseRecordReader createRecordReader(final InputStream inputStream,
                                                final Charset charset, final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray<DB4Access> array,
                                                final Object optional) {
        return new DB3RecordReader<DB4Access>(this.dialect.getAccess(), inputStream, charset, array,
                this.timezone);
    }

    @Override
    public XBaseOptionalReader createOptionalReader(final InputStream inputStream,
                                                    final Charset charset,
                                                    final XBaseMetadata metadata,
                                                    final XBaseFieldDescriptorArray<DB4Access> array) {
        // no optional here.
        return new DB2OptionalReader<FoxProDialect, DB4Access>(this.dialect, inputStream, metadata, array);
    }
}
