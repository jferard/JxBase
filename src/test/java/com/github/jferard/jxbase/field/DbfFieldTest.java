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

package com.github.jferard.jxbase.field;

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.dialect.db3memo.DB3MemoDialect;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DbfFieldTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private DB3MemoDialect dialect;

    @Before
    public void setUp() {
        this.dialect = new DB3MemoDialect(XBaseFileTypeEnum.dBASE4SQLTable);
    }

    @Test
    public void testToString() {
        final XBaseField f = new CharacterField("a", 10);
        Assert.assertEquals("CharacterField[name=a, length=10]", f.toString());
    }

    @Test
    public void testGetStringRepresentation() {
        final XBaseField f = new CharacterField("a", 10);
        Assert.assertEquals("a,C,10,0", f.toStringRepresentation(this.dialect));
    }

    @Test
    public void testFromStringRepresentation() {
        final XBaseField f = this.dialect.fromStringRepresentation("a,C,10,0");
        Assert.assertEquals("a", f.getName());
        Assert.assertEquals(10, f.getValueByteLength(this.dialect));
        Assert.assertEquals("CharacterField[name=a, length=10]", f.toString());
        Assert.assertEquals("a,C,10,0", f.toStringRepresentation(this.dialect));
    }

    @Test
    public void testFromStringRepresentation0() {
        final XBaseField f = new FoxProDialect(XBaseFileTypeEnum.VisualFoxPro)
                .fromStringRepresentation("a,0,10,0");
    }
}