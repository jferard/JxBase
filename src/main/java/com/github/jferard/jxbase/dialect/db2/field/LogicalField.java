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
import com.github.jferard.jxbase.field.XBaseField;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A logical field : true/false.
 */
public class LogicalField implements XBaseField<LogicalAccess> {
    private final String name;

    public LogicalField(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValueLength(final LogicalAccess access) {
        return access.getLogicalValueLength();
    }

    @Override
    public Boolean extractValue(final LogicalAccess access, final byte[] recordBuffer,
                                final int offset) {
        return access.extractLogicalValue(recordBuffer, offset, this.getValueLength(access));
    }

    @Override
    public void writeValue(final LogicalAccess access, final OutputStream out, final Object value)
            throws IOException {
        access.writeLogicalValue(out, (Boolean) value);
    }

    @Override
    public String toStringRepresentation(final LogicalAccess access) {
        return access.getLogicalFieldRepresentation(this.name).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final LogicalAccess access) {
        return access.getLogicalFieldRepresentation(this.name);
    }

    @Override
    public String toString() {
        return "LogicalField[name=" + this.name + "]";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LogicalField)) {
            return false;
        }

        final LogicalField that = (LogicalField) o;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
