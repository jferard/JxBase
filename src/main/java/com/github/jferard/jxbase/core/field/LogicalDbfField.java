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

public class LogicalDbfField<T extends XBaseMemoRecord> implements XBaseField<Boolean, T> {
    private final String name;

    public LogicalDbfField(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public DbfFieldTypeEnum getType() {
        return DbfFieldTypeEnum.Logical;
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public int getNumberOfDecimalPlaces() {
        return 0;
    }

    @Override
    public String getStringRepresentation() {
        return this.name + ",L,1,0";
    }

    @Override
    public Boolean getValue(final XBaseRecord<T> dbfRecord, final Charset charset) {
        String s = dbfRecord.getASCIIString(this.name);
        if (s == null) {
            return null;
        }
        if (s.equalsIgnoreCase("t")) {
            return Boolean.TRUE;
        } else if (s.equalsIgnoreCase("f")) {
            return Boolean.FALSE;
        } else {
            return null;
        }
    }

    @Override
    public OffsetXBaseField<Boolean, T> withOffset(int offset) {
        return new OffsetXBaseField<Boolean, T>(this, offset);
    }
}
