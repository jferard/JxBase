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

import java.nio.charset.Charset;

/**
 * https://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm, Table Records
 */
public class DbfMemoRecord implements XBaseMemoRecord {
    private final byte[] bytes;
    private final MemoRecordTypeEnum type;
    private final int length;
    private final int offsetInBlocks;

    public DbfMemoRecord(byte[] bytes, MemoRecordTypeEnum type, int length, int offsetInBlocks) {
        this.bytes = bytes;
        this.type = type;
        this.length = length;
        this.offsetInBlocks = offsetInBlocks;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String getValueAsString(Charset charset) {
        return new String(bytes, 0, bytes.length, charset);
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
    public int getOffsetInBlocks() {
        return this.offsetInBlocks;
    }
}
