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

package com.github.jferard.jxbase.memo;

import java.util.Arrays;

/**
 * https://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm, Table Records
 */
public class ImageMemoRecord implements XBaseMemoRecord<byte[]> {
    private final byte[] bytes;
    private final MemoRecordTypeEnum type;
    private final int length;
    private final long offsetInBlocks;

    public ImageMemoRecord(final byte[] bytes, final int length, final long offsetInBlocks) {
        this.bytes = bytes;
        this.type = MemoRecordTypeEnum.IMAGE;
        this.length = length;
        this.offsetInBlocks = offsetInBlocks;
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
        return this.type;
    }

    @Override
    public long getOffsetInBlocks() {
        return this.offsetInBlocks;
    }

    @Override
    public String toString() {
        return "ImageMemoRecord[bytes=" + Arrays.toString(this.bytes) + "]";
    }
}
