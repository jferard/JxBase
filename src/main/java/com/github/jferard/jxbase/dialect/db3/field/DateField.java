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

package com.github.jferard.jxbase.dialect.db3.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * A date field
 */
public class DateField implements XBaseField<DateAccess> {
    private final String name;

    public DateField(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValueLength(final DateAccess access) {
        return access.getDateValueLength();
    }

    @Override
    public Date extractValue(final DateAccess access, final byte[] recordBuffer,
                             final int offset) {
        return access.extractDateValue(recordBuffer, offset, this.getValueLength(access));
    }

    @Override
    public void writeValue(final DateAccess access, final OutputStream out, final Object value) throws IOException {
        access.writeDateValue(out, (Date) value);
    }

    @Override
    public String toStringRepresentation(final DateAccess access) {
        return this.toRepresentation(access).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final DateAccess acces) {
        return acces.getDateFieldRepresentation(this.name);
    }

    @Override
    public String toString() {
        return "DateField[name=" + this.name + "]";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DateField)) {
            return false;
        }

        final DateField that = (DateField) o;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
