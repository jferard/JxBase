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

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.dialect.db3.reader.DB3MemoFileHeaderReader;
import com.github.jferard.jxbase.dialect.db4.reader.DB4MemoFileHeaderReader;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class MemoFileHeaderTest {
    @Test
    public void test() {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("foo", "bar");
        final MemoFileHeader header = new MemoFileHeader(512, 1, meta);
        Assert.assertEquals(512, header.getBlockLength());
        Assert.assertEquals(1, header.getNextFreeBlockLocation());
        Assert.assertEquals("bar", header.get("foo"));
        Assert.assertEquals(
                "MemoFileHeader[blockLength=512, nextFreeBlockLocation=1, meta={foo=bar}]",
                header.toString());
    }

    @Test
    public void testBlockAndNext() {
        final byte[] bytes = TestHelper
                .getBytes("abcdefghijklmnop\0qrstuvw", DB3MemoFileHeaderReader.MEMO_HEADER_LENGTH);
        final MemoFileHeader memoFileHeader =
                new DB4MemoFileHeaderReader().read(ByteBuffer.wrap(bytes));
        Assert.assertEquals(1701209960, memoFileHeader.getBlockLength());
        Assert.assertEquals(1684234849, memoFileHeader.getNextFreeBlockLocation());
    }

    @Test
    public void testToString() {
        final byte[] bytes = TestHelper
                .getBytes("abcdefghijklmnop\0qrstuvw", DB3MemoFileHeaderReader.MEMO_HEADER_LENGTH);
        final MemoFileHeader memoFileHeader =
                new DB4MemoFileHeaderReader().read(ByteBuffer.wrap(bytes));
        Assert.assertEquals(
                "MemoFileHeader[blockLength=1701209960, nextFreeBlockLocation=1684234849, " +
                        "meta={dbfName=ijklmnop}]", memoFileHeader.toString());
    }
}