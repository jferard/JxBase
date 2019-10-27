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

public class OffsetDbfField {
    private final DbfField field;
    private final int offset;

    /**
     * @param field  the field
     * @param offset the offset
     */
    public OffsetDbfField(final DbfField field, final int offset) {
        this.field = field;
        this.offset = offset;
    }

    /**
     * @return the field
     */
    public DbfField getField() {
        return field;
    }

    /**
     * @return the offset of the field
     */
    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "OffsetDbfField[field=" + field.getStringRepresentation() + ", offset=" + offset +
                "]";
    }

    /**
     * @return the length of the inner field
     */
    public int getLength() {
        return this.field.getLength();
    }

    /**
     * @return the type of the inner field
     */
    public DbfFieldTypeEnum getType() {
        return this.field.getType();
    }

    /**
     * @return the name of the inner field
     */
    public String getName() {
        return this.field.getName();
    }

    /**
     * @return the nodp of the inner field
     */
    public int getNumberOfDecimalPlaces() {
        return this.field.getNumberOfDecimalPlaces();
    }
}
