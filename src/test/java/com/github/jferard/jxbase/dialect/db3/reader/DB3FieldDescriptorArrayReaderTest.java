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

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.dialect.db3.DB3DialectFactory;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;

public class DB3FieldDescriptorArrayReaderTest {
    private DB3Access db3Access;

    @Before
    public void setUp() {
        this.db3Access =
                 DB3DialectFactory.create(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE).getAccess();
    }

    @Test(expected = IOException.class)
    public void testVoidStream() throws IOException {
        final DB3FieldDescriptorArrayReader<DB3Dialect, DB3Access> arrayReader =
                new DB3FieldDescriptorArrayReader<DB3Dialect, DB3Access>(
                        new DB3Dialect(XBaseFileTypeEnum.dBASE3plus, this.db3Access),
                        new ByteArrayInputStream(new byte[]{}), null);
        arrayReader.read();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRandomStream() throws IOException {
        final DB3FieldDescriptorArrayReader<DB3Dialect, DB3Access> arrayReader =
                new DB3FieldDescriptorArrayReader<DB3Dialect, DB3Access>(
                        new DB3Dialect(XBaseFileTypeEnum.dBASE3plus, this.db3Access),
                        new ByteArrayInputStream("abcdefghijklmnop                "
                                .getBytes(JxBaseUtils.ASCII_CHARSET)), null);
        arrayReader.read();
    }

    @Test(expected = IOException.class)
    public void testStreamWithoutTerminator() throws IOException {
        final DB3FieldDescriptorArrayReader<DB3Dialect, DB3Access> arrayReader =
                new DB3FieldDescriptorArrayReader<DB3Dialect, DB3Access>(
                        new DB3Dialect(XBaseFileTypeEnum.dBASE3plus, this.db3Access),
                        new ByteArrayInputStream("abcdefghijkCmnop                                "
                                .getBytes(JxBaseUtils.ASCII_CHARSET)), null);
        arrayReader.read();
    }

    @Test(expected = IOException.class)
    public void testStreamWithoutBadTerminator() throws IOException {
        final DB3FieldDescriptorArrayReader<DB3Dialect, DB3Access> arrayReader =
                new DB3FieldDescriptorArrayReader<DB3Dialect, DB3Access>(
                        new DB3Dialect(XBaseFileTypeEnum.dBASE3plus, this.db3Access),
                        new ByteArrayInputStream("abcdefghijkCmnop                Q"
                                .getBytes(JxBaseUtils.ASCII_CHARSET)), null);
        arrayReader.read();
    }

    @Test
    public void testStream() throws IOException {
        final byte[] bytes = new byte[16 * 32 + 2];
        System.arraycopy("abcdefghijkCmnop                ".getBytes(JxBaseUtils.ASCII_CHARSET), 0,
                bytes, 0, 16);
        bytes[32] = JxBaseUtils.HEADER_TERMINATOR;
        final DB3FieldDescriptorArrayReader<DB3Dialect, DB3Access> arrayReader =
                new DB3FieldDescriptorArrayReader<DB3Dialect, DB3Access>(
                        new DB3Dialect(XBaseFileTypeEnum.dBASE3plus, this.db3Access),
                        new ByteArrayInputStream(bytes), null);
        final XBaseFieldDescriptorArray<DB3Access> array = arrayReader.read();
        Assert.assertEquals(1, array.getRecordLength());
        Assert.assertEquals(33, array.getArrayLength());
        Assert.assertEquals(Collections.singletonList(new CharacterField("abcdefghijk", 0)),
                array.getFields());
    }
}