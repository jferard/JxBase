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

import com.github.jferard.jxbase.memo.ImageMemoRecord;
import com.github.jferard.jxbase.memo.MemoRecordTypeEnum;
import com.github.jferard.jxbase.memo.TextMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.charset.Charset;

public class MemoRecordTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private Charset charset;

    @Before
    public void setUp() {
        this.charset = Charset.forName("ASCII");
    }

    @Test
    public void testCreateVoid() {
        final XBaseMemoRecord<?> mr = new TextMemoRecord("", 0, this.charset);
    }

    @Test
    public void testCreate() {
        final XBaseMemoRecord<?> mr =
                new ImageMemoRecord(new byte[]{0, 0, 0, 0, 5, 6, 7, 8}, 84281096, 0);
        Assert.assertEquals(MemoRecordTypeEnum.IMAGE, mr.getMemoType());
        Assert.assertEquals(84281096, mr.getLength());
    }

    @Test
    public void testGet() {
        final byte[] value = {'A', 'B'};
        final XBaseMemoRecord<?> mr = new TextMemoRecord("AB", 3, this.charset);
        Assert.assertArrayEquals(value, mr.getBytes());
        Assert.assertEquals(MemoRecordTypeEnum.TEXT, mr.getMemoType());
        Assert.assertEquals(2, mr.getLength());
        Assert.assertEquals(3, mr.getOffsetInBlocks());
    }
}