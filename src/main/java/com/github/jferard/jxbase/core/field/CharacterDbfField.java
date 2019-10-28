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
import java.text.ParseException;

public class CharacterDbfField<T extends XBaseMemoRecord> implements XBaseField<String, T> {
    private final String name;
    private final int length;

    public CharacterDbfField(final String name, final int length) {
        this.name = name;
        this.length = length;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public DbfFieldTypeEnum getType() {
        return DbfFieldTypeEnum.Character;
    }

    @Override
    public int getLength() {
        return this.length;
    }

    @Override
    public int getNumberOfDecimalPlaces() {
        return 0;
    }

    @Override
    public String getStringRepresentation() {
        return this.name + ",C," + this.length + ",0";
    }

    @Override
    public String getValue(final XBaseRecord<T> dbfRecord, final Charset charset)
            throws ParseException {
        return dbfRecord.getString(this.name, charset);
    }

    @Override
    public OffsetXBaseField<String, T> withOffset(int offset) {
        return new OffsetXBaseField<String, T>(this, offset);
    }
}
