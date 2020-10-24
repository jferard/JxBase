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

package com.github.jferard.jxbase.dialect.vfoxpro.field;

import com.github.jferard.jxbase.field.FieldRepresentation;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Access (read/write) to integer fields.
 */
public interface IntegerAccess {
    /**
     * @return the actual length of the value
     */
    int getIntegerValueLength();

    /**
     * Get the integer value
     * @param recordBuffer the record buffer
     * @param offset the offset
     * @param length the length
     * @return the value
     */
    Long extractIntegerValue(byte[] recordBuffer, int offset, int length);

    /**
     * Write the integer value
     * @param out the output stream
     * @param value the value
     * @throws IOException
     */
    void writeIntegerValue(OutputStream out, Long value) throws IOException;

    /**
     * @param fieldName the field name
     * @return the field representation.
     */
    FieldRepresentation getIntegerFieldRepresentation(String fieldName);
}