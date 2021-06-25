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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.dialect.db3.DB3DialectFactory;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.dialect.db4.DB4DialectFactory;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialectFactory;
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
@PrepareForTest({DB2Dialect.class, DB3DialectFactory.class, DB4DialectFactory.class,
        VisualFoxProDialectFactory.class})
public class MemoReaderDialectFactoryTest {
    @Test
    public void createDB2Dialect() {
        final DB2Dialect dialect = PowerMock.createMock(DB2Dialect.class);
        PowerMock.mockStatic(DB2Dialect.class);
        PowerMock.resetAll();

        EasyMock.expect(DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET))
                .andReturn(dialect);
        PowerMock.replayAll();

        final XBaseDialect<DB2Access, DB2Dialect> d =
                new DialectFactoryAux(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET)
                        .createDB2Dialect();
        PowerMock.verifyAll();

        Assert.assertSame(dialect, d);
    }

    @Test
    public void createDB3Dialect() throws IOException {
        final DB3Dialect dialect = PowerMock.createMock(DB3Dialect.class);
        final DB3DialectFactory factory = PowerMock.createMock(DB3DialectFactory.class);
        PowerMock.mockStatic(DB3DialectFactory.class);
        PowerMock.resetAll();

        EasyMock.expect(DB3DialectFactory
                .create(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE))
                .andReturn(dialect);
        PowerMock.replayAll();

        final XBaseDialect<DB3Access, DB3Dialect> d =
                new DialectFactoryAux(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET)
                        .createDB3Dialect(
                                JxBaseUtils.UTC_TIME_ZONE);
        PowerMock.verifyAll();

        Assert.assertSame(dialect, d);
    }

    @Test
    public void createDB4Dialect() {
        final DB4Dialect dialect = PowerMock.createMock(DB4Dialect.class);
        final DB4DialectFactory factory = PowerMock.createMock(DB4DialectFactory.class);
        PowerMock.mockStatic(DB4DialectFactory.class);
        PowerMock.resetAll();

        EasyMock.expect(DB4DialectFactory
                .create(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE))
                .andReturn(dialect);
        PowerMock.replayAll();

        final XBaseDialect<DB4Access, DB4Dialect> d =
                new DialectFactoryAux(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET)
                        .createDB4Dialect(
                                JxBaseUtils.UTC_TIME_ZONE);
        PowerMock.verifyAll();

        Assert.assertSame(dialect, d);
    }

    @Test
    public void createFoxProDialect() {
        final VisualFoxProDialect dialect = PowerMock.createMock(VisualFoxProDialect.class);
        final VisualFoxProDialectFactory factory =
                PowerMock.createMock(VisualFoxProDialectFactory.class);
        PowerMock.mockStatic(VisualFoxProDialectFactory.class);
        PowerMock.resetAll();

        EasyMock.expect(VisualFoxProDialectFactory
                .create(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE))
                .andReturn(dialect);
        PowerMock.replayAll();

        final XBaseDialect<VisualFoxProAccess, VisualFoxProDialect> d =
                new DialectFactoryAux(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET)
                        .createVisualFoxProDialect(
                                JxBaseUtils.UTC_TIME_ZONE);
        PowerMock.verifyAll();

        Assert.assertSame(dialect, d);
    }
}