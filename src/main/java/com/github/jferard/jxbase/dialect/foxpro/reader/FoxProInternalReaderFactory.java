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

package com.github.jferard.jxbase.dialect.foxpro.reader;

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.dialect.db3.reader.DB3FieldDescriptorArrayReader;
import com.github.jferard.jxbase.dialect.db3.reader.DB3RecordReader;
import com.github.jferard.jxbase.dialect.foxpro.FoxProAccess;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.reader.internal.XBaseFieldDescriptorArrayReader;
import com.github.jferard.jxbase.reader.internal.XBaseInternalReaderFactory;
import com.github.jferard.jxbase.reader.internal.XBaseMetadataReader;
import com.github.jferard.jxbase.reader.internal.XBaseOptionalReader;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.TimeZone;

public class FoxProInternalReaderFactory implements XBaseInternalReaderFactory<FoxProDialect, FoxProAccess> {
    protected final FoxProDialect dialect;
    protected final TimeZone timezone;

    public FoxProInternalReaderFactory(final FoxProDialect dialect, final TimeZone timezone) {
        this.dialect = dialect;
        this.timezone = timezone;
    }

    public XBaseMetadataReader createMetadataReader(final InputStream inputStream) {
        return new FoxProMetadataReader(this.dialect, inputStream);
    }

    public XBaseFieldDescriptorArrayReader<FoxProDialect, FoxProAccess> createFieldDescriptorArrayReader(
            final InputStream inputStream, final XBaseMetadata metadata) {
        return new DB3FieldDescriptorArrayReader<FoxProDialect, FoxProAccess>(this.dialect, inputStream,
                metadata);
    }

    public XBaseRecordReader createRecordReader(final InputStream inputStream,
                                                final Charset charset, final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray<FoxProAccess> array,
                                                final Object optional) {
        return new DB3RecordReader<FoxProAccess>(this.dialect.getAccess(), inputStream, charset, array,
                this.timezone);
    }

    public XBaseOptionalReader createOptionalReader(final InputStream inputStream,
                                                    final Charset charset,
                                                    final XBaseMetadata metadata,
                                                    final XBaseFieldDescriptorArray<FoxProAccess> array) {
        return new FoxProOptionalReader<FoxProDialect, FoxProAccess>(this.dialect, inputStream, metadata, array);
    }
}
