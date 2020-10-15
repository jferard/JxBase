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

package com.github.jferard.jxbase.dialect.db2;

import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2NumericAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericAccess;
import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;

public class DB2Access implements CLNFieldsAccess {
    public static DB2Access create(final Charset charset) {
        final RawRecordReadHelper rawRecordReader = new RawRecordReadHelper(charset);
        final RawRecordWriteHelper rawRecordWriter = new RawRecordWriteHelper(charset);
        final CharacterAccess characterAccess =
                new DB2CharacterAccess(rawRecordReader, rawRecordWriter);
        final LogicalAccess logicalAccess = new DB2LogicalAccess(rawRecordReader, rawRecordWriter);
        final NumericAccess numericAccess = new DB2NumericAccess(rawRecordReader, rawRecordWriter);
        return new DB2Access(characterAccess, logicalAccess, numericAccess);
    }

    private final CharacterAccess characterAccess;
    private final LogicalAccess logicalAccess;
    private final NumericAccess numericAccess;

    public DB2Access(final CharacterAccess characterAccess, final LogicalAccess logicalAccess,
                     final NumericAccess numericAccess) {
        this.characterAccess = characterAccess;
        this.logicalAccess = logicalAccess;
        this.numericAccess = numericAccess;
    }

    @Override
    public int getCharacterValueLength(final int dataSize) {
        return this.characterAccess.getCharacterValueLength(dataSize);
    }

    @Override
    public String extractCharacterValue(final byte[] recordBuffer, final int offset,
                                        final int length) {
        return this.characterAccess.extractCharacterValue(recordBuffer, offset, length);
    }

    @Override
    public void writeCharacterValue(final OutputStream out, final String value, final int dataSize)
            throws IOException {
        this.characterAccess.writeCharacterValue(out, value, dataSize);
    }

    @Override
    public FieldRepresentation getCharacterFieldRepresentation(final String name,
                                                               final int dataSize) {
        return this.characterAccess.getCharacterFieldRepresentation(name, dataSize);
    }

    @Override
    public int getLogicalValueLength() {
        return this.logicalAccess.getLogicalValueLength();
    }

    @Override
    public Boolean extractLogicalValue(final byte[] recordBuffer, final int offset,
                                       final int length) {
        return this.logicalAccess.extractLogicalValue(recordBuffer, offset, length);
    }

    @Override
    public FieldRepresentation getLogicalFieldRepresentation(final String name) {
        return this.logicalAccess.getLogicalFieldRepresentation(name);
    }

    @Override
    public void writeLogicalValue(final OutputStream out, final Boolean value) throws IOException {
        this.logicalAccess.writeLogicalValue(out, value);
    }

    @Override
    public int getNumericValueLength(final int dataSize) {
        return this.numericAccess.getNumericValueLength(dataSize);
    }

    @Override
    public BigDecimal extractNumericValue(final byte[] recordBuffer, final int offset,
                                          final int length, final int numberOfDecimalPlaces) {
        return this.numericAccess
                .extractNumericValue(recordBuffer, offset, length, numberOfDecimalPlaces);
    }

    @Override
    public void writeNumericValue(final OutputStream out, final BigDecimal value, final int length,
                                  final int numberOfDecimalPlaces) throws IOException {
        this.numericAccess.writeNumericValue(out, value, length, numberOfDecimalPlaces);
    }

    @Override
    public FieldRepresentation getNumericFieldRepresentation(final String name, final int dataSize,
                                                             final int numberOfDecimalPlaces) {
        return this.numericAccess
                .getNumericFieldRepresentation(name, dataSize, numberOfDecimalPlaces);
    }
}
