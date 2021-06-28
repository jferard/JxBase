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

package com.github.jferard.jxbase.dialect.db4.reader;

import com.github.jferard.jxbase.DialectFactory;
import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.dialect.db4.DB4Utils;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;

public class DB4MetadataReaderTest {
    @Test(expected = IOException.class)
    public void testVoidHeader() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        @SuppressWarnings("unchecked") final XBaseDialect<DB4Access, DB4Dialect> dialect =
                (XBaseDialect<DB4Access, DB4Dialect>) DialectFactory
                        .getDialect(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET);
        final DB4MetadataReader reader = new DB4MetadataReader(dialect,
                inputStream);
        reader.read();
    }

    @Test
    public void test() throws IOException {
        final byte[] bytes =
                {0x04, 101, 2, 3, 1, 0, 0, 0, 48, 0, 12, 0, 101, 102, 103, 104, 105, 106, 107, 108,
                        109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120};
        Assert.assertEquals(32, bytes.length);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        @SuppressWarnings("unchecked") final XBaseDialect<DB4Access, DB4Dialect> dialect =
                (XBaseDialect<DB4Access, DB4Dialect>) DialectFactory
                        .getDialect(XBaseFileTypeEnum.dBASE4, JxBaseUtils.ASCII_CHARSET);
        final DB4MetadataReader reader = new DB4MetadataReader(dialect, inputStream);
        final GenericMetadata meta = reader.read();
        Assert.assertEquals(0x04, meta.getFileTypeByte());
        Assert.assertEquals(XBaseFileTypeEnum.dBASE4, meta.getFileType());
        Assert.assertEquals(48, meta.getFullHeaderLength());
        Assert.assertEquals(12, meta.getOneRecordLength());
        Assert.assertEquals(
                TestHelper.newSet(DB4Utils.META_UPDATE_DATE, DB4Utils.META_RECORDS_QTY,
                        DB4Utils.META_ENCRYPTION_FLAG, DB4Utils.META_UNCOMPLETED_TX_FLAG,
                        DB4Utils.META_MDX_FLAG, DB4Utils.META_LANGUAGE_DRIVER_ID), meta.keySet());
        final Calendar calendar = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        calendar.set(2001, 1, 3, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Assert.assertEquals(calendar.getTime(), meta.get(DB4Utils.META_UPDATE_DATE));
        Assert.assertEquals(1, meta.get(DB4Utils.META_RECORDS_QTY));
        Assert.assertEquals((byte) 103, meta.get(DB4Utils.META_UNCOMPLETED_TX_FLAG));
        Assert.assertEquals((byte) 104, meta.get(DB4Utils.META_ENCRYPTION_FLAG));
        Assert.assertEquals((byte) 117, meta.get(DB4Utils.META_MDX_FLAG));
        Assert.assertEquals((byte) 118, meta.get(DB4Utils.META_LANGUAGE_DRIVER_ID));
    }

}