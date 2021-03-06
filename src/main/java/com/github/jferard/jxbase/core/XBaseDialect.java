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

package com.github.jferard.jxbase.core;

import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.reader.XBaseChunkReaderFactory;
import com.github.jferard.jxbase.writer.XBaseChunkWriterFactory;

/**
 * A dialect.
 *
 * @param <D> the dialect
 * @param <A> the access
 */
public interface XBaseDialect<A extends XBaseAccess, D extends XBaseDialect<A, D>> {
    /**
     * Create a field. The field depends on the dialect (e.g. memo fields dialect dependent).
     *
     * @param name                  name of the field
     * @param typeByte              the type byte of the field ('C' for char...)
     * @param length                the given length of the field
     * @param numberOfDecimalPlaces the given number of decimal places
     * @return the field.
     */
    XBaseField<? super A> createXBaseField(String name, byte typeByte, int length,
                                           int numberOfDecimalPlaces);

    /**
     * @return the type of the dialect file
     */
    XBaseFileTypeEnum getType();

    /**
     * @return the length of the meta data
     */
    int getMetaDataLength();

    /**
     * @return the length of descriptor of *one* field.
     */
    int getFieldDescriptorLength();

    /**
     * @return the length of the optional zone after the header.
     */
    int getOptionalLength();

    /**
     * @return the access to data.
     */
    A getAccess();

    XBaseChunkReaderFactory<A, D> getInternalReaderFactory();

    XBaseChunkWriterFactory<A, D> getInternalWriterFactory();
}
