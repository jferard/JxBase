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

package com.github.jferard.jxbase.memo;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;

public class RawMemoReaderTest {
    @Test
    public void test() throws IOException {
        final byte[] bytes = new byte[1024];
        for (int i=0; i<1024; i++) {
            bytes[i] = (byte) i;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        final RawMemoReader reader = new RawMemoReader(buffer, 512, 16, null);
        final byte[] temp = reader.read(0); // header
        final byte[] temp2 = reader.read(1, 10, 5);
        try {
            reader.close();
            Assert.fail();
        } catch (final NullPointerException e) {
            // pass
        }

        Assert.assertArrayEquals(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, temp);
        Assert.assertArrayEquals(new byte[] {26, 27, 28, 29, 30}, temp2);
    }
}