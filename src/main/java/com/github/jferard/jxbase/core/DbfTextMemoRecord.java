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
public class DbfTextMemoRecord implements XBaseMemoRecord<String> {

    private final byte[] bytes;
    private final MemoRecordTypeEnum type;
    private final int length;
    private final int offsetInBlocks;
    private final Charset charset;

    public DbfTextMemoRecord(final byte[] bytes, final int length, final long offsetInBlocks, final Charset charset) {
        this.bytes = bytes;
        this.type = MemoRecordTypeEnum.TEXT;
        this.length = length;
        this.offsetInBlocks = (int) offsetInBlocks;
        this.charset = charset;
    }

    @Override
    public byte[] getBytes() {
        return this.bytes;
    }

    @Override
    public String getValue() {
        return new String(this.bytes, 0, this.bytes.length, this.charset);
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
        return "ImageMemoRecord[bytes=" + this.getValue() + "]";
    }
}
