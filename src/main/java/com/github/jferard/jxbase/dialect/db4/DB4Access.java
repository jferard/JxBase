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

package com.github.jferard.jxbase.dialect.db4;

import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericAccess;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.field.DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.dialect.db4.field.FloatAccess;
import com.github.jferard.jxbase.field.FieldRepresentation;

import java.io.IOException;
import java.io.OutputStream;

public class DB4Access extends DB3Access implements CDFLMNFieldsAccess {
    private final FloatAccess floatAccess;

    public DB4Access(final CharacterAccess characterAccess, final DateAccess dateAccess,
                     final FloatAccess floatAccess, final LogicalAccess logicalAccess,
                     final MemoAccess memoAccess, final NumericAccess numericAccess) {
        super(characterAccess, dateAccess, logicalAccess, memoAccess, numericAccess);
        this.floatAccess = floatAccess;
    }

    @Override
    public int getFloatValueLength() {
        return this.floatAccess.getFloatValueLength();
    }

    @Override
    public Object extractFloatValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.floatAccess.extractFloatValue(recordBuffer, offset, length);
    }

    @Override
    public void writeFloatValue(final OutputStream out, final Object value) throws IOException {
        this.floatAccess.writeFloatValue(out, value);
    }

    @Override
    public FieldRepresentation getFloatFieldRepresentation(final String name) {
        return this.floatAccess.getFloatFieldRepresentation(name);
    }
}
