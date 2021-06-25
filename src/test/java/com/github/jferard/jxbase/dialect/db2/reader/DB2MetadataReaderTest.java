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

package com.github.jferard.jxbase.dialect.db2.reader;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;

public class DB2MetadataReaderTest {
    @Test(expected = IOException.class)
    public void testVoidHeader() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        final DB2MetadataReader reader = new DB2MetadataReader(
                DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET),
                inputStream);
        reader.read();
    }

    @Test
    public void test() throws IOException {
        final byte[] bytes = new byte[520];
        System.arraycopy(
                new byte[]{0x03, 1, 0, 101, 2, 3, 12, 0, 48, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, 0, bytes, 0, 32);
        Assert.assertEquals(520, bytes.length);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        final DB2MetadataReader reader = new DB2MetadataReader(
                DB2Dialect.create(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET),
                inputStream);
        final GenericMetadata meta = reader.read();
        Assert.assertEquals(0x03, meta.getFileTypeByte());
        Assert.assertEquals(520, meta.getFullHeaderLength());
        Assert.assertEquals(12, meta.getOneRecordLength());
        Assert.assertEquals(TestHelper.newSet("updateDate", "recordsQty"), meta.keySet());
        final Calendar calendar = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        calendar.set(2001, 1, 3, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Assert.assertEquals(calendar.getTime(), meta.get("updateDate"));
        Assert.assertEquals(1, meta.get("recordsQty"));
    }
}