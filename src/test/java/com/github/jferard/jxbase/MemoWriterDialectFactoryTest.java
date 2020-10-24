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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.dialect.db3.DB3DialectBuilder;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.dialect.db4.DB4DialectBuilder;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialectBuilder;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DB2Dialect.class, DB3DialectBuilder.class, DB4DialectBuilder.class,
        VisualFoxProDialectBuilder.class})
public class MemoWriterDialectFactoryTest {
    @Test
    public void createDB2Dialect() {
        final DB2Dialect dialect = PowerMock.createMock(DB2Dialect.class);
        PowerMock.mockStatic(DB2Dialect.class);
        PowerMock.resetAll();

        EasyMock.expect(DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET))
                .andReturn(dialect);
        PowerMock.replayAll();

        final XBaseDialect<DB2Dialect, DB2Access> d =
                new WriterDialectFactoryAux(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET)
                        .createDB2Dialect();
        PowerMock.verifyAll();

        Assert.assertSame(dialect, d);
    }

    @Test
    public void createDB3Dialect() throws IOException {
        final DB3Dialect dialect = PowerMock.createMock(DB3Dialect.class);
        final DB3DialectBuilder factory = PowerMock.createMock(DB3DialectBuilder.class);
        PowerMock.mockStatic(DB3DialectBuilder.class);
        PowerMock.resetAll();

        EasyMock.expect(DB3DialectBuilder
                .create(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE))
                .andReturn(factory);
        EasyMock.expect(factory.writer("foo", null)).andReturn(factory);
        EasyMock.expect(factory.build()).andReturn(dialect);
        PowerMock.replayAll();

        final XBaseDialect<DB3Dialect, DB3Access> d =
                new WriterDialectFactoryAux(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET)
                        .createDB3Dialect(
                                JxBaseUtils.UTC_TIME_ZONE, "foo", null);
        PowerMock.verifyAll();

        Assert.assertSame(dialect, d);
    }

    @Test
    public void createDB4Dialect() throws IOException {
        final DB4Dialect dialect = PowerMock.createMock(DB4Dialect.class);
        final DB4DialectBuilder factory = PowerMock.createMock(DB4DialectBuilder.class);
        PowerMock.mockStatic(DB4DialectBuilder.class);
        PowerMock.resetAll();

        EasyMock.expect(DB4DialectBuilder
                .create(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE))
                .andReturn(factory);
        EasyMock.expect(factory.writer("foo", null)).andReturn(factory);
        EasyMock.expect(factory.build()).andReturn(dialect);
        PowerMock.replayAll();

        final XBaseDialect<DB4Dialect, DB4Access> d =
                new WriterDialectFactoryAux(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET)
                        .createDB4Dialect(
                                JxBaseUtils.UTC_TIME_ZONE, "foo", null);
        PowerMock.verifyAll();

        Assert.assertSame(dialect, d);
    }

    @Test
    public void createFoxProDialect() throws IOException {
        final VisualFoxProDialect dialect = PowerMock.createMock(VisualFoxProDialect.class);
        final VisualFoxProDialectBuilder factory =
                PowerMock.createMock(VisualFoxProDialectBuilder.class);
        PowerMock.mockStatic(VisualFoxProDialectBuilder.class);
        PowerMock.resetAll();

        EasyMock.expect(VisualFoxProDialectBuilder
                .create(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE))
                .andReturn(factory);
        EasyMock.expect(factory.writer("foo", null)).andReturn(factory);
        EasyMock.expect(factory.build()).andReturn(dialect);
        PowerMock.replayAll();

        final XBaseDialect<VisualFoxProDialect, VisualFoxProAccess> d =
                new WriterDialectFactoryAux(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET)
                        .createVisualFoxProDialect(
                                JxBaseUtils.UTC_TIME_ZONE, "foo", null);
        PowerMock.verifyAll();

        Assert.assertSame(dialect, d);
    }
}