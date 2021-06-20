/*
 * JxBase - Copyright (c) 2019-2020 Julien Férard
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

package com.github.jferard.jxbase.util;

import com.github.jferard.jxbase.TestHelper;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

/**
 * A test class for IOUtils.
 */
public class IOUtilsTest {
    private InputStream in;

    @Before
    public void setUp() {
        final byte[] bytes = new byte[10];
        Arrays.fill(bytes, (byte) 15);
        this.in = new ByteArrayInputStream(bytes);
    }

    @Test
    public void testToByteArray() throws IOException {
        Assert.assertArrayEquals(new byte[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15},
                IOUtils.toByteArray(this.in));
    }

    @Test
    public void testReadFullyAVoidArray() throws Exception {
        final byte[] bs = new byte[0];
        Assert.assertEquals(0, IOUtils.readFully(this.in, bs));

        final byte[] ret = new byte[0];
        Assert.assertArrayEquals(ret, bs);
    }

    @Test
    public void testReadFullyASmallArray() throws Exception {
        final byte[] bs = new byte[5];
        Assert.assertEquals(5, IOUtils.readFully(this.in, bs));

        Assert.assertArrayEquals(new byte[]{15, 15, 15, 15, 15}, bs);
    }

    @Test
    public void testReadFullyAFullSizeArray() throws Exception {
        final byte[] bs = new byte[10];
        Assert.assertEquals(10, IOUtils.readFully(this.in, bs));

        Assert.assertArrayEquals(new byte[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, bs);
    }

    @Test
    public void testReadFullyABigArray() throws Exception {
        final byte[] bs = new byte[100];
        Assert.assertEquals(10, IOUtils.readFully(this.in, bs));

        final byte[] ret = new byte[100];
        Arrays.fill(ret, 0, 10, (byte) 15);
        Assert.assertArrayEquals(ret, bs);
    }

    @Test
    public void testReadFullyWithOffsetBigArray() throws Exception {
        final byte[] bs = new byte[100];
        Assert.assertEquals(10, IOUtils.readFully(this.in, bs, 5, 10));

        final byte[] ret = new byte[100];
        Arrays.fill(ret, 5, 15, (byte) 15);
        Assert.assertArrayEquals(ret, bs);
    }

    @Test
    public void testReadFully() throws Exception {
        final byte[] bs = new byte[20];
        final InputStream is = PowerMock.createMock(InputStream.class);
        PowerMock.resetAll();

        EasyMock.expect(is.read(EasyMock.eq(bs), EasyMock.anyInt(), EasyMock.anyInt())).andReturn(1).times(10);
        PowerMock.replayAll();

        final int count = IOUtils.readFully(is, bs, 5, 10);
        PowerMock.verifyAll();

        Assert.assertEquals(10, count);
        Assert.assertArrayEquals(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, bs);
    }

    @Test
    public void testIsEndOfRecords() throws IOException {
        final ByteArrayInputStream bis =
                new ByteArrayInputStream(new byte[]{0x00, 0x20});
        Assert.assertFalse(IOUtils.isEndOfRecords(bis, 0x20));
        bis.read();
        Assert.assertTrue(IOUtils.isEndOfRecords(bis, 0x20));
    }

    @Test
    public void testAlreadyResettable() {
        final ByteArrayInputStream bis =
                new ByteArrayInputStream(new byte[]{0x00, 0x20});
        Assert.assertTrue(bis.markSupported());
        Assert.assertSame(bis, IOUtils.resettable(bis, 100));
    }

    @Test
    public void testNotResettable() throws IOException {
        final ByteArrayInputStream bis =
                new ByteArrayInputStream(new byte[]{0x00, 0x20});
        final InputStream is = new InputStream() {
            @Override
            public int read() {
                return bis.read();
            }
        };
        Assert.assertFalse(is.markSupported());
        final InputStream ris = IOUtils.resettable(is, 100);
        Assert.assertNotSame(is, ris);
        Assert.assertTrue(ris.markSupported());
        ris.mark(3);
        Assert.assertEquals(0x00, ris.read());
        Assert.assertEquals(0x20, ris.read());
        Assert.assertEquals(-1, ris.read());
        ris.reset();
        Assert.assertEquals(0x00, ris.read());
    }

    @Test
    public void testGetFile() throws IOException {
        final String tableName = TestHelper.getResourceTableName("data1/tir_im.dbf");
        Assert.assertTrue(IOUtils.getIgnoreCaseFile(tableName + ".dbf").exists());
        Assert.assertTrue(IOUtils.getIgnoreCaseFile(tableName + ".DBF").exists());
        Assert.assertNull(IOUtils.getIgnoreCaseFile("&é'(-è_çà'"));
    }

    @Test
    public void testGetExtension() throws IOException {
        final String pathDir = TestHelper.getResourcePath("data1");
        Assert.assertNull(IOUtils.getExtension(new File(pathDir)));
        Assert.assertNull(IOUtils.getExtension(new File(pathDir, "foo")));
        Assert.assertEquals("dbf", IOUtils.getExtension(new File(pathDir, "tir_im.dbf")));
    }

    @Test
    public void testRemoveExtension() throws IOException {
        final String pathDir = TestHelper.getResourcePath("data1");
        Assert.assertNull(IOUtils.removeExtension(new File(pathDir)));
        Assert.assertNull(IOUtils.removeExtension(new File(pathDir, "foo")));
        Assert.assertEquals(new File(pathDir, "tir_im").getAbsolutePath(), IOUtils.removeExtension(new File(pathDir, "tir_im.dbf")));
    }

    @Test
    public void testRemoveExtensionNoExt() throws IOException {
        final File foo = new File("LICENSE");
        Assert.assertEquals(foo.getAbsolutePath(), IOUtils.removeExtension(foo));
    }

    @Test
    public void testGetExtensionNoExt() throws IOException {
        final File foo = new File("LICENSE");
        Assert.assertEquals("", IOUtils.getExtension(foo));
    }
}