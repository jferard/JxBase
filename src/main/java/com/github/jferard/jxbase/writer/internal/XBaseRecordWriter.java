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

package com.github.jferard.jxbase.writer.internal;

import com.github.jferard.jxbase.core.XBaseMemoRecord;
import com.github.jferard.jxbase.core.field.XBaseField;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface XBaseRecordWriter {

    void write(Map<String, Object> objectByName)
            throws IOException;

    <V> void writeValue(XBaseField field, V value) throws IOException;

    void writeCharacterValue(String value, int length) throws IOException;

    void writeDateValue(Date value) throws IOException;

    void writeDatetimeValue(Date value) throws IOException;

    void writeIntegerValue(Long value, int length) throws IOException;

    void writeLogicalValue(Boolean value) throws IOException;

    <T extends XBaseMemoRecord<?>> void writeMemoValue(T value) throws IOException;

    void writeNumericValue(BigDecimal value, int length, int numberOfDecimalPlaces) throws IOException;

    int getRecordQty();
}