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

package com.github.jferard.jxbase.dialect.foxpro.memo;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.dialect.db3.DB3Utils;
import com.github.jferard.jxbase.dialect.foxpro.memo.FoxProMemoFileHeaderReader;
import com.github.jferard.jxbase.memo.MemoFileHeader;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

public class FoxProMemoFileHeaderReaderTest {
    @Test
    public void testBlockAndNext() {
        final byte[] bytes = TestHelper
                .getBytes("\0\0\0\110xx\10\0xxxxxxxx", DB3Utils.MEMO_HEADER_LENGTH);
        final MemoFileHeader memoFileHeader =
                new FoxProMemoFileHeaderReader().read(ByteBuffer.wrap(bytes));
        Assert.assertEquals(72, memoFileHeader.getNextFreeBlockLocation());
        Assert.assertEquals(2048, memoFileHeader.getBlockLength());
        Assert.assertNull(memoFileHeader.get("foo"));
    }

    @Test
    public void test0BlockAndNext() {
        final byte[] bytes = TestHelper
                .getBytes("\0\0\0\110xx\0\0xxxxxxxx", DB3Utils.MEMO_HEADER_LENGTH);
        final MemoFileHeader memoFileHeader =
                new FoxProMemoFileHeaderReader().read(ByteBuffer.wrap(bytes));
        Assert.assertEquals(0, memoFileHeader.getBlockLength());
        Assert.assertNull(memoFileHeader.get("foo"));
    }

    @Test
    public void testToString() {
        final byte[] bytes = TestHelper
                .getBytes("\0\0\0\110xx\10\0xxxxxxxx", DB3Utils.MEMO_HEADER_LENGTH);
        final MemoFileHeader memoFileHeader =
                new FoxProMemoFileHeaderReader().read(ByteBuffer.wrap(bytes));
        Assert.assertEquals(
                "MemoFileHeader[blockLength=2048, nextFreeBlockLocation=72, " +
                        "meta={}]", memoFileHeader.toString());
    }
}