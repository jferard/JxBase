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

public interface DbfField {
    /**
     * @return the name of the field
     */
    String getName();

    /**
     * @return the type of the field
     */
    DbfFieldTypeEnum getType();

    /**
     * @return the length of the field
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

    Object getValue(DbfRecord dbfRecord, Charset charset) throws ParseException;
}
