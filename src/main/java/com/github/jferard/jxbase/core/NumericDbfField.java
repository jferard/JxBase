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

package com.github.jferard.jxbase.core;

import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.charset.Charset;
import java.text.ParseException;

public class NumericDbfField implements DbfField<BigDecimal> {
    public static final String NUMERIC_OVERFLOW = "*";
    private final String name;
    private final int length;
    private final int numberOfDecimalPlaces;
    private final MathContext mc;

    public NumericDbfField(final String name, final int length, int numberOfDecimalPlaces) {
        this.name = name;
        this.length = length;
        this.numberOfDecimalPlaces = numberOfDecimalPlaces;
        this.mc = new MathContext(numberOfDecimalPlaces);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public DbfFieldTypeEnum getType() {
        return DbfFieldTypeEnum.Numeric;
    }

    @Override
    public int getLength() {
        return this.length;
    }

    @Override
    public int getNumberOfDecimalPlaces() {
        return this.numberOfDecimalPlaces;
    }

    @Override
    public String getStringRepresentation() {
        return this.name + ",N," + this.length + "," + this.numberOfDecimalPlaces;
    }

    @Override
    public BigDecimal getValue(final DbfRecord dbfRecord, final Charset charset) throws ParseException {
        String s = dbfRecord.getASCIIString(this.name);
        if (s == null || s.contains(NumericDbfField.NUMERIC_OVERFLOW)) {
            return null;
        }

        return new BigDecimal(s, this.mc);
    }

    @Override
    public OffsetDbfField<BigDecimal> withOffset(int offset) {
        return new OffsetDbfField<BigDecimal>(this, offset);
    }
}
