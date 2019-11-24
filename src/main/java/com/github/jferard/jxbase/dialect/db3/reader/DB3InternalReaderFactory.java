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

package com.github.jferard.jxbase.dialect.db3.reader;

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.dialect.db2.reader.DB2OptionalReader;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.reader.internal.XBaseFieldDescriptorArrayReader;
import com.github.jferard.jxbase.reader.internal.XBaseInternalReaderFactory;
import com.github.jferard.jxbase.reader.internal.XBaseMetadataReader;
import com.github.jferard.jxbase.reader.internal.XBaseOptionalReader;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.TimeZone;

/**
 * A factory to create internal readers.
 */
public class DB3InternalReaderFactory implements XBaseInternalReaderFactory<DB3Dialect, DB3Access> {
    protected final DB3Dialect dialect;
    protected final TimeZone timezone;

    public DB3InternalReaderFactory(final DB3Dialect dialect, final TimeZone timezone) {
        this.dialect = dialect;
        this.timezone = timezone;
    }

    public XBaseMetadataReader createMetadataReader(final InputStream inputStream) {
        return new DB3MetadataReader(this.dialect, inputStream);
    }

    public XBaseFieldDescriptorArrayReader<DB3Dialect, DB3Access> createFieldDescriptorArrayReader(
            final InputStream inputStream, final XBaseMetadata metadata) {
        return new DB3FieldDescriptorArrayReader<DB3Dialect, DB3Access>(this.dialect, inputStream, metadata);
    }

    public XBaseRecordReader createRecordReader(final InputStream inputStream,
                                                final Charset charset, final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray<DB3Access> array,
                                                final Object optional) {
        return new DB3RecordReader<DB3Access>(this.dialect.getAccess(), inputStream, charset, array, this.timezone);
    }

    public XBaseOptionalReader createOptionalReader(final InputStream inputStream,
                                                    final Charset charset,
                                                    final XBaseMetadata metadata,
                                                    final XBaseFieldDescriptorArray<DB3Access> array) {
        return new DB2OptionalReader<DB3Dialect, DB3Access>(this.dialect, inputStream, metadata, array);
    }
}
