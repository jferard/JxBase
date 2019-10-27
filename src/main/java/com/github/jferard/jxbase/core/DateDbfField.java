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

import com.github.jferard.jxbase.util.JdbfUtils;

import java.nio.charset.Charset;
import java.text.ParseException;

public class DateDbfField implements DbfField {
    private String name;

    public DateDbfField(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public DbfFieldTypeEnum getType() {
        return DbfFieldTypeEnum.Date;
    }

    @Override
    public int getLength() {
        return 8;
    }

    @Override
    public int getNumberOfDecimalPlaces() {
        return 0;
    }

    @Override
    public String getStringRepresentation() {
        return this.name + ",D,8,0";
    }

    @Override
    public Object getValue(DbfRecord dbfRecord, Charset charset) throws ParseException {
        String s = dbfRecord.getASCIIString(this.name);
        if (s == null) {
            return null;
        }
        return JdbfUtils.parseDate(s);
    }
}
