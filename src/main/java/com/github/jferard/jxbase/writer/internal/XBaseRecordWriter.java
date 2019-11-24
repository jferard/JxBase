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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface XBaseRecordWriter<D> {

    void write(Map<String, Object> objectByName) throws IOException;

    /*
    void writeCharacterValue(String value, int length) throws IOException;

    void writeDateValue(Date value) throws IOException;

    void writeIntegerValue(Long value) throws IOException;

    void writeLogicalValue(Boolean value) throws IOException;

    void writeNumericValue(BigDecimal value, int length, int numberOfDecimalPlaces)
            throws IOException;

*/
    /* With memo
    void writeMemoValue(XBaseMemoRecord value) throws IOException;
    */

    int getRecordQty();

    void close() throws IOException;

    /* FoxPro
    void writeDatetimeValue(Date value) throws IOException;

    <T extends XBaseMemoRecord<?>> void writeSmallMemoValue(T value) throws IOException;

    void writeNullFlagsValue(byte[] value, int length) throws IOException;
    */
}
