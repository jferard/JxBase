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

package com.github.jferard.jxbase.memo;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.*;

public class RawMemoReaderTest {
    @Test
    public void test() {
        final byte[] bytes = new byte[1024];
        for (int i=0; i<1024; i++) {
            bytes[i] = (byte) i;
        }
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        final RawMemoReader reader = new RawMemoReader(buffer, 512, 16);
        final byte[] temp = reader.read(0); // header
        Assert.assertArrayEquals(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, temp);
        final byte[] temp2 = reader.read(1, 10, 5);
        Assert.assertArrayEquals(new byte[] {26, 27, 28, 29, 30}, temp2);
    }
}