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
import org.junit.Assert;
import org.junit.Test;

public class ImageMemoRecordTest {
    @Test
    public void testGet() {
        final byte[] bytes = {1, 2, 3, 4};
        final ImageMemoRecord record = new ImageMemoRecord(bytes, 10);
        Assert.assertArrayEquals(bytes, record.getBytes());
        Assert.assertArrayEquals(bytes, record.getValue());
        Assert.assertEquals(10, record.getLength());
        Assert.assertEquals(MemoRecordType.IMAGE, record.getMemoType());
        Assert.assertEquals("ImageMemoRecord[bytes=[1, 2, 3, 4]]", record.toString());
    }
}