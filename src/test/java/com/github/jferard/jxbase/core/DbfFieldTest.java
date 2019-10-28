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

import com.github.jferard.jxbase.core.field.DbfFieldFactory;
import com.github.jferard.jxbase.core.field.DbfFieldTypeEnum;
import com.github.jferard.jxbase.core.field.DefaultDbfField;
import com.github.jferard.jxbase.core.field.XBaseField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DbfFieldTest {
    private DbfFieldFactory<DbfMemoRecord> dbfFieldFactory;

    @Before
    public void setUp() {
        this.dbfFieldFactory = new DbfFieldFactory<DbfMemoRecord>();
    }

    @Test
    public void testToString() {
        XBaseField<?, DbfMemoRecord> f = new DefaultDbfField<DbfMemoRecord>("a", DbfFieldTypeEnum.Character, 10, 2);
        Assert.assertEquals("DbfField[name=a, type=Character, length=10, numberOfDecimalPlaces=2]",
                f.toString());
    }

    @Test
    public void testGetStringRepresentation() {
        XBaseField<?, DbfMemoRecord> f = new DefaultDbfField<DbfMemoRecord>("a", DbfFieldTypeEnum.Character, 10, 2);
        Assert.assertEquals("a,C,10,2", f.getStringRepresentation());
    }

    @Test
    public void testFromStringRepresentation() {
        XBaseField<?, DbfMemoRecord> f = dbfFieldFactory.fromStringRepresentation("a,C,10,2");
        Assert.assertEquals("a", f.getName());
        Assert.assertEquals(DbfFieldTypeEnum.Character, f.getType());
        Assert.assertEquals(10, f.getLength());
        Assert.assertEquals(0, f.getNumberOfDecimalPlaces());
    }

    @Test
    public void testFromStringRepresentation0() {
        XBaseField<?, DbfMemoRecord> f = dbfFieldFactory.fromStringRepresentation("a,0,1,2");
        Assert.assertEquals("a", f.getName());
        Assert.assertEquals(DbfFieldTypeEnum.NullFlags, f.getType());
        Assert.assertEquals(1, f.getLength());
        Assert.assertEquals(2, f.getNumberOfDecimalPlaces());
    }
}