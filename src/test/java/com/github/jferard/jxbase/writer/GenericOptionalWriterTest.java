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

package com.github.jferard.jxbase.writer;

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.dialect.db4.DB4DialectFactory;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialectFactory;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GenericOptionalWriterTest {
    @Test
    public void testDB4() throws IOException {
        final DB4Dialect dialect =
                DB4DialectFactory.create(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final GenericOptionalWriter<DB4Access, DB4Dialect> gow =
                new GenericOptionalWriter<DB4Access, DB4Dialect>(dialect, bos, null, null);
        gow.write(null);
        Assert.assertArrayEquals(new byte[0], bos.toByteArray());
    }

    @Test
    public void testVisualFoxPro() throws IOException {
        final VisualFoxProDialect dialect =
                VisualFoxProDialectFactory
                        .create(XBaseFileTypeEnum.VisualFoxPro, JxBaseUtils.ASCII_CHARSET,
                                JxBaseUtils.UTC_TIME_ZONE);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final GenericOptionalWriter<VisualFoxProAccess, VisualFoxProDialect> gow =
                new GenericOptionalWriter<VisualFoxProAccess, VisualFoxProDialect>(dialect, bos,
                        null, null);
        gow.write(null);
        Assert.assertArrayEquals(new byte[263], bos.toByteArray());
    }
}