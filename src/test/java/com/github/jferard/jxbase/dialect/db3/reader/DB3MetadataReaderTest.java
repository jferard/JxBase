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

package com.github.jferard.jxbase.dialect.db3.reader;

import com.github.jferard.jxbase.DialectFactory;
import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;

public class DB3MetadataReaderTest {
    @Test(expected = IOException.class)
    public void testVoidHeader() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        @SuppressWarnings("unchecked")
        final DB3MetadataReader reader = new DB3MetadataReader(
                (XBaseDialect<DB3Dialect, DB3Access>) DialectFactory
                        .getDialect(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET),
                inputStream);
        reader.read();
    }

    @Test
    public void test() throws IOException {
        final byte[] bytes =
                {0x03, 101, 2, 3, 1, 0, 0, 0, 48, 0, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0};
        Assert.assertEquals(32, bytes.length);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        @SuppressWarnings("unchecked")
        final XBaseDialect<DB3Dialect, DB3Access> dialect =
                (XBaseDialect<DB3Dialect, DB3Access>) DialectFactory
                        .getDialect(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET);
        final DB3MetadataReader reader = new DB3MetadataReader(dialect, inputStream);
        final GenericMetadata meta = reader.read();
        Assert.assertEquals(0x03, meta.getFileTypeByte());
        Assert.assertEquals(48, meta.getFullHeaderLength());
        Assert.assertEquals(12, meta.getOneRecordLength());
        Assert.assertEquals(TestHelper.newSet("updateDate", "recordsQty"), meta.keySet());
        final Calendar calendar = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        calendar.set(2001, 1, 3, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Assert.assertEquals(calendar.getTime(), meta.get("updateDate"));
        Assert.assertEquals(1, meta.get("recordsQty"));
    }
}