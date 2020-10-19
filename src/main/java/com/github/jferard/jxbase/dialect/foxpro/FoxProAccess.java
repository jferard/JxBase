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

package com.github.jferard.jxbase.dialect.foxpro;

import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericAccess;
import com.github.jferard.jxbase.dialect.db3.field.DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.field.FloatAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.DatetimeAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.DoubleAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.IntegerAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.NullFlagsAccess;
import com.github.jferard.jxbase.field.FieldRepresentation;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class FoxProAccess extends DB4Access implements CDDtFILMN0FieldsAccess {
    private final DatetimeAccess datetimeAccess;
    private final IntegerAccess integerAccess;
    private final NullFlagsAccess nullFlagsAccess;
    private final DoubleAccess doubleAccess;

    public FoxProAccess(final CharacterAccess characterAccess, final DateAccess dateAccess,
                        final DatetimeAccess datetimeAccess, final FloatAccess floatAccess,
                        final IntegerAccess integerAccess, final LogicalAccess logicalAccess,
                        final MemoAccess memoAccess, final NullFlagsAccess nullFlagsAccess,
                        final NumericAccess numericAccess,
                        final DoubleAccess doubleAccess) {
        super(characterAccess, dateAccess, floatAccess, logicalAccess, memoAccess, numericAccess);
        this.datetimeAccess = datetimeAccess;
        this.integerAccess = integerAccess;
        this.nullFlagsAccess = nullFlagsAccess;
        this.doubleAccess = doubleAccess;
    }

    @Override
    public int getDatetimeValueLength() {
        return this.datetimeAccess.getDatetimeValueLength();
    }

    @Override
    public Date getDatetimeValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.datetimeAccess.getDatetimeValue(recordBuffer, offset, length);
    }

    @Override
    public void writeDatetimeValue(final OutputStream out, final Date value) throws IOException {
        this.datetimeAccess.writeDatetimeValue(out, value);
    }

    @Override
    public FieldRepresentation getDatetimeRepresentation(final String name) {
        return this.datetimeAccess.getDatetimeRepresentation(name);
    }

    @Override
    public int getIntegerValueLength() {
        return this.integerAccess.getIntegerValueLength();
    }

    @Override
    public Long getIntegerValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.integerAccess.getIntegerValue(recordBuffer, offset, length);
    }

    @Override
    public void writeIntegerValue(final OutputStream out, final Long value) throws IOException {
        this.integerAccess.writeIntegerValue(out, value);
    }

    @Override
    public FieldRepresentation getIntegerFieldRepresentation(final String name) {
        return this.integerAccess.getIntegerFieldRepresentation(name);
    }

    @Override
    public int getNullFlagsFieldLength(final int dataSize) {
        return this.nullFlagsAccess.getNullFlagsFieldLength(dataSize);
    }

    @Override
    public byte[] getNullFlagsValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.nullFlagsAccess.getNullFlagsValue(recordBuffer, offset, length);
    }

    @Override
    public void writeNullFlagsValue(final OutputStream out, final byte[] value, final int length)
            throws IOException {
        this.nullFlagsAccess.writeNullFlagsValue(out, value, length);
    }

    @Override
    public FieldRepresentation getNullFlagsFieldRepresentation(final String name,
                                                               final int length) {
        return this.nullFlagsAccess.getNullFlagsFieldRepresentation(name, length);
    }

    @Override
    public int getDoubleValueLength() {
        return this.doubleAccess.getDoubleValueLength();
    }

    @Override
    public double getDoubleValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.doubleAccess.getDoubleValue(recordBuffer, offset, length);
    }

    @Override
    public void writeDoubleValue(final OutputStream out, final double value) throws IOException {
        this.doubleAccess.writeDoubleValue(out, value);
    }

    @Override
    public FieldRepresentation getDoubleFieldRepresentation(final String name,
                                                            final int numberOfDecimalPlaces) {
        return this.doubleAccess.getDoubleFieldRepresentation(name, numberOfDecimalPlaces);
    }
}
