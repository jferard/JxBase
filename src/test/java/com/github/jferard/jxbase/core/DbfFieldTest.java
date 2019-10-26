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

public class DbfFieldTest {
    @Test
    public void testToString() {
        DbfField f = new DbfField("a", DbfFieldTypeEnum.Character, 10, 2);
        Assert.assertEquals(
                "DbfField [\n" + "  name=a, \n" + "  type=Character, \n" + "  length=10, \n" +
                        "  numberOfDecimalPlaces=2, \n" + "  offset=0\n" + "]", f.toString());
    }

    @Test
    public void testGetStringRepresentation() {
        DbfField f = new DbfField("a", DbfFieldTypeEnum.Character, 10, 2);
        Assert.assertEquals("a,C,10,2", f.getStringRepresentation());
    }

    @Test
    public void testFromStringRepresentation() {
        DbfField f = DbfField.fromStringRepresentation("a,C,10,2");
        Assert.assertEquals("a", f.getName());
        Assert.assertEquals(DbfFieldTypeEnum.Character, f.getType());
        Assert.assertEquals(10, f.getLength());
        Assert.assertEquals(2, f.getNumberOfDecimalPlaces());
    }

    @Test
    public void testFromStringRepresentation0() {
        DbfField f = DbfField.fromStringRepresentation("a,0,1,2");
        Assert.assertEquals("a", f.getName());
        Assert.assertEquals(DbfFieldTypeEnum.NullFlags, f.getType());
        Assert.assertEquals(1, f.getLength());
        Assert.assertEquals(2, f.getNumberOfDecimalPlaces());
    }
}