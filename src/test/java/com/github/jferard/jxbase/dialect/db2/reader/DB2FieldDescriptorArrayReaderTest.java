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

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;

public class DB2FieldDescriptorArrayReaderTest {
    @Test(expected = IOException.class)
    public void testVoidStream() throws IOException {
        final DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect> arrayReader =
                new DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect>(
                        DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET),
                        new ByteArrayInputStream(new byte[]{}));
        arrayReader.read();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStream() throws IOException {
        final DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect> arrayReader =
                new DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect>(
                        DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET),
                        new ByteArrayInputStream(
                                "abcdefghijklmnop".getBytes(JxBaseUtils.ASCII_CHARSET)));
        arrayReader.read();
    }

    @Test(expected = IOException.class)
    public void testStreamWithoutTerminator() throws IOException {
        final DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect> arrayReader =
                new DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect>(
                        DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET),
                        new ByteArrayInputStream(
                                "abcdefghijkCmnop".getBytes(JxBaseUtils.ASCII_CHARSET)));
        arrayReader.read();
    }

    @Test(expected = IOException.class)
    public void testStreamWithoutBadTerminator() throws IOException {
        final DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect> arrayReader =
                new DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect>(
                        DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET),
                        new ByteArrayInputStream(
                                "abcdefghijkCmnopQ".getBytes(JxBaseUtils.ASCII_CHARSET)));
        arrayReader.read();
    }

    @Test(expected = IOException.class)
    public void testStreamIncorrectRemainingFields() throws IOException {
        final DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect> arrayReader =
                new DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect>(
                        DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET),
                        new ByteArrayInputStream(
                                "abcdefghijkC\155xx\0\015".getBytes(JxBaseUtils.ASCII_CHARSET)));
        arrayReader.read();
    }

    @Test
    public void test31Fields() throws IOException {
        final StringBuilder sb = new StringBuilder();
        for (int i=0; i<31; i++) {
            sb.append("abcdefghijkC\155xx\0");
        }
        sb.append('\015');
        for (int j=0; j<16; j++) {
            sb.append('\0');
        }
        Assert.assertEquals(32*16+1, sb.toString().length());
        final byte[] bytes = sb.toString().getBytes(JxBaseUtils.ASCII_CHARSET);
        final DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect> arrayReader =
                new DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect>(
                        DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET),
                        new ByteArrayInputStream(
                                bytes));
        final XBaseFieldDescriptorArray<DB2Access> array = arrayReader.read();
        Assert.assertEquals(32*16+1, array.getArrayLength());

    }

    @Test
    public void test32Fields() throws IOException {
        final StringBuilder sb = new StringBuilder();
        for (int i=0; i<32; i++) {
            sb.append("abcdefghijkC\155xx\0");
        }
        sb.append('\015');
        Assert.assertEquals(32*16+1, sb.toString().length());
        final byte[] bytes = sb.toString().getBytes(JxBaseUtils.ASCII_CHARSET);
        final DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect> arrayReader =
                new DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect>(
                        DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET),
                        new ByteArrayInputStream(
                                bytes));
        final XBaseFieldDescriptorArray<DB2Access> array = arrayReader.read();
        Assert.assertEquals(32*16+1, array.getArrayLength());

    }

    @Test(expected = IOException.class)
    public void test32FieldsButNoTerminator() throws IOException {
        final StringBuilder sb = new StringBuilder();
        for (int i=0; i<32; i++) {
            sb.append("abcdefghijkC\155xx\0");
        }
        sb.append('\0');
        Assert.assertEquals(32*16+1, sb.toString().length());
        final byte[] bytes = sb.toString().getBytes(JxBaseUtils.ASCII_CHARSET);
        final DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect> arrayReader =
                new DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect>(
                        DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET),
                        new ByteArrayInputStream(
                                bytes));
        final XBaseFieldDescriptorArray<DB2Access> array = arrayReader.read();
    }

    @Test(expected = IOException.class)
    public void test31FieldsError() throws IOException {
        final StringBuilder sb = new StringBuilder();
        for (int i=0; i<31; i++) {
            sb.append("abcdefghijkC\155xx\0");
        }
        sb.append('\015');
        sb.append("abcdefghijkC\155xx\0");
        Assert.assertEquals(32*16+1, sb.toString().length());
        final byte[] bytes = sb.toString().getBytes(JxBaseUtils.ASCII_CHARSET);
        final DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect> arrayReader =
                new DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect>(
                        DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET),
                        new ByteArrayInputStream(
                                bytes));
        final XBaseFieldDescriptorArray<DB2Access> array = arrayReader.read();
        Assert.assertEquals(32*16+1, array.getArrayLength());

    }

    @Test
    public void testStream() throws IOException {
        final byte[] bytes = new byte[32 * 16 + 1];
        System.arraycopy("abcdefghijkCmnop".getBytes(JxBaseUtils.ASCII_CHARSET), 0, bytes, 0, 16);
        bytes[16] = JxBaseUtils.HEADER_TERMINATOR;
        final DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect> arrayReader =
                new DB2FieldDescriptorArrayReader<DB2Access, DB2Dialect>(
                        DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET),
                        new ByteArrayInputStream(bytes));
        final XBaseFieldDescriptorArray<DB2Access> array = arrayReader.read();
        Assert.assertEquals(110, array.getRecordLength());
        Assert.assertEquals(513, array.getArrayLength());
        Assert.assertEquals(Collections.singletonList(new CharacterField("abcdefghijk", 109)),
                array.getFields());
    }
}