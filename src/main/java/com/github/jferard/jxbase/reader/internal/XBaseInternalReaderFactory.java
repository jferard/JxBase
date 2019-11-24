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

package com.github.jferard.jxbase.reader.internal;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;

import java.io.InputStream;
import java.nio.charset.Charset;

public interface XBaseInternalReaderFactory<D extends XBaseDialect<D, A>, A> {
    XBaseMetadataReader createMetadataReader(InputStream inputStream);

    XBaseFieldDescriptorArrayReader<D, A> createFieldDescriptorArrayReader(InputStream inputStream,
                                                                        XBaseMetadata metadata);

    XBaseOptionalReader createOptionalReader(InputStream inputStream, Charset charset,
                                                XBaseMetadata metadata,
                                                XBaseFieldDescriptorArray<A> array);

    XBaseRecordReader createRecordReader(InputStream inputStream, Charset charset,
                                         XBaseMetadata metadata, XBaseFieldDescriptorArray<A> array,
                                         Object optional);
}
