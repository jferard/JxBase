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

package com.github.jferard.jxbase.dialect.db4;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
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
import java.util.HashMap;

public class DB4DialectTest {
    private XBaseDialect<DB4Dialect, DB4Access> dialect;

    @Before
    public void setUp() {
        final DB4DialectFactory factory =
                DB4DialectFactory.create(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE);
        this.dialect = factory.build();
    }

    @Test
    public void testOptionalLength() {
        Assert.assertEquals(263, this.dialect.getOptionalLength());
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
                this.dialect.getXBaseField("x", (byte) 'C', 2, 0));
        Assert.assertEquals(new DateField("x"), this.dialect.getXBaseField("x", (byte) 'D', 8, 0));
        Assert.assertEquals(new FloatField("x"),
                this.dialect.getXBaseField("x", (byte) 'F', 20, 0));
        Assert.assertEquals(new LogicalField("x"),
                this.dialect.getXBaseField("x", (byte) 'L', 1, 0));
        Assert.assertEquals(new MemoField("x"), this.dialect.getXBaseField("x", (byte) 'M', 10, 0));
        Assert.assertEquals(new NumericField("x", 10, 2),
                this.dialect.getXBaseField("x", (byte) 'N', 10, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDateField() {
        this.dialect.getXBaseField("x", (byte) 'D', 5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFloatField() {
        this.dialect.getXBaseField("x", (byte) 'F', 5, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLogicalField() {
        this.dialect.getXBaseField("x", (byte) 'L', 10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMemoField() {
        this.dialect.getXBaseField("x", (byte) 'M', 4, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOtherField() {
        this.dialect.getXBaseField("x", (byte) 'A', 10, 2);
    }

    @Test
    public void test() throws IOException {
        this.dialect.getInternalReaderFactory("d", JxBaseUtils.ASCII_CHARSET);
        this.dialect.getInternalWriterFactory("d", JxBaseUtils.ASCII_CHARSET,
                new HashMap<String, Object>());
    }
}