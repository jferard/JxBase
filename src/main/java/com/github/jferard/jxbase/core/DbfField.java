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


public class DbfField {
    public static DbfField fromStringRepresentation(String s) {
        String[] a = s.split(",");

        return new DbfField(a[0], DbfFieldTypeEnum.fromChar(a[1].charAt(0)), Integer.parseInt(a[2]),
                Integer.parseInt(a[3]));
    }

    private final String name;
    private final DbfFieldTypeEnum type;
    private final int length;
    private final int numberOfDecimalPlaces;
    private int offset;


    /**
     * @param name                  the name of the field
     * @param type                  the type of the field
     * @param length                the length of the field
     * @param numberOfDecimalPlaces the number of decimal places of the field
     */
    public DbfField(String name, DbfFieldTypeEnum type, int length, int numberOfDecimalPlaces) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.numberOfDecimalPlaces = numberOfDecimalPlaces;
    }

    /**
     * @return the name of the field
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type of the field
     */
    public DbfFieldTypeEnum getType() {
        return type;
    }

    /**
     * @return the length of the field
     */
    public int getLength() {
        return length;
    }

    /**
     * @return the number of decimal places of the field
     */
    public int getNumberOfDecimalPlaces() {
        return numberOfDecimalPlaces;
    }

    /**
     * @return the offset of the field
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @param offset the offset of the field
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "DbfField [\n  name=" + name + ", \n  type=" + type + ", \n  length=" + length +
                ", \n  numberOfDecimalPlaces=" + numberOfDecimalPlaces + ", \n  offset=" + offset +
                "\n]";
    }

    /**
     * @return a string representation in the format name[11 chars], type[1 char], length[<256],
     * decimal[<256]
     */
    public String getStringRepresentation() {
        return name + "," + type.getType() + "," + length + "," + numberOfDecimalPlaces;
    }
}
