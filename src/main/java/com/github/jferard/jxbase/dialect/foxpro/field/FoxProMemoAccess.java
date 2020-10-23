/*
 * JxBase - Copyright (c) 2019-2020 Julien Férard
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

package com.github.jferard.jxbase.dialect.foxpro.field;

import com.github.jferard.jxbase.dialect.db3.field.DB3MemoAccess;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.BytesUtils;

public class FoxProMemoAccess extends DB3MemoAccess {
    public FoxProMemoAccess(final XBaseMemoReader memoReader, final XBaseMemoWriter memoWriter,
                            final RawRecordReadHelper rawRecordReader) {
        super(memoReader, memoWriter, rawRecordReader);
    }

    @Override
    public int getMemoValueLength() {
        return 4;
    }

    /**
     * https://www.clicketyclick.dk/databases/xbase/format/data_types.html:
     * > Pointer to ASCII text field in memo file 10 digits representing a pointer to a DBT block
     * (default is blanks).
     *
     * @param recordBuffer
     * @param offset
     * @param length
     * @return
     */
    @Override
    public long getOffsetInBlocks(final byte[] recordBuffer, final int offset, final int length) {
        return BytesUtils
                .makeLEInt(recordBuffer[offset], recordBuffer[offset + 1], recordBuffer[offset + 2],
                        recordBuffer[offset + 3]);
    }
}
