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

package com.github.jferard.jxbase.reader.internal;

import com.github.jferard.jxbase.core.GenericRecord;
import com.github.jferard.jxbase.core.XBaseMemoRecord;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Date;

public interface XBaseRecordReader {
    GenericRecord read() throws IOException, ParseException;

    String getCharacterValue(byte[] recordBuffer, int offset, int length);

    String getTrimmedString(byte[] recordBuffer, int offset, int length, Charset charset);

    String getTrimmedASCIIString(byte[] recordBuffer, int offset, int length);

    Date getDateValue(byte[] recordBuffer, int offset, int length);

    Date getDatetimeValue(byte[] recordBuffer, int offset, int length);

    Long getIntegerValue(byte[] recordBuffer, int offset, int length);

    Boolean getLogicalValue(byte[] recordBuffer, int offset, int length);

    <T extends XBaseMemoRecord<?>> T getMemoValue(byte[] recordBuffer, int offset, int length)
            throws IOException;

    BigDecimal getNumericValue(byte[] recordBuffer, int offset, int length, int numberOfDecimalPlaces);

    void close() throws IOException;
}
