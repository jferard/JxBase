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

/**
 * Util to handle bytes
 */
public class BytesUtils {
    /**
     * @param b1 least significant byte
     * @param b2 most significant byte
     * @return the int
     */
    public static int makeLEInt(final byte b1, final byte b2) {
        return ((b1) & 0x00FF) + ((b2 << 8) & 0xFF00);
    }

    /**
     * @param b1 least significant byte
     * @param b2 2nd least significant byte
     * @param b3 2nd most significant byte
     * @param b4 most significant byte
     * @return the int
     */
    public static int makeLEInt(final byte b1, final byte b2, final byte b3, final byte b4) {
        return ((b1) & 0x000000FF) + ((b2 << 8) & 0x0000FF00) + ((b3 << 16) & 0x00FF0000) +
                ((b4 << 24) & 0xFF000000);
    }

    /**
     * @param i the integer to "parse"
     * @return a 4 bytes array, the *least* significant byte first
     */
    public static byte[] makeLEByte4(final int i) {
        return new byte[]{(byte) (i & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) ((i >> 16) & 0xFF),
                (byte) ((i >> 24) & 0xFF)};
    }

    /**
     * @param i the integer to "parse"
     * @return a 4 bytes array, the *most* significant byte first
     */
    public static byte[] makeBEByte4(final int i) {
        return new byte[]{(byte) ((i >> 24) & 0xFF), (byte) ((i >> 16) & 0xFF),
                (byte) ((i >> 8) & 0xFF),
                (byte) (i & 0xFF)};
    }

    /**
     * Write an int as 2 bytes array, little endian
     *
     * @param out the output stream
     * @param i   the int
     * @throws IOException
     */
    public static void writeLEByte2(final OutputStream out, final int i) throws IOException {
        out.write(i & 0xFF);
        out.write((i >> 8) & 0xFF);
    }

    /**
     * Same as makeLEByte4
     *
     * @param out the output stream
     * @param i   the int
     * @throws IOException
     */
    public static void writeLEByte4(final OutputStream out, final int i) throws IOException {
        out.write(i & 0xFF);
        out.write((i >> 8) & 0xFF);
        out.write((i >> 16) & 0xFF);
        out.write((i >> 24) & 0xFF);
    }

    /**
     * Write an int as 8 bytes array, little endian
     *
     * @param out the output stream
     * @param l   the long
     * @throws IOException
     */
    public static void writeLEByte8(final OutputStream out, final long l) throws IOException {
        writeLEByte4(out, (int) (l / 0x100000000L));
        writeLEByte4(out, (int) (l % 0x100000000L));
    }

    /**
     * @param buffer the buffer to fill
     * @param value  the value to set
     */
    public static void memset(final byte[] buffer, final int value) {
        // this approach is a bit faster than Arrays.fill,
        // because there is no rangeCheck
        final byte b = (byte) value;
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = b;
        }
    }

    /**
     * Write 0's to the output stream.
     * @param out the output stream
     * @param zeroCount the number of zeroes to write
     * @throws IOException
     */
    public static void writeZeroes(final OutputStream out, final int zeroCount) throws IOException {
        for (int i = 0; i < zeroCount; i++) {
            out.write(0x00);
        }
    }

    public static void writeEmpties(final OutputStream out, final int length) throws IOException {
        for (int i = 0; i < length; i++) {
            out.write(JxBaseUtils.EMPTY);
        }
    }

    private BytesUtils() {
    }
}
