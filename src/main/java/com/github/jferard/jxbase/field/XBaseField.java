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

package com.github.jferard.jxbase.field;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A field. Independent of the dialect
 */
public interface XBaseField<A> {
    String getName();

    /**
     * @return the length of the field (the actual length, not necessarily the third field)
     */
    int getValueLength(A access);

    /**
     * Extract the value from the buffer. Delegates the job to the access
     * @param access the access
     * @param recordBuffer the buffer
     * @param offset offset of the value in the buffer
     * @return the value.
     * @throws IOException
     */
    Object extractValue(A access, byte[] recordBuffer, int offset)
            throws IOException;

    /**
     * Write the value. Delegates the job to the access.
     * @param access the access
     * @param out the output stream
     * @param value the value to write.
     * @throws IOException
     */
    void writeValue(A access, OutputStream out, Object value) throws IOException;

    /**
     * @param access the access
     * @return the string representation of the field: name,type,len,num
     */
    String toStringRepresentation(A access);

    /**
     * @param access the access
     * @return the representation.
     */
    FieldRepresentation toRepresentation(A access);
}
