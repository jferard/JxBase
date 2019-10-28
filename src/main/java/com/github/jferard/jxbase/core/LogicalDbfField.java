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

public class LogicalDbfField implements DbfField<Boolean> {
    private String name;

    public LogicalDbfField(String name) {
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
    public Boolean getValue(DbfRecord dbfRecord, Charset charset) throws ParseException {
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
    public OffsetDbfField<Boolean> withOffset(int offset) {
        return new OffsetDbfField<Boolean>(this, offset);
    }
}
