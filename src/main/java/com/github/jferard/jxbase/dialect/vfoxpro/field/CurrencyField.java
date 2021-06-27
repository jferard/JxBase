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

package com.github.jferard.jxbase.dialect.vfoxpro.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A currency field.
 */
public class CurrencyField implements XBaseField<CurrencyAccess> {
    private final String fieldName;

    public CurrencyField(final String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getName() {
        return this.fieldName;
    }

    @Override
    public int getValueLength(final CurrencyAccess access) {
        return access.getCurrencyValueLength();
    }

    @Override
    public Long extractValue(final CurrencyAccess access, final byte[] recordBuffer,
                             final int offset) {
        return access.extractCurrencyValue(recordBuffer, offset, this.getValueLength(access));
    }

    @Override
    public void writeValue(final CurrencyAccess access, final OutputStream out, final Object value)
            throws IOException {
        access.writeCurrencyValue(out, (Long) value);
    }

    @Override
    public String toStringRepresentation(final CurrencyAccess access) {
        return this.toRepresentation(access).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final CurrencyAccess access) {
        return access.getCurrencyRepresentation(this.fieldName);
    }

    @Override
    public String toString() {
        return "CurrencyField[name=" + this.fieldName + "]";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyField)) {
            return false;
        }

        final CurrencyField that = (CurrencyField) o;
        return this.fieldName.equals(that.fieldName);
    }

    @Override
    public int hashCode() {
        return this.fieldName.hashCode();
    }
}