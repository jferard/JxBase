/*
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

import java.io.IOException;
import java.io.OutputStream;

public class BitUtils {
    public static int makeInt(final byte b1, final byte b2) {
        return ((b1) & 0xFF) + ((b2 << 8) & 0x0000FF00);
    }

    public static int makeInt(final byte b1, final byte b2, final byte b3, final byte b4) {
        return ((b1) & 0xFF) + ((b2 << 8) & 0x0000FF00) + ((b3 << 16) & 0x00FF0000) +
                ((b4 << 24) & 0xFF000000);
    }

    public static byte[] makeByte4(final int i) {
        final byte[] b = {(byte) (i & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) ((i >> 16) & 0xFF),
                (byte) ((i >> 24) & 0xFF)};
        return b;
    }

    public static void writeLEByte4(final OutputStream out, final int i) throws IOException {
        out.write(i & 0xFF);
        out.write((i >> 8) & 0xFF);
        out.write((i >> 16) & 0xFF);
        out.write((i >> 24) & 0xFF);
    }

    public static void writeLEByte2(final OutputStream out, final int i) throws IOException {
        out.write(i & 0xFF);
        out.write((i >> 8) & 0xFF);
    }

    public static void writeZeroes(final OutputStream out, final int zeroCount) throws IOException {
        for (int i = 0; i < zeroCount; i++) {
            out.write(0x00);
        }
    }

    public static byte[] makeByte2(final int i) {
        final byte[] b = {(byte) (i & 0xFF), (byte) ((i >> 8) & 0xFF)};
        return b;
    }

    public static void memset(final byte[] bytes, final int value) {
        // this approach is a bit faster than Arrays.fill,
        // because there is no rangeCheck
        final byte b = (byte) value;
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = b;
        }
        //		Arrays.fill(bytes, (byte)value);
    }

    public static void writeLEByte8(final OutputStream out, final long l) throws IOException {
        writeLEByte4(out, (int) (l / (long) (1 << 32)));
        writeLEByte4(out, (int) (l % (long) (1 << 32)));
    }

    public static void writeEmpties(final OutputStream out, final int length) throws IOException {
        for (int i = 0; i < length; i++) {
            out.write(JdbfUtils.EMPTY);
        }
    }
}
