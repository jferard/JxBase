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

package com.github.jferard.jxbase.dialect.db4;

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.LogicalField;
import com.github.jferard.jxbase.dialect.db2.field.NumericField;
import com.github.jferard.jxbase.dialect.db3.field.DateField;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.db4.field.FloatField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class DB4DialectTest {
    private XBaseDialect<DB4Access, DB4Dialect> dialect;

    @Before
    public void setUp() {
        this.dialect = DB4DialectFactory.create(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE);
    }

    @Test
    public void testOptionalLength() {
        Assert.assertEquals(0, this.dialect.getOptionalLength());
    }

    @Test
    public void testFieldDescriptionLength() {
        Assert.assertEquals(32, this.dialect.getFieldDescriptorLength());
    }

    @Test
    public void testType() {
        Assert.assertEquals(XBaseFileTypeEnum.dBASE4, this.dialect.getType());
    }

    @Test
    public void testGetField() {
        Assert.assertEquals(new CharacterField("x", 2),
                this.dialect.createXBaseField("x", (byte) 'C', 2, 0));
        Assert.assertEquals(new DateField("x"), this.dialect.createXBaseField("x", (byte) 'D', 8, 0));
        Assert.assertEquals(new FloatField("x"),
                this.dialect.createXBaseField("x", (byte) 'F', 20, 0));
        Assert.assertEquals(new LogicalField("x"),
                this.dialect.createXBaseField("x", (byte) 'L', 1, 0));
        Assert.assertEquals(new MemoField("x"), this.dialect.createXBaseField("x", (byte) 'M', 10, 0));
        Assert.assertEquals(new NumericField("x", 10, 2),
                this.dialect.createXBaseField("x", (byte) 'N', 10, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDateField() {
        this.dialect.createXBaseField("x", (byte) 'D', 5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFloatField() {
        this.dialect.createXBaseField("x", (byte) 'F', 5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLogicalField() {
        this.dialect.createXBaseField("x", (byte) 'L', 10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMemoField() {
        this.dialect.createXBaseField("x", (byte) 'M', 4, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOtherField() {
        this.dialect.createXBaseField("x", (byte) 'A', 10, 2);
    }

    @Test
    public void test() throws IOException {
        this.dialect.getInternalReaderFactory();
        this.dialect.getInternalWriterFactory(
        );
    }
}