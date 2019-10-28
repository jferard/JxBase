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

package com.github.jferard.jxbase.core.field;


import com.github.jferard.jxbase.core.OffsetXBaseField;
import com.github.jferard.jxbase.core.XBaseMemoRecord;
import com.github.jferard.jxbase.core.XBaseRecord;

import java.nio.charset.Charset;

public class DefaultDbfField<T extends XBaseMemoRecord> implements XBaseField<Void, T> {

    private final String name;
    private final DbfFieldTypeEnum type;
    private final int length;
    private final int numberOfDecimalPlaces;

    /**
     * @param name                  the name of the field
     * @param type                  the type of the field
     * @param length                the length of the field
     * @param numberOfDecimalPlaces the number of decimal places of the field
     */
    public DefaultDbfField(final String name, final DbfFieldTypeEnum type, final int length,
                           final int numberOfDecimalPlaces) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.numberOfDecimalPlaces = numberOfDecimalPlaces;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public DbfFieldTypeEnum getType() {
        return type;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int getNumberOfDecimalPlaces() {
        return numberOfDecimalPlaces;
    }

    @Override
    public String toString() {
        return "DbfField[name=" + name + ", type=" + type + ", length=" + length +
                ", numberOfDecimalPlaces=" + numberOfDecimalPlaces + "]";
    }

    @Override
    public String getStringRepresentation() {
        return name + "," + type.getType() + "," + length + "," + numberOfDecimalPlaces;
    }

    @Override
    public Void getValue(final XBaseRecord<T> dbfRecord, final Charset charset) {
        return null;
    }

    @Override
    public OffsetXBaseField<Void, T> withOffset(int offset) {
        return new OffsetXBaseField<Void, T>(this, offset);
    }
}
