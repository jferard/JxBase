/*
* JxBase - Copyright (c) 2019-2021 Julien Férard
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

package com.github.jferard.jxbase.dialect.foxpro.memo;

import com.github.jferard.jxbase.memo.MemoRecordType;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;

import java.nio.charset.Charset;

/**
 * A foxpro memo record of type text.
 * https://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm, Table Records
 */
public class TextMemoRecord implements XBaseMemoRecord {
    private final MemoRecordType type;
    private final Charset charset;
    private final String s;

    public TextMemoRecord(final String s, final Charset charset) {
        this.s = s;
        this.type = MemoRecordType.TEXT;
        this.charset = charset;
    }

    @Override
    public byte[] getBytes() {
        return this.s.getBytes(this.charset);
    }

    @Override
    public String getValue() {
        return this.s;
    }

    @Override
    public int getLength() {
        return this.s.getBytes(this.charset).length;
    }

    @Override
    public MemoRecordType getMemoType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "TextMemoRecord[text=" + this.getValue() + "]";
    }
}
