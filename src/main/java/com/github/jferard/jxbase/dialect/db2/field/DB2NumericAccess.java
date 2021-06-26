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
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;
import com.github.jferard.jxbase.util.BytesUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Access (read/write) to numeric field in DB2.
 */
public class DB2NumericAccess implements NumericAccess {
    public static final CharSequence NUMERIC_OVERFLOW = "*";
    private final RawRecordWriteHelper rawRecordWriter;
    private final Map<Integer, DecimalFormat> decimalFormatByDecimalPlaces;

    public DB2NumericAccess(final RawRecordWriteHelper rawRecordWriter) {
        this.rawRecordWriter = rawRecordWriter;
        this.decimalFormatByDecimalPlaces = new HashMap<Integer, DecimalFormat>();
    }

    @Override
    public int getNumericValueLength(final int fieldLength) {
        return fieldLength;
    }

    @Override
    public BigDecimal extractNumericValue(final byte[] recordBuffer, final int offset,
                                          final int length, final int numberOfDecimalPlaces) {
        final String s =
                BytesUtils.extractTrimmedASCIIString(recordBuffer, offset, length);
        if (s == null || s.contains(NUMERIC_OVERFLOW)) {
            return null;
        }
        final BigDecimal ret = new BigDecimal(s);
        return ret.setScale(numberOfDecimalPlaces, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public void writeNumericValue(final OutputStream out, final BigDecimal value, final int length,
                                  final int numberOfDecimalPlaces) throws IOException {
        if (value == null) {
            this.rawRecordWriter.writeEmpties(out, length);
        } else {
            final DecimalFormat df = this.getDecimalFormat(numberOfDecimalPlaces);
            final String s = df.format(value);
            final byte[] numberBytes = s.getBytes(JxBaseUtils.ASCII_CHARSET);
            final int missingCount = length - numberBytes.length;
            if (missingCount >= 0) {
                this.writeNumeric(out, numberBytes, missingCount, length);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    private DecimalFormat getDecimalFormat(final int numberOfDecimalPlaces) {
        DecimalFormat df = this.decimalFormatByDecimalPlaces.get(numberOfDecimalPlaces);
        if (df == null) {
            df = new DecimalFormat();
            df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
            df.setMinimumIntegerDigits(1);
            df.setGroupingUsed(false);
            df.setMinimumFractionDigits(numberOfDecimalPlaces);
            df.setMaximumFractionDigits(numberOfDecimalPlaces);
            this.decimalFormatByDecimalPlaces.put(numberOfDecimalPlaces, df);
        }
        return df;
    }

    private void writeNumeric(final OutputStream out, final byte[] numberBytes,
                              final int missingCount, final int length) throws IOException {
        final byte[] bytes = new byte[length];
        for (int i = 0; i < missingCount; i++) {
            bytes[i] = (byte) JxBaseUtils.EMPTY;
        }
        for (int i = 0; i < numberBytes.length; i++) {
            bytes[missingCount + i] = numberBytes[i];
        }
        this.rawRecordWriter.write(out, bytes);
    }

    @Override
    public FieldRepresentation getNumericFieldRepresentation(final String fieldName, final int fieldLength,
                                                             final int fieldNumberOfDecimalPlaces) {
        return new FieldRepresentation(fieldName, 'N', fieldLength, fieldNumberOfDecimalPlaces);
    }
}
