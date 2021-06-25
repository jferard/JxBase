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

package com.github.jferard.jxbase.dialect.db4.memo;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.dialect.db3.DB3Utils;
import com.github.jferard.jxbase.memo.MemoFileHeader;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class DB4MemoFileHeaderReaderTest {
    @Test
    public void testBlockAndNext() {
        final byte[] bytes = TestHelper
                .getBytes("\110\0\0\0\0\10\0\0foobar  \0xxxxxxxxx", DB3Utils.MEMO_HEADER_LENGTH);
        final MemoFileHeader memoFileHeader =
                new DB4MemoFileHeaderReader().read(ByteBuffer.wrap(bytes));
        Assert.assertEquals(72, memoFileHeader.getNextFreeBlockLocation());
        Assert.assertEquals(2048, memoFileHeader.getBlockLength());
    }

    @Test
    public void test0Block() {
        final byte[] bytes = TestHelper
                .getBytes("\110\0\0\0\0\0\0\0foobar  \0xxxxxxxxx", DB3Utils.MEMO_HEADER_LENGTH);
        final MemoFileHeader memoFileHeader =
                new DB4MemoFileHeaderReader().read(ByteBuffer.wrap(bytes));
        Assert.assertEquals(512, memoFileHeader.getBlockLength());
    }

    @Test
    public void testToString() {
        final byte[] bytes = TestHelper
                .getBytes("\110\0\0\0\0\10\0\0foobar  \0xxxxxxxxx", DB3Utils.MEMO_HEADER_LENGTH);
        final MemoFileHeader memoFileHeader =
                new DB4MemoFileHeaderReader().read(ByteBuffer.wrap(bytes));
        Assert.assertEquals(
                "MemoFileHeader[blockLength=2048, nextFreeBlockLocation=72, " +
                        "meta={dbfName=foobar}]", memoFileHeader.toString());
    }
}