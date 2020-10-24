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

package com.github.jferard.jxbase.tool;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialectBuilder;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

public class SQLiteQueryBuilderTest {
    private SQLQueryBuilder builder;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws IOException {
        final XBaseDialect<VisualFoxProDialect, VisualFoxProAccess> dialect = VisualFoxProDialectBuilder
                .create(XBaseFileTypeEnum.dBASE4SQLTable, JxBaseUtils.ASCII_CHARSET, TimeZone
                        .getDefault()).build();
        final List<XBaseField<?>> fields =
                Arrays.<XBaseField<?>>asList(TestHelper.fromStringRepresentation(dialect, "a,C,10,0"),
                        TestHelper.fromStringRepresentation(dialect, "b,D,8,0"),
                        TestHelper.fromStringRepresentation(dialect, "c,N,10,0"));
        this.builder = new SQLiteQueryBuilder("foo", fields);
    }

    @Test
    public void testDrop() {
        Assert.assertEquals("DROP TABLE IF EXISTS \"foo\"", this.builder.dropTable());
    }

    @Test
    public void testCreate() throws IOException {
        Assert.assertEquals("CREATE TABLE \"foo\" (\n" +
                "    \"a\" text,\n" +
                "    \"b\" integer,\n" +
                "    \"c\" numeric\n" +
                ")", this.builder.createTable());
    }

    @Test
    public void testGetValuesSize() throws IOException {
        Assert.assertEquals(3, this.builder.getColumnsSize());
    }

    @Test
    public void testInsertValues() throws IOException {
        Assert.assertEquals("INSERT INTO \"foo\" VALUES (?, ?, ?)" +
                "", this.builder.insertValues());
    }
}