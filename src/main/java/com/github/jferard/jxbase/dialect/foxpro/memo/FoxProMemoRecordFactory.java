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

package com.github.jferard.jxbase.dialect.foxpro.memo;

import com.github.jferard.jxbase.memo.ByteMemoRecord;
import com.github.jferard.jxbase.memo.MemoRecordType;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;

import java.nio.charset.Charset;

/**
 * A record factory for foxpro records
 */
public class FoxProMemoRecordFactory {
    final Charset charset;

    public FoxProMemoRecordFactory(final Charset charset) {
        this.charset = charset;
    }

    /**
     * Create a new record
     * @param dataBytes the data
     * @param memoRecordType the type of the record
     * @param memoRecordLength the length of the record
     * @return
     */
    public XBaseMemoRecord create(final byte[] dataBytes, final MemoRecordType memoRecordType,
                                  final int memoRecordLength) {
        switch (memoRecordType) {
            case IMAGE:
            case OBJECT:
                return new ImageMemoRecord(dataBytes, memoRecordLength);
            case TEXT:
                return new TextMemoRecord(new String(dataBytes, 0, memoRecordLength, this.charset),
                        this.charset);
            case NO_TYPE:
                return new ByteMemoRecord(dataBytes, memoRecordLength);
            default:
                throw new IllegalArgumentException(memoRecordType.toString());
        }
    }
}
