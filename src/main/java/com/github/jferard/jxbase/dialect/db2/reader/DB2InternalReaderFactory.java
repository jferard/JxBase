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

package com.github.jferard.jxbase.dialect.db2.reader;

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.dialect.db2.reader.DB2FieldDescriptorArrayReader;
import com.github.jferard.jxbase.dialect.db2.reader.DB2MetadataReader;
import com.github.jferard.jxbase.dialect.db2.reader.DB2OptionalReader;
import com.github.jferard.jxbase.dialect.db2.reader.DB2RecordReader;
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
public class DB2InternalReaderFactory implements XBaseInternalReaderFactory<DB2Dialect, DB2Access> {
    protected final DB2Dialect dialect;
    protected final TimeZone timezone;

    public DB2InternalReaderFactory(final DB2Dialect dialect, final TimeZone timezone) {
        this.dialect = dialect;
        this.timezone = timezone;
    }

    public XBaseMetadataReader createMetadataReader(final InputStream inputStream) {
        return new DB2MetadataReader(this.dialect, inputStream);
    }

    public XBaseFieldDescriptorArrayReader<DB2Dialect, DB2Access> createFieldDescriptorArrayReader(
            final InputStream inputStream, final XBaseMetadata metadata) {
        return new DB2FieldDescriptorArrayReader<DB2Dialect, DB2Access>(this.dialect, inputStream,
                metadata);
    }

    public XBaseRecordReader createRecordReader(final InputStream inputStream,
                                                final Charset charset, final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray<DB2Dialect,
                                                        DB2Access> array,
                                                final Object optional) {
        return new DB2RecordReader<DB2Dialect, DB2Access>(this.dialect, inputStream, charset,
                array);
    }

    public XBaseOptionalReader createOptionalReader(final InputStream inputStream,
                                                    final Charset charset,
                                                    final XBaseMetadata metadata,
                                                    final XBaseFieldDescriptorArray<DB2Dialect,
                                                            DB2Access> array) {
        return new DB2OptionalReader(this.dialect, inputStream, metadata, array);
    }
}
