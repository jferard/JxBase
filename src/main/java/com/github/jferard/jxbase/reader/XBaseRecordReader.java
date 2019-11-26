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

package com.github.jferard.jxbase.reader;

import com.github.jferard.jxbase.core.XBaseRecord;

import java.io.IOException;
import java.text.ParseException;

public interface XBaseRecordReader {
    XBaseRecord read() throws IOException, ParseException;

    /*
    String getCharacterValue(byte[] recordBuffer, int offset, int length);

    String getTrimmedString(byte[] recordBuffer, int offset, int length, Charset charset);

    String getTrimmedASCIIString(byte[] recordBuffer, int offset, int length);

    Date getDateValue(byte[] recordBuffer, int offset, int length);

    Long getIntegerValue(byte[] recordBuffer, int offset, int length);

    Boolean getLogicalValue(byte[] recordBuffer, int offset, int length);

    BigDecimal getNumericValue(byte[] recordBuffer, int offset, int length,
                               int numberOfDecimalPlaces);


     */
    void close() throws IOException;

    /* With memo
    <T extends XBaseMemoRecord<?>> T getMemoValue(byte[] recordBuffer, int offset, int length)
            throws IOException;
    */

    /* FoxPro
    Date getDatetimeValue(byte[] recordBuffer, int offset, int length);

    <T extends XBaseMemoRecord<?>> T getSmallMemoValue(byte[] recordBuffer, int offset, int length)
            throws IOException;

    byte[] getNullFlagsValue(byte[] recordBuffer, int offset, int length);
    */
}