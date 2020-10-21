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

package com.github.jferard.jxbase.dialect.foxpro.reader;

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.dialect.foxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.foxpro.VisualFoxProDialect;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FoxProOptionalReaderTest {
    @Test(expected = IOException.class)
    public void testFail() throws IOException {
        final FoxProOptionalReader<VisualFoxProDialect, VisualFoxProAccess> reader =
                new FoxProOptionalReader<VisualFoxProDialect, VisualFoxProAccess>(
                        new VisualFoxProDialect(XBaseFileTypeEnum.VisualFoxPro, null),
                        new ByteArrayInputStream(new byte[]{}), null, null);
        reader.read();
    }

    @Test
    public void test() throws IOException {
        final FoxProOptionalReader<VisualFoxProDialect, VisualFoxProAccess> reader =
                new FoxProOptionalReader<VisualFoxProDialect, VisualFoxProAccess>(
                        new VisualFoxProDialect(XBaseFileTypeEnum.VisualFoxPro, null),
                        new ByteArrayInputStream(new byte[263]), null, null);
        final XBaseOptional actual = reader.read();
        Assert.assertEquals(263, actual.getLength());
        Assert.assertArrayEquals(new byte[263], actual.getBytes());
    }
}