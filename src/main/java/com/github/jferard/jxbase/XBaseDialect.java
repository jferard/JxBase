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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.reader.XBaseInternalReaderFactory;
import com.github.jferard.jxbase.writer.XBaseInternalWriterFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public interface XBaseDialect<D extends XBaseDialect<D, A>, A> {

    XBaseField<? super A> getXBaseField(String name, byte typeByte, int length,
                                        int numberOfDecimalPlaces);

    XBaseFileTypeEnum getType();

    int getMetaDataLength();

    int getFieldDescriptorLength();

    int getOptionalLength();

    A getAccess();

    XBaseInternalReaderFactory<D, A> getInternalReaderFactory(String databaseName, Charset charset)
            throws IOException;

    XBaseInternalWriterFactory<D, A> getInternalWriterFactory(String databaseName, Charset charset,
                                                           Map<String, Object> headerMeta)
            throws IOException;
}
