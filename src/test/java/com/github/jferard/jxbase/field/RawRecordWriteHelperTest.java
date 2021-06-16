/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

package com.github.jferard.jxbase.field;

import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RawRecordWriteHelperTest {
    @Test
    public void testWriteEmpties() throws IOException {
        final RawRecordWriteHelper writeHelper =
                new RawRecordWriteHelper(JxBaseUtils.ASCII_CHARSET);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeHelper.writeEmpties(bos, 4);
        Assert.assertArrayEquals(new byte[]{0x20, 0x20, 0x20, 0x20}, bos.toByteArray());
    }

    @Test
    public void testWrite() throws IOException {
        final RawRecordWriteHelper writeHelper =
                new RawRecordWriteHelper(JxBaseUtils.ASCII_CHARSET);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeHelper.write(bos, new byte[]{'a', 'b', 'c'});
        Assert.assertArrayEquals(new byte[]{'a', 'b', 'c'}, bos.toByteArray());
    }

    @Test
    public void testWriteLengthShort() throws IOException {
        final RawRecordWriteHelper writeHelper =
                new RawRecordWriteHelper(JxBaseUtils.ASCII_CHARSET);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeHelper.write(bos, "abc", 8);
        Assert.assertArrayEquals(new byte[]{'a', 'b', 'c', 0x20, 0x20, 0x20, 0x20, 0x20},
                bos.toByteArray());
    }

    @Test
    public void testWriteLengthLong() throws IOException {
        final RawRecordWriteHelper writeHelper =
                new RawRecordWriteHelper(JxBaseUtils.ASCII_CHARSET);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeHelper.write(bos, "abcdefghijklm", 8);
        Assert.assertArrayEquals(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'}, bos.toByteArray());
    }
}