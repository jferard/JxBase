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

package com.github.jferard.jxbase.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitUtilsTest {
    @Test
    public void testWriteLEByte4_1() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        BytesUtils.writeLEByte4(out, 1);
        Assert.assertArrayEquals(new byte[] {1, 0, 0, 0}, out.toByteArray());
    }

    @Test
    public void testWriteLEByte4_256() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        BytesUtils.writeLEByte4(out, 256);
        Assert.assertArrayEquals(new byte[] {0, 1, 0, 0}, out.toByteArray());
    }

    @Test
    public void testWriteLEByte8_huge() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        BytesUtils.writeLEByte8(out, 12345678910111314L);
        Assert.assertArrayEquals(new byte[] {84, -36, 43, 0, 82, -66, -14, 93}, out.toByteArray());
    }

    @Test
    public void testMemSet() {
        final byte[] bytes = new byte[5];
        BytesUtils.memset(bytes, 'a');
        Assert.assertArrayEquals(new byte[] {97, 97, 97, 97, 97}, bytes);
    }
}