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

import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.util.BitUtils;

public class FoxProDialect extends GenericDialect {
    public FoxProDialect(final XBaseFileTypeEnum type) {
        super(type);
    }

    public int getDatetimeFieldLength() {
        return 0;
    }

    public String datetimeFieldToStringRepresentation(final String name) {
        return null;
    }

    public int getNullFlagsFieldLength(final int length) {
        return length;
    }

    public String smallMemoFieldToStringRepresentation(final String name) {
        return name + ",M,10,0";
    }

    public int getSmallMemoFieldLength() {
        return 4;
    }



    /**
     * https://www.clicketyclick.dk/databases/xbase/format/data_types.html:
     * > Pointer to ASCII text field in memo file 10 digits representing a pointer to a DBT block
     * (default is blanks).
     *
     * @param recordReader
     * @param recordBuffer
     * @param offset
     * @param length
     * @return
     */
    @Override
    public long getOffsetInBlocks(final XBaseRecordReader recordReader,
                                  final byte[] recordBuffer, final int offset, final int length) {
        assert length == 4;
        return BitUtils.makeInt(recordBuffer[offset], recordBuffer[offset+1], recordBuffer[offset+2], recordBuffer[offset+3]);
    }
}

