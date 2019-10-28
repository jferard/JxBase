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

import java.nio.charset.Charset;
import java.text.ParseException;

public class MemoDbfField<V> implements DbfField<V> {
    private String name;

    public MemoDbfField(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public DbfFieldTypeEnum getType() {
        return DbfFieldTypeEnum.Memo;
    }

    @Override
    public int getLength() {
        return 10;
    }

    @Override
    public int getNumberOfDecimalPlaces() {
        return 0;
    }

    @Override
    public String getStringRepresentation() {
        return this.name + ",M,10,0";
    }

    @Override
    public V getValue(DbfRecord dbfRecord, Charset charset) throws ParseException {
        return null;
    }

    @Override
    public OffsetDbfField<V> withOffset(int offset) {
        return new OffsetDbfField<V>(this, offset);
    }
}
