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

package com.github.jferard.jxbase.memo;

import java.util.Arrays;

/**
 * https://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm, Table Records
 */
public class ByteMemoRecord implements XBaseMemoRecord {
    private final byte[] bytes;
    private final int length;

    public ByteMemoRecord(final byte[] bytes, final int length) {
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
        final byte[] ret;
        if (this.bytes.length >= 10) {
            ret = Arrays.copyOfRange(this.bytes, 0, 10);
        } else {
            ret = this.bytes;
        }
        return "ByteMemoRecord[bytes=" + Arrays.toString(ret) + "...]";
    }
}
