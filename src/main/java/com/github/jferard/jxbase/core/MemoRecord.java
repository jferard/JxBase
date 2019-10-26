/*
 * JDBF - Copyright (c) 2012-2018 Ivan Ryndin (https://github.com/iryndin)
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

import com.github.jferard.jxbase.util.BitUtils;

import java.nio.charset.Charset;

/**
 * https://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm, Table Records
 */
public class MemoRecord {
    private final int blockSize;
    private final int offsetInBlocks;
    private byte[] value;
    private int length;
    private MemoRecordTypeEnum memoType;

    public MemoRecord(byte[] header, byte[] value, int blockSize, int offsetInBlocks) {
        this.value = value;
        calculateFields(header);
        this.blockSize = blockSize;
        this.offsetInBlocks = offsetInBlocks;
    }

    private void calculateFields(byte[] bytes) {
        int type = BitUtils.makeInt(bytes[3],bytes[2],bytes[1],bytes[0]);
        this.memoType = MemoRecordTypeEnum.fromInt(type);
        this.length = BitUtils.makeInt(bytes[7],bytes[6],bytes[5],bytes[4]);
    }

    public byte[] getValue() {
        return value;
    }

    public String getValueAsString(Charset charset) {
        return new String(value, charset);
    }

    /**
     * Memo record length in bytes
     * @return
     */
    public int getLength() {
        return length;
    }

    public MemoRecordTypeEnum getMemoType() {
        return memoType;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getOffsetInBlocks() {
        return offsetInBlocks;
    }
}
