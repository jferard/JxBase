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

package com.github.jferard.jxbase.dialect.basic;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.reader.internal.GenericFieldDescriptorArrayReader;
import com.github.jferard.jxbase.reader.internal.GenericMetadataReader;
import com.github.jferard.jxbase.reader.internal.GenericOptionalReader;
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
public class BasicInternalReaderFactory implements XBaseInternalReaderFactory {
    protected final XBaseDialect dialect;
    protected final TimeZone timezone;

    public BasicInternalReaderFactory(final XBaseDialect dialect, final TimeZone timezone) {
        this.dialect = dialect;
        this.timezone = timezone;
    }

    public XBaseMetadataReader createMetadataReader(final InputStream inputStream) {
        return new GenericMetadataReader(this.dialect, inputStream);
    }

    public XBaseFieldDescriptorArrayReader createFieldDescriptorArrayReader(
            final InputStream inputStream, final XBaseMetadata metadata) {
        return new GenericFieldDescriptorArrayReader(this.dialect, inputStream, metadata);
    }

    public XBaseRecordReader createRecordReader(final InputStream inputStream,
                                                final Charset charset, final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray array,
                                                final Object optional) {
        return new BasicRecordReader(this.dialect, inputStream, charset, array, this.timezone);
    }

    public XBaseOptionalReader createOptionalReader(final InputStream inputStream,
                                                    final Charset charset,
                                                    final XBaseMetadata metadata,
                                                    final XBaseFieldDescriptorArray array) {
        return new GenericOptionalReader(this.dialect, inputStream, metadata, array);
    }
}
