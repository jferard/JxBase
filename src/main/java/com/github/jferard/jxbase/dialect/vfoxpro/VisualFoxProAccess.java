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

package com.github.jferard.jxbase.dialect.vfoxpro;

import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericAccess;
import com.github.jferard.jxbase.dialect.db3.field.DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.field.FloatAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.CurrencyAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.DatetimeAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.DoubleAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.IntegerAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.NullFlagsAccess;
import com.github.jferard.jxbase.field.FieldRepresentation;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Access (read/write) for VisualFoxPro fields.
 */
public class VisualFoxProAccess extends DB4Access implements CDDtFILMN0FieldsAccess, XBaseAccess {
    private final DatetimeAccess datetimeAccess;
    private final IntegerAccess integerAccess;
    private final NullFlagsAccess nullFlagsAccess;
    private final DoubleAccess doubleAccess;
    private final CurrencyAccess currencyAccess;

    public VisualFoxProAccess(final CharacterAccess characterAccess, final DateAccess dateAccess,
                              final DatetimeAccess datetimeAccess, final FloatAccess floatAccess,
                              final IntegerAccess integerAccess, final LogicalAccess logicalAccess,
                              final MemoAccess memoAccess, final NullFlagsAccess nullFlagsAccess,
                              final NumericAccess numericAccess,
                              final DoubleAccess doubleAccess, final CurrencyAccess currencyAccess) {
        super(characterAccess, dateAccess, floatAccess, logicalAccess, memoAccess, numericAccess);
        this.datetimeAccess = datetimeAccess;
        this.integerAccess = integerAccess;
        this.nullFlagsAccess = nullFlagsAccess;
        this.doubleAccess = doubleAccess;
        this.currencyAccess = currencyAccess;
    }

    @Override
    public int getDatetimeValueLength() {
        return this.datetimeAccess.getDatetimeValueLength();
    }

    @Override
    public Date extractDatetimeValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.datetimeAccess.extractDatetimeValue(recordBuffer, offset, length);
    }

    @Override
    public void writeDatetimeValue(final OutputStream out, final Date value) throws IOException {
        this.datetimeAccess.writeDatetimeValue(out, value);
    }

    @Override
    public FieldRepresentation getDatetimeRepresentation(final String fieldName) {
        return this.datetimeAccess.getDatetimeRepresentation(fieldName);
    }

    @Override
    public int getIntegerValueLength() {
        return this.integerAccess.getIntegerValueLength();
    }

    @Override
    public Long extractIntegerValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.integerAccess.extractIntegerValue(recordBuffer, offset, length);
    }

    @Override
    public void writeIntegerValue(final OutputStream out, final Long value) throws IOException {
        this.integerAccess.writeIntegerValue(out, value);
    }

    @Override
    public FieldRepresentation getIntegerFieldRepresentation(final String fieldName) {
        return this.integerAccess.getIntegerFieldRepresentation(fieldName);
    }

    @Override
    public int getNullFlagsFieldLength(final int fieldLength) {
        return this.nullFlagsAccess.getNullFlagsFieldLength(fieldLength);
    }

    @Override
    public byte[] extractNullFlagsValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.nullFlagsAccess.extractNullFlagsValue(recordBuffer, offset, length);
    }

    @Override
    public void writeNullFlagsValue(final OutputStream out, final byte[] value, final int length)
            throws IOException {
        this.nullFlagsAccess.writeNullFlagsValue(out, value, length);
    }

    @Override
    public FieldRepresentation getNullFlagsFieldRepresentation(final String fieldName,
                                                               final int fieldLength) {
        return this.nullFlagsAccess.getNullFlagsFieldRepresentation(fieldName, fieldLength);
    }

    @Override
    public int getDoubleValueLength() {
        return this.doubleAccess.getDoubleValueLength();
    }

    @Override
    public double extractDoubleValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.doubleAccess.extractDoubleValue(recordBuffer, offset, length);
    }

    @Override
    public void writeDoubleValue(final OutputStream out, final double value) throws IOException {
        this.doubleAccess.writeDoubleValue(out, value);
    }

    @Override
    public FieldRepresentation getDoubleFieldRepresentation(final String fieldName,
                                                            final int fieldNumberOfDecimalPlaces) {
        return this.doubleAccess.getDoubleFieldRepresentation(fieldName, fieldNumberOfDecimalPlaces);
    }

    @Override
    public int getCurrencyValueLength() {
        return this.currencyAccess.getCurrencyValueLength();
    }

    @Override
    public long extractCurrencyValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.currencyAccess.extractCurrencyValue(recordBuffer, offset, length);
    }

    @Override
    public void writeCurrencyValue(final OutputStream out, final long value) throws IOException {
        this.currencyAccess.writeCurrencyValue(out, value);

    }

    @Override
    public FieldRepresentation getCurrencyRepresentation(final String fieldName) {
        return this.currencyAccess.getCurrencyRepresentation(fieldName);
    }
}
