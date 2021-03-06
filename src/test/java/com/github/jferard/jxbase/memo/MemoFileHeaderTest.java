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

package com.github.jferard.jxbase.memo;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.dialect.db3.DB3Utils;
import com.github.jferard.jxbase.dialect.db4.memo.DB4MemoFileHeaderReader;
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
}