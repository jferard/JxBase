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

package com.github.jferard.jxbase.dialect.db3.writer;

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.dialect.db3.DB3Utils;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DB3MetadataWriterTest {

    private RandomAccessFile file;
    private ByteArrayOutputStream out;
    private DB3MetadataWriter<DB3Access, DB3Dialect> writer;

    @Before
    public void setUp() {
        this.file = PowerMock.createMock(RandomAccessFile.class);
        this.out = new ByteArrayOutputStream();
        this.writer = new DB3MetadataWriter<DB3Access, DB3Dialect>(null, this.file, this.out,
                JxBaseUtils.ASCII_CHARSET);
    }

    @Test
    public void write() throws IOException {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB3Utils.META_UPDATE_DATE, new Date(0));
        meta.put(DB3Utils.META_RECORDS_QTY, 1);
        this.writer
                .write(new GenericMetadata(XBaseFileTypeEnum.dBASE3plus.toByte(), 128, 11, meta));
        Assert.assertArrayEquals(
                new byte[]{3, 70, 1, 1, 1, 0, 0, 0, -128, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0}, this.out.toByteArray());
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