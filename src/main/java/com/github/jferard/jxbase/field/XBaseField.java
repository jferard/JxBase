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

package com.github.jferard.jxbase.field;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Independent of the dialect
 */
public interface XBaseField<A> {
    String getName();

    /**
     * @return the length of the field (the actual length, not necessarily the third field)
     */
    int getValueByteLength(A access);

    Object getValue(A access, byte[] recordBuffer, int offset, int length)
            throws IOException;

    void writeValue(A access, OutputStream out, Object value) throws IOException;

    String toStringRepresentation(A access);

    FieldRepresentation toRepresentation(A access);
}
