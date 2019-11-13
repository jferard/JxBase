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

package com.github.jferard.jxbase.core;

import com.github.jferard.jxbase.dialect.db4memo.DB4MemoFileHeader;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

public class MemoFileHeaderTest {
    @Test
    public void testBlockAndNext() {
        final DB4MemoFileHeader memoFileHeader = DB4MemoFileHeader
                .create("abcdefghijklmnop\0qrstuvw".getBytes(Charset.forName("ASCII")));
        Assert.assertEquals(1701209960, memoFileHeader.getBlockSize());
        Assert.assertEquals(1633837924, memoFileHeader.getNextFreeBlockLocation());
    }

    @Test
    public void testToString() {
        final DB4MemoFileHeader memoFileHeader = DB4MemoFileHeader
                .create("abcdefghijklmnop\0qrstuvw".getBytes(Charset.forName("ASCII")));
        Assert.assertEquals(
                "MemoFileHeader[nextFreeBlockLocation=1633837924, blockSize=1701209960, " +
                        "dbfName=ijklmnop, blockLength=29813]",
                memoFileHeader.toString());
    }
}