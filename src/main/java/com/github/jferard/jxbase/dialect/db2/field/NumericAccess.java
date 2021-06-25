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
import java.math.BigDecimal;

/**
 * Access (read/write) to a numeric field.
 */
public interface NumericAccess {
    /**
     * @param fieldLength the "length"
     * @return the actual length
     */
    int getNumericValueLength(int fieldLength);

    /**
     * @param recordBuffer          the record buffer
     * @param offset                the offset
     * @param length                the actual length
     * @param numberOfDecimalPlaces the actual number of decimal places
     * @return the value as BigDecimal
     */
    BigDecimal extractNumericValue(byte[] recordBuffer, int offset, int length,
                                   int numberOfDecimalPlaces);

    /**
     * @param out                   the output stream
     * @param value                 the value to write
     * @param length                the actual length
     * @param numberOfDecimalPlaces the actual number of decimal places
     * @throws IOException
     */
    void writeNumericValue(OutputStream out, BigDecimal value, int length,
                           int numberOfDecimalPlaces)
            throws IOException;

    /**
     * @param fieldName                  the field name
     * @param fieldLength                the field length
     * @param fieldNumberOfDecimalPlaces the field number of decimal places
     * @return the representation
     */
    FieldRepresentation getNumericFieldRepresentation(String fieldName, int fieldLength,
                                                      int fieldNumberOfDecimalPlaces);
}
