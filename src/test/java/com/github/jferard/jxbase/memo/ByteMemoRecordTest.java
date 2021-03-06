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

import org.junit.Assert;
import org.junit.Test;

public class ByteMemoRecordTest {
    @Test
    public void test() {
        final byte[] bytes = {1, 2, 3, 4};
        final ByteMemoRecord record = new ByteMemoRecord(bytes, 10);
        Assert.assertArrayEquals(bytes, record.getBytes());
        Assert.assertArrayEquals(bytes, record.getValue());
        Assert.assertEquals(10, record.getLength());
        Assert.assertEquals(MemoRecordType.NO_TYPE, record.getMemoType());
        Assert.assertEquals("ByteMemoRecord[bytes=[1, 2, 3, 4]]", record.toString());
    }

    @Test
    public void testLong() {
        final byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        final ByteMemoRecord record = new ByteMemoRecord(bytes, 10);
        Assert.assertEquals("ByteMemoRecord[bytes=[1, 2, 3, 4, 5, 6, 7, 8, 9, 10,...]]", record.toString());
    }
}