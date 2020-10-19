/*
 * JxBase - Copyright (c) 2019-2020 Julien Férard
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

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DbfFileTypeEnumTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testFomInt() {
        Assert.assertEquals(XBaseFileTypeEnum.FoxBASE1, XBaseFileTypeEnum.fromInt((byte) 0x02));
    }

    @Test
    public void testFomIntNoValue() {
        this.exception.expect(IllegalArgumentException.class);
        XBaseFileTypeEnum.fromInt((byte) 0xFF);
    }

    @Test
    public void testToByte() {
        Assert.assertEquals((byte) 0x8B, XBaseFileTypeEnum.dBASE4Memo.toByte());
    }
}