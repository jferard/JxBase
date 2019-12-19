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

package com.github.jferard.jxbase.dialect.foxpro.writer;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.dialect.db4.DB4DialectFactory;
import com.github.jferard.jxbase.dialect.foxpro.FoxProAccess;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialectFactory;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.GenericOptionalWriter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class FoxProOptionalWriterTest {
    @Test
    public void test() throws IOException {
        final FoxProDialectFactory factory =
                new FoxProDialectFactory(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE);
        final XBaseDialect<FoxProDialect, FoxProAccess> dialect = factory.build();
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final FoxProOptionalWriter gow = new FoxProOptionalWriter(dialect, bos, null, null);
        final byte[] bytes = new byte[100];
        Arrays.fill(bytes, (byte) 13);

        gow.write(new XBaseOptional() {
            @Override
            public int getLength() {
                return 100;
            }

            @Override
            public byte[] getBytes() {
                return bytes;
            }
        });
        Assert.assertArrayEquals(bytes, bos.toByteArray());
    }

}