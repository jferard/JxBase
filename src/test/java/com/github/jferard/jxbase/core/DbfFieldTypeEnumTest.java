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

import org.junit.Assert;
import org.junit.Test;

public class DbfFieldTypeEnumTest {
    @Test
    public void testType() {
        Assert.assertEquals('N', DbfFieldTypeEnum.Numeric.getType());
    }

    @Test
    public void testByte() {
        Assert.assertEquals((byte) 'D', DbfFieldTypeEnum.Date.toByte());
    }

    @Test
    public void testFromChar() {
        Assert.assertEquals(DbfFieldTypeEnum.Integer, DbfFieldTypeEnum.fromChar('I'));
    }
}