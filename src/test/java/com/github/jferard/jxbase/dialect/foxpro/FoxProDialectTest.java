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

package com.github.jferard.jxbase.dialect.foxpro;

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.LogicalField;
import com.github.jferard.jxbase.dialect.db2.field.NumericField;
import com.github.jferard.jxbase.dialect.db3.field.DateField;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.db4.field.FloatField;
import com.github.jferard.jxbase.dialect.foxpro.field.NullFlagsField;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.util.Collections;

public class FoxProDialectTest {

    private FoxProDialect dialect;
    private FoxProAccess access;

    @Before
    public void setUp() {
        this.access = PowerMock.createMock(FoxProAccess.class);
        this.dialect = new FoxProDialect(XBaseFileTypeEnum.VisualFoxPro, this.access);
    }

    @Test
    public void testGetCharacterField() {
        final XBaseField<? super FoxProAccess> field =
                this.dialect.getXBaseField("char", (byte) 'C', 10, 0);
        Assert.assertEquals(new CharacterField("char", 10), field);
    }

    @Test
    public void testGetDateField() {
        final XBaseField<? super FoxProAccess> field =
                this.dialect.getXBaseField("date", (byte) 'D', 8, 0);
        Assert.assertEquals(new DateField("date"), field);
    }

    @Test
    public void testGetFloatField() {
        final XBaseField<? super FoxProAccess> field =
                this.dialect.getXBaseField("float", (byte) 'F', 20, 0);
        Assert.assertEquals(new FloatField("float"), field);
    }

    @Test
    public void testGetLogicalField() {
        final XBaseField<? super FoxProAccess> field =
                this.dialect.getXBaseField("bool", (byte) 'L', 1, 0);
        Assert.assertEquals(new LogicalField("bool"), field);
    }

    @Test
    public void testGetMemoField() {
        final XBaseField<? super FoxProAccess> field =
                this.dialect.getXBaseField("memo", (byte) 'M', 4, 0);
        Assert.assertEquals(new MemoField("memo"), field);
    }

    @Test
    public void testGetNumericField() {
        final XBaseField<? super FoxProAccess> field =
                this.dialect.getXBaseField("num", (byte) 'N', 8, 2);
        Assert.assertEquals(new NumericField("num", 8, 2), field);
    }

    @Test
    public void testGetNullFlagsField() {
        final XBaseField<? super FoxProAccess> field =
                this.dialect.getXBaseField("null", (byte) '0', 8, 0);
        Assert.assertEquals(new NullFlagsField("null", 8), field);
    }

    @Test
    public void testGetType() {
        Assert.assertEquals(XBaseFileTypeEnum.VisualFoxPro, this.dialect.getType());
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
        Assert.assertEquals(263, this.dialect.getOptionalLength());
    }

    @Test
    public void testGetAccess() {
        Assert.assertEquals(this.access, this.dialect.getAccess());
    }

    @Test
    public void testGetInternalReaderFactory() {
        this.dialect.getInternalReaderFactory("a", JxBaseUtils.ASCII_CHARSET);
    }

    @Test
    public void testGetInternalWriterFactory() {
        this.dialect.getInternalWriterFactory("a", JxBaseUtils.ASCII_CHARSET,
                Collections.<String, Object>emptyMap());
    }
}