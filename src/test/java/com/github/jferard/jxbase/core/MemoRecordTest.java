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

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MemoRecordTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testCreateVoid() {
        exception.expect(ArrayIndexOutOfBoundsException.class);
        MemoRecord mr = new MemoRecord(new byte[]{}, new byte[]{}, 0, 0);
    }

    @Test
    public void testCreate() {
        MemoRecord mr = new MemoRecord(new byte[]{0, 0, 0, 0, 5, 6, 7, 8}, new byte[]{}, 0, 0);
        Assert.assertEquals(MemoRecordTypeEnum.IMAGE, mr.getMemoType());
        Assert.assertEquals(84281096, mr.getLength());
    }

    @Test
    public void testGet() {
        final byte[] value = {0, 1};
        MemoRecord mr = new MemoRecord(new byte[]{0, 0, 0, 0, 5, 6, 7, 8}, value, 1, 2);
        Assert.assertArrayEquals(value, mr.getValue());
        Assert.assertEquals(1, mr.getBlockSize());
        Assert.assertEquals(2, mr.getOffsetInBlocks());
    }
}