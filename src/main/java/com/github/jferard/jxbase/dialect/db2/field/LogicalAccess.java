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

package com.github.jferard.jxbase.dialect.db2.field;

import com.github.jferard.jxbase.field.FieldRepresentation;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Access (read/write) to a logical field.
 */
public interface LogicalAccess {
    /**
     * @return the length in byte of the value
     */
    int getLogicalValueLength();

    /**
     * @param recordBuffer the record buffer
     * @param offset the offset
     * @param length the length
     * @return the value, true or false
     */
    Boolean extractLogicalValue(byte[] recordBuffer, int offset, int length);

    /**
     * @param fieldName the field name
     * @return the field representation
     */
    FieldRepresentation getLogicalFieldRepresentation(String fieldName);

    /**
     * Write a value.
     * @param out the output stream
     * @param value the value
     * @throws IOException
     */
    void writeLogicalValue(OutputStream out, Boolean value) throws IOException;
}
