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

/**
 * A XBase field
 * @param <V> the Java type of the field
 * @param <T> the type of the memo records.
 */
public interface XBaseField<V, T extends XBaseMemoRecord> {
    /**
     * @return the name of the field
     */
    String getName();

    /**
     * @return the type of the field
     */
    DbfFieldTypeEnum getType();

    /**
     * @return the length of the field (the actual length, not necessarily the third field)
     */
    int getLength();

    /**
     * @return the number of decimal places of the field
     */
    int getNumberOfDecimalPlaces();

    /**
     * @return a string representation in the format name[11 chars], type[1 char], length[<256],
     * decimal[<256]
     */
    String getStringRepresentation();

    /**
     * @param dbfRecord the current record
     * @param charset   the charset
     * @return the value of this field in the record
     * @throws ParseException if the value can't be parsed.
     */
    V getValue(XBaseRecord<T> dbfRecord, Charset charset) throws ParseException;

    /**
     * @param offset the offset in the field array
     * @return the field with an offset
     */
    OffsetXBaseField<V, T> withOffset(final int offset);
}
