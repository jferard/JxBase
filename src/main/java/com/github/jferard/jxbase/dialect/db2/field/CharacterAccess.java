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

package com.github.jferard.jxbase.dialect.db2.field;

import com.github.jferard.jxbase.field.FieldRepresentation;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Access (read/write) to a character field
 */
public interface CharacterAccess {
    /**
     * @param fieldLength the "length" in the array descriptor
     * @return the actual length
     */
    int getCharacterValueLength(int fieldLength);

    /**
     * @param recordBuffer the buffer that contains the record
     * @param offset       the offset
     * @param length       the actual length
     * @return the value as a string
     */
    String extractCharacterValue(byte[] recordBuffer, int offset, int length);

    /**
     * @param out    the output stream
     * @param value  the value
     * @param length the length of data in record
     * @throws IOException
     */
    void writeCharacterValue(OutputStream out, String value, int length) throws IOException;

    /**
     * @param fieldName   the field name
     * @param fieldLength the "length" in array descriptor
     * @return the representation
     */
    FieldRepresentation getCharacterFieldRepresentation(String fieldName, int fieldLength);
}
