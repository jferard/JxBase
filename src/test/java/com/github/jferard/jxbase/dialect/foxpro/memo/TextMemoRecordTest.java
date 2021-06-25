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
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

public class TextMemoRecordTest {
    @Test
    public void testGet() {
        final String text = "foobarbaz";
        final TextMemoRecord record = new TextMemoRecord(text, JxBaseUtils.ASCII_CHARSET);
        Assert.assertArrayEquals(text.getBytes(JxBaseUtils.ASCII_CHARSET), record.getBytes());
        Assert.assertEquals(text, record.getValue());
        Assert.assertEquals(9, record.getLength());
        Assert.assertEquals(MemoRecordType.TEXT, record.getMemoType());
        Assert.assertEquals("TextMemoRecord[text=foobarbaz]", record.toString());
    }

}