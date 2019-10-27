/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

package com.github.jferard.jxbase.core;


import java.nio.charset.Charset;
import java.text.ParseException;

public class DbfFieldImpl implements DbfField {
    public static DbfField fromStringRepresentation(String s) {
        String[] a = s.split(",");

        final DbfFieldTypeEnum type = DbfFieldTypeEnum.fromChar(a[1].charAt(0));
        final int length = Integer.parseInt(a[2]);
        final int numberOfDecimalPlaces = Integer.parseInt(a[3]);
        final String name = a[0];
        if (type == DbfFieldTypeEnum.Date) {
            assert length == 8;
            return new DateDbfField(name);
        } else {
            return new DbfFieldImpl(name, type, length, numberOfDecimalPlaces);
        }
    }

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
    public DbfFieldImpl(String name, DbfFieldTypeEnum type, int length, int numberOfDecimalPlaces) {
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
    public Object getValue(DbfRecord dbfRecord, Charset charset) throws ParseException {
        return dbfRecord.getObject(this.name, this.type, charset);
    }
}
