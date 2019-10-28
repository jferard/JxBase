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

import com.github.jferard.jxbase.core.XBaseMemoRecord;

public class DbfFieldFactory<T extends XBaseMemoRecord> {
    public XBaseField<?, T> fromStringRepresentation(final String s) {
        String[] a = s.split(",");

        final String name = a[0];
        final DbfFieldTypeEnum type = DbfFieldTypeEnum.fromChar(a[1].charAt(0));
        final int length = Integer.parseInt(a[2]);
        final int numberOfDecimalPlaces = Integer.parseInt(a[3]);
        return this.getDbfField(name, type, length, numberOfDecimalPlaces);
    }

    public XBaseField<?, T> getDbfField(final String name, final DbfFieldTypeEnum type,
                                        final int length, final int numberOfDecimalPlaces) {
        switch (type) {
            case Date:
                if (length != 8) {
                    throw new IllegalArgumentException("A date has 8 chars");
                }
                return new DateDbfField<T>(name);
            case Character:
                return new CharacterDbfField<T>(name, length);
            case Numeric:
            case Double:
                return new NumericDbfField<T>(name, length, numberOfDecimalPlaces);
            case Logical:
                if (length != 1) {
                    throw new IllegalArgumentException("A boolean has one char");
                }
                return new LogicalDbfField<T>(name);
            case Integer:
                return new IntegerDbfField<T>(name, length);
            case Memo:
                return new MemoDbfField<T>(name);
            default:
                return new DefaultDbfField<T>(name, type, length, numberOfDecimalPlaces);
        }
    }
}
