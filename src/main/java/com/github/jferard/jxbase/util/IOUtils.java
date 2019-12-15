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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;


public class IOUtils {

    public static byte[] toByteArray(final InputStream input) throws IOException {
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();

            final byte[] buffer = new byte[1024 * 8];
            int count;
            while ((count = input.read(buffer)) > 0) {
                baos.write(buffer, 0, count);
            }

            return baos.toByteArray();
        } finally {
            input.close();
        }
    }

    /**
     * @param in the input stream
     * @param bs a byte array to fill from the input stream
     * @return 0 if b.length == 0 or the number or bytes really read, between 0 and b.length.
     * If b.length != 0, a return value lower than b.length means that the end of the stream was
     * reached.
     * @throws IOException
     */
    public static int readFully(final InputStream in, final byte[] bs) throws IOException {
        return IOUtils.readFully(in, bs, 0, bs.length);
    }

    /**
     * @param in     the input stream
     * @param bs     a byte array to fill from the input stream
     * @param offset first byte to write in bs
     * @param len    number of bytes to write in bs
     * @return 0 if b.length == 0 or the number or bytes really read, bewteen 0 and b.length.
     * If b.length != 0, a return value lower than b.length means that the end of the stream was
     * reached.
     * @throws IOException
     */
    public static int readFully(final InputStream in, final byte[] bs, final int offset,
                                final int len) throws IOException {
        int byteCount = in.read(bs, offset, len);
        if (byteCount <= -1) // length == 0 or EOF
        {
            return 0;
        }

        while (byteCount < len) {
            final int ret = in.read(bs, offset + byteCount, len - byteCount);
            if (ret == -1) {
                break;
            }
            byteCount += ret;
        }
        return byteCount;
    }

    /**
     * @param in  the input stream
     * @param len number of bytes to skip
     * @return 0 if b.length == 0 or the number or bytes really read, bewteen 0 and b.length.
     * If b.length != 0, a return value lower than b.length means that the end of the stream was
     * reached.
     * @throws IOException
     */
    public static long skipFully(final InputStream in, final int len) throws IOException {
        final long byteCount = in.skip(len);
        return byteCount;
    }

    /**
     * @param inputStream the buffered input stream
     * @param terminator
     * @return true if the terminator was met
     * @throws IOException if the file ended abruptly.
     */
    public static boolean isEndOfFieldArray(final InputStream inputStream, final int terminator)
            throws IOException {
        inputStream.mark(1);
        final int maybeTerminator = inputStream.read();
        if (maybeTerminator == -1) {
            throw new IOException("The file is corrupted or is not a dbf file");
        } else if (maybeTerminator == terminator) {
            return true;
        } else { // unget
            inputStream.reset();
            return false;
        }
    }

    /**
     * @param inputStream the buffered input stream
     * @param terminator
     * @return true if the terminator was met
     * @throws IOException if the file ended abruptly.
     */
    public static boolean isEndOfRecords(final InputStream inputStream, final int terminator)
            throws IOException {
        inputStream.mark(1);
        final int maybeTerminator = inputStream.read();
        if (maybeTerminator == -1 || maybeTerminator == terminator) {
            return true;
        } else { // unget
            inputStream.reset();
            return false;
        }
    }

    public static InputStream resettable(final InputStream inputStream, final int bufferSize) {
        if (inputStream.markSupported()) {
            return inputStream;
        } else {
            return new BufferedInputStream(inputStream, bufferSize);
        }
    }

    public static File getFile(final String filename) {
        final File candidateMemoFile = new File(filename);
        final String candidateMemoPath = candidateMemoFile.getAbsolutePath().toLowerCase(Locale.US);
        final File dir = candidateMemoFile.getParentFile();
        for (final File f : dir.listFiles()) {
            if (candidateMemoPath.equals(f.getAbsolutePath().toLowerCase(Locale.US))) {
                return f;
            }
        }
        return null;

    }
}
