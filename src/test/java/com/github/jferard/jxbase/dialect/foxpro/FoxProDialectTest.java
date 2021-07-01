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

package com.github.jferard.jxbase.dialect.foxpro;

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.LogicalField;
import com.github.jferard.jxbase.dialect.db2.field.NumericField;
import com.github.jferard.jxbase.dialect.db3.field.DateField;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.field.FloatField;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.dialect.vfoxpro.field.DatetimeField;
import com.github.jferard.jxbase.dialect.vfoxpro.field.DoubleField;
import com.github.jferard.jxbase.dialect.vfoxpro.field.IntegerField;
import com.github.jferard.jxbase.dialect.vfoxpro.field.NullFlagsField;
import com.github.jferard.jxbase.field.XBaseField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.powermock.api.easymock.PowerMock;

public class FoxProDialectTest {
    private FoxProDialect dialect;
    private DB4Access access;

    @Before
    public void setUp() {
        this.access = PowerMock.createMock(VisualFoxProAccess.class);
        this.dialect = new FoxProDialect(XBaseFileTypeEnum.FoxPro2xMemo, this.access);
    }

    @Test
    public void testGetCharacterField() {
        final XBaseField<? super VisualFoxProAccess> field =
                this.dialect.createXBaseField("char", (byte) 'C', 10, 0);
        Assert.assertEquals(new CharacterField("char", 10), field);
    }

    @Test
    public void testGetDateField() {
        final XBaseField<? super VisualFoxProAccess> field =
                this.dialect.createXBaseField("date", (byte) 'D', 8, 0);
        Assert.assertEquals(new DateField("date"), field);
    }

    @Test
    public void testGetDateFieldException() {
        final FoxProDialect thisDialect = this.dialect;
        final IllegalArgumentException e =
                Assert.assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        thisDialect.createXBaseField("date", (byte) 'D', 9, 0);
                    }
                });
        Assert.assertEquals("A date has 8 chars", e.getMessage());
    }

    @Test
    public void testGetFloatField() {
        final XBaseField<? super VisualFoxProAccess> field =
                this.dialect.createXBaseField("float", (byte) 'F', 20, 0);
        Assert.assertEquals(new FloatField("float"), field);
    }

    @Test
    public void testGetFloatFieldException() {
        final FoxProDialect thisDialect = this.dialect;
        final IllegalArgumentException e =
                Assert.assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        thisDialect.createXBaseField("float", (byte) 'F', 21, 0);
                    }
                });
        Assert.assertEquals("A float has 20 chars", e.getMessage());
    }

    @Test
    public void testGetLogicalField() {
        final XBaseField<? super VisualFoxProAccess> field =
                this.dialect.createXBaseField("bool", (byte) 'L', 1, 0);
        Assert.assertEquals(new LogicalField("bool"), field);
    }

    @Test
    public void testGetLogicalFieldException() {
        final FoxProDialect thisDialect = this.dialect;
        final IllegalArgumentException e =
                Assert.assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        thisDialect.createXBaseField("bool", (byte) 'L', 2, 0);
                    }
                });
        Assert.assertEquals("A boolean has one char", e.getMessage());
    }

    @Test
    public void testGetMemoField() {
        final XBaseField<? super VisualFoxProAccess> field =
                this.dialect.createXBaseField("memo", (byte) 'M', 10, 0);
        Assert.assertEquals(new MemoField("memo"), field);
    }

    @Test
    public void testGetMemoFieldException() {
        final FoxProDialect thisDialect = this.dialect;
        final IllegalArgumentException e =
                Assert.assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        thisDialect.createXBaseField("memo", (byte) 'M', 5, 0);
                    }
                });
        Assert.assertEquals("A memo offset has 10 chars, was 5", e.getMessage());
    }

    @Test
    public void testGetNumericField() {
        final XBaseField<? super VisualFoxProAccess> field =
                this.dialect.createXBaseField("num", (byte) 'N', 8, 2);
        Assert.assertEquals(new NumericField("num", 8, 2), field);
    }

    @Test
    public void testGetUnknownFieldException() {
        final FoxProDialect thisDialect = this.dialect;
        final IllegalArgumentException e =
                Assert.assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        thisDialect.createXBaseField("unk", (byte) 'Z', 5, 0);
                    }
                });
        Assert.assertEquals("'Z' (90) is not a dbf field type", e.getMessage());
    }


    @Test
    public void testGetType() {
        Assert.assertEquals(XBaseFileTypeEnum.FoxPro2xMemo, this.dialect.getType());
    }

    @Test
    public void testGetMetaDataLength() {
        Assert.assertEquals(32, this.dialect.getMetaDataLength());
    }

    @Test
    public void testGetFieldDescriptorLength() {
        Assert.assertEquals(32, this.dialect.getFieldDescriptorLength());
    }

    @Test
    public void testGetOptionalLength() {
        Assert.assertEquals(0, this.dialect.getOptionalLength());
    }

    @Test
    public void testGetAccess() {
        Assert.assertEquals(this.access, this.dialect.getAccess());
    }

    @Test
    public void testGetInternalReaderFactory() {
        this.dialect.getInternalReaderFactory();
    }

    @Test
    public void testGetInternalWriterFactory() {
        this.dialect.getInternalWriterFactory(
        );
    }
}