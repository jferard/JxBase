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

package com.github.jferard.jxbase.dialect.db4.field;

import com.github.jferard.jxbase.field.FieldRepresentation;

import java.io.OutputStream;

public class DB4FloatAccess implements FloatAccess {
    @Override
    public int getFloatValueLength() {
        return 0;
    }

    @Override
    public Object getFloatValue(final byte[] recordBuffer, final int offset, final int length) {
        return null;
    }

    @Override
    public void writeFloatValue(final OutputStream out, final Object value) {

    }

    @Override
    public FieldRepresentation getFloatFieldRepresentation(final String name) {
        return null;
    }
}
