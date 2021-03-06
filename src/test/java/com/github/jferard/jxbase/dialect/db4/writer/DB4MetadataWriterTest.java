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

package com.github.jferard.jxbase.dialect.db4.writer;

import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.dialect.db4.DB4Utils;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DB4MetadataWriterTest {

    private RandomAccessFile file;
    private ByteArrayOutputStream out;
    private DB4MetadataWriter<DB4Access, DB4Dialect> writer;

    @Before
    public void setUp() {
        this.file = PowerMock.createMock(RandomAccessFile.class);
        this.out = new ByteArrayOutputStream();
        this.writer = new DB4MetadataWriter<DB4Access, DB4Dialect>(null, this.file, this.out,
                JxBaseUtils.ASCII_CHARSET);
    }

    @Test
    public void write() throws IOException {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB4Utils.META_UPDATE_DATE, new Date(0));
        meta.put(DB4Utils.META_RECORDS_QTY, 1);
        meta.put(DB4Utils.META_UNCOMPLETED_TX_FLAG, 2);
        meta.put(DB4Utils.META_ENCRYPTION_FLAG, 3);
        meta.put(DB4Utils.META_MDX_FLAG, 3);
        meta.put(DB4Utils.META_LANGUAGE_DRIVER_ID, 4);
        this.writer.write(new GenericMetadata(XBaseFileTypeEnum.dBASE4.toByte(), 128, 11, meta));
        Assert.assertArrayEquals(
                new byte[]{4, 70, 1, 1, 1, 0, 0, 0, -128, 0, 11, 0, 0, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 3, 4, 0, 0}, this.out.toByteArray());
    }

    @Test
    public void writeNull() throws IOException {
        final Map<String, Object> meta = new HashMap<String, Object>();
        this.writer.write(new GenericMetadata(XBaseFileTypeEnum.dBASE4.toByte(), 128, 11, meta));
        Assert.assertArrayEquals(
                new byte[]{4, 0, 0, 0, 0, 0, 0, 0, -128, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0}, this.out.toByteArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeObjectInsteadOfDate() throws IOException {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB4Utils.META_UPDATE_DATE, new Object());
        this.writer.write(new GenericMetadata(XBaseFileTypeEnum.dBASE4.toByte(), 128, 11, meta));
    }

    @Test
    public void writeCalendar() throws IOException {
        final Map<String, Object> meta = new HashMap<String, Object>();
        final Calendar cal = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        cal.setTimeInMillis(0);
        meta.put(DB4Utils.META_UPDATE_DATE, cal);
        this.writer.write(new GenericMetadata(XBaseFileTypeEnum.dBASE4.toByte(), 128, 11, meta));
        Assert.assertArrayEquals(
                new byte[]{4, 70, 1, 1, 0, 0, 0, 0, -128, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0}, this.out.toByteArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeObjectInsteadOfNumber() throws IOException {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB4Utils.META_RECORDS_QTY, new Object());
        this.writer.write(new GenericMetadata(XBaseFileTypeEnum.dBASE4.toByte(), 128, 11, meta));
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeObjectInsteadOfFlag() throws IOException {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB4Utils.META_UNCOMPLETED_TX_FLAG, new Object());
        this.writer.write(new GenericMetadata(XBaseFileTypeEnum.dBASE4.toByte(), 128, 11, meta));
    }

    @Test
    public void fixMetadata() throws IOException {
        this.writer.fixMetadata(15);
        final byte[] rq = Arrays.copyOfRange(this.out.toByteArray(), 3, 7);
        Assert.assertArrayEquals(new byte[]{15, 0, 0, 0}, rq);
    }

    @Test
    public void testClose() throws IOException {
        this.writer.close();
    }
}