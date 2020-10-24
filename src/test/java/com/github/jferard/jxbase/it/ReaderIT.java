/*
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

package com.github.jferard.jxbase.it;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.XBaseReader;
import com.github.jferard.jxbase.XBaseReaderFactory;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class ReaderIT {
    @Test
    public void test1() throws IOException, ParseException {
        final Charset stringCharset = Charset.forName("Cp866");

        final String tableName = TestHelper.getResourceTableName("data1/gds_im.dbf");

        XBaseRecord rec;
        final String[] g311s = new String[]{"21", "192", "376", "1318", "189"};

        final XBaseReader<?, ?> reader = XBaseReaderFactory.createReader(tableName, stringCharset);
        try {
            final XBaseMetadata meta = reader.getMetadata();

            Assert.assertEquals(5, meta.get("recordsQty"));
            Assert.assertEquals(3, meta.getFileTypeByte());
            Assert.assertEquals(929, meta.getFullHeaderLength());
            Assert.assertEquals(479, meta.getOneRecordLength());
            Assert.assertEquals(TestHelper.createDate(112, 8, 26), meta.get("updateDate"));
            Assert.assertEquals(28, reader.getFieldDescriptorArray().getFields().size());

            int recCounter = 0;
            while ((rec = reader.read()) != null) {
                Assert.assertFalse(rec.isDeleted());
                Assert.assertEquals(recCounter + 1, rec.getRecordNumber());
                Assert.assertEquals(g311s[recCounter], rec.getMap().get("G311"));

                recCounter++;
                assertEquals(recCounter, rec.getRecordNumber());
            }
        } finally {
            reader.close();
        }
    }

    @Test
    public void test2() throws IOException, ParseException {
        final Charset stringCharset = Charset.forName("Cp866");

        final String tableName = TestHelper.getResourceTableName("data1/tir_im.dbf");

        XBaseRecord rec;
        final XBaseReader<?, ?> reader = XBaseReaderFactory.createReader(tableName, stringCharset);
        try {
            final XBaseMetadata meta = reader.getMetadata();

            Assert.assertEquals(1, meta.get("recordsQty"));
            Assert.assertEquals(3, meta.getFileTypeByte());
            Assert.assertEquals(3777, meta.getFullHeaderLength());
            Assert.assertEquals(2763, meta.getOneRecordLength());
            Assert.assertEquals(TestHelper.createDate(112, 8, 27), meta.get("updateDate"));
            Assert.assertEquals(117, reader.getFieldDescriptorArray().getFields().size());

            int recCounter = 0;
            while ((rec = reader.read()) != null) {
                Assert.assertFalse(rec.isDeleted());
                Assert.assertEquals(recCounter+1, rec.getRecordNumber());
                Assert.assertEquals("WSK00000001218733", rec.getMap().get("VIN"));

                recCounter++;
                assertEquals(recCounter, rec.getRecordNumber());
            }
        } finally {
            reader.close();
        }
    }

}
