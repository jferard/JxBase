/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    public void readFullyAVoidArray() throws Exception {
        final byte[] bs = new byte[0];
        Assert.assertEquals(0, IOUtils.readFully(this.in, bs));

        final byte[] ret = new byte[0];
        Assert.assertArrayEquals(ret, bs);
    }

    @Test
    public void readFullyASmallArray() throws Exception {
        final byte[] bs = new byte[5];
        Assert.assertEquals(5, IOUtils.readFully(this.in, bs));

        Assert.assertArrayEquals(new byte[]{15, 15, 15, 15, 15}, bs);
    }

    @Test
    public void readFullyAFullSizeArray() throws Exception {
        final byte[] bs = new byte[10];
        Assert.assertEquals(10, IOUtils.readFully(this.in, bs));

        Assert.assertArrayEquals(new byte[]{15, 15, 15, 15, 15, 15, 15, 15, 15, 15}, bs);
    }

    @Test
    public void readFullyABigArray() throws Exception {
        final byte[] bs = new byte[100];
        Assert.assertEquals(10, IOUtils.readFully(this.in, bs));

        final byte[] ret = new byte[100];
        Arrays.fill(ret, 0, 10, (byte) 15);
        Assert.assertArrayEquals(ret, bs);
    }

    @Test
    public void readFullyWithOffsetBigArray() throws Exception {
        final byte[] bs = new byte[100];
        Assert.assertEquals(10, IOUtils.readFully(this.in, bs, 5, 10));

        final byte[] ret = new byte[100];
        Arrays.fill(ret, 5, 15, (byte) 15);
        Assert.assertArrayEquals(ret, bs);
    }

    @Test
    public void readFully() throws Exception {
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
}