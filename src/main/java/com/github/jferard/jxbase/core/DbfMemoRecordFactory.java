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

public class DbfMemoRecordFactory {
    final Charset charset;

    public DbfMemoRecordFactory(final Charset charset) {
        this.charset = charset;
    }

    public XBaseMemoRecord<?> create(final byte[] dataBytes,
                                     final MemoRecordTypeEnum memoRecordType,
                                     final int memoRecordLength, final long offsetInBlocks) {
        switch (memoRecordType) {
            case IMAGE:
                return new DbfImageMemoRecord(dataBytes, memoRecordLength, offsetInBlocks);
            case TEXT:
                return new DbfTextMemoRecord(
                        new String(dataBytes, 0, memoRecordLength, this.charset), offsetInBlocks,
                        this.charset);
            default:
                throw new IllegalArgumentException(memoRecordType.toString());
        }
    }
}
