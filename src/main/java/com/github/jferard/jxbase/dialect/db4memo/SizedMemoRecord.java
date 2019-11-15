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

package com.github.jferard.jxbase.dialect.db4memo;

import com.github.jferard.jxbase.memo.MemoRecordTypeEnum;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;

import java.util.Arrays;

public class SizedMemoRecord implements XBaseMemoRecord {
    private final byte[] bytes;
    private final int length;

    public SizedMemoRecord(final byte[] bytes, final int length) {
        this.bytes = bytes;
        this.length = length;
    }

    @Override
    public byte[] getBytes() {
        return this.bytes;
    }

    @Override
    public byte[] getValue() {
        return this.bytes;
    }

    @Override
    public int getLength() {
        return this.length;
    }

    @Override
    public MemoRecordTypeEnum getMemoType() {
        return MemoRecordTypeEnum.NO_TYPE;
    }

    @Override
    public String toString() {
        return "ByteMemoRecord[bytes=" + Arrays.toString(this.bytes) + "]";
    }
}
