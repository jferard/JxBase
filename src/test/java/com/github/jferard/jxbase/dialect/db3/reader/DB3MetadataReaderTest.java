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

package com.github.jferard.jxbase.dialect.db3.reader;

import com.github.jferard.jxbase.DialectFactory;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DB3MetadataReaderTest {
    @Test(expected = IOException.class)
    public void testVoidHeader() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        final DB3MetadataReader reader = new DB3MetadataReader(DialectFactory
                .getNoMemoDialect(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET),
                inputStream);
        final GenericMetadata meta = reader.read();
    }

    @Test
    public void test() throws IOException {
        final byte[] bytes =
                {0x03, 1, 2, 3, 1, 0, 0, 0, 48, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0};
        Assert.assertEquals(32, bytes.length);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        final DB3MetadataReader reader = new DB3MetadataReader(DialectFactory
                .getNoMemoDialect(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET),
                inputStream);
        final GenericMetadata meta = reader.read();
        System.out.println(meta);
    }
}