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

package com.github.jferard.jxbase.dialect.db4.writer;

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
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

public class DB4MetadataWriterTest {

    private RandomAccessFile file;
    private ByteArrayOutputStream out;
    private DB4MetadataWriter<DB4Dialect, DB4Access> writer;

    @Before
    public void setUp() {
        this.file = PowerMock.createMock(RandomAccessFile.class);
        this.out = new ByteArrayOutputStream();
        this.writer = new DB4MetadataWriter<DB4Dialect, DB4Access>(null, this.file, this.out,
                JxBaseUtils.ASCII_CHARSET);
    }

    @Test
    public void write() throws IOException {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("updateDate", new Date(0));
        meta.put("recordsQty", 1);
        meta.put("uncompletedTxFlag", 2);
        meta.put("encryptionFlag", 3);
        meta.put("mdxFlag", 3);
        meta.put("languageDriverId", 4);
        this.writer.write(new GenericMetadata(XBaseFileTypeEnum.dBASE4.toByte(), 128, 11, meta));
        Assert.assertArrayEquals(
                new byte[]{4, 70, 1, 1, 1, 0, 0, 0, -128, 0, 11, 0, 0, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 3, 4, 0, 0}, this.out.toByteArray());
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