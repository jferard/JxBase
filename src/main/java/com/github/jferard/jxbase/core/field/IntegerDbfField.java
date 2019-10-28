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
import com.github.jferard.jxbase.util.BitUtils;

import java.nio.charset.Charset;

public class IntegerDbfField<T extends XBaseMemoRecord> implements XBaseField<Integer, T> {
    private final String name;
    private final int length;

    public IntegerDbfField(final String name, final int length) {
        this.name = name;
        this.length = length;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public DbfFieldTypeEnum getType() {
        return DbfFieldTypeEnum.Integer;
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
        return this.name + ",I," + this.length + ",0";
    }

    @Override
    public Integer getValue(final XBaseRecord<T> dbfRecord, final Charset charset) {
        byte[] bytes = dbfRecord.getBytes(this.name);
        return BitUtils.makeInt(bytes[0], bytes[1], bytes[2], bytes[3]);
    }

    @Override
    public OffsetXBaseField<Integer, T> withOffset(int offset) {
        return new OffsetXBaseField<Integer, T>(this, offset);
    }
}
