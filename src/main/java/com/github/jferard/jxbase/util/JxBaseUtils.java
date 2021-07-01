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

package com.github.jferard.jxbase.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.TimeZone;

/**
 * Some utils.
 */
public class JxBaseUtils {
    public static final int HEADER_TERMINATOR = 0x0D;
    public static final ThreadLocal<SimpleDateFormat> DATE_FORMAT =
            new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat("yyyyMMdd");
                }
            };
    public static final int METADATA_LENGTH = 32;
    public static final byte NULL_BYTE = (byte) 0x0;
    public static final int RECORDS_TERMINATOR = 0x1A;
    public static final Charset ASCII_CHARSET = Charset.forName("US-ASCII");
    public static final Charset LATIN1_CHARSET = Charset.forName("ISO-8859-1");
    public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    public static final int VISUAL_FOXPRO_OPTIONAL_LENGTH = 263;
    public static final int BUFFER_SIZE = 8192;
    public static final int MAX_FIELD_NAME_SIZE = 11;
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
    private static final CharSequence NUMERIC_OVERFLOW = "*";
    public static int EMPTY = 0x20;

    /**
     * Read the field bytes into a buffer
     * @param inputStream  the input stream
     * @param fieldBytes the field bytes
     * @throws IOException if the bytes are not there
     */
    public static void readFieldBytes(final InputStream inputStream, final byte[] fieldBytes)
            throws IOException {
        final int count = IOUtils.readFully(inputStream, fieldBytes);
        if (count != fieldBytes.length) {
            throw new IOException("The file is corrupted or is not a dbf file. Couldn't read " +
                    fieldBytes.length + " bytes for a field description (just found " + count + ")");
        }
    }

    /**
     * Get the name of the field
     * @param fieldBytes the data
     * @return the name
     */
    public static String getName(final byte[] fieldBytes) {
        int nameLength = 0;
        while (nameLength < MAX_FIELD_NAME_SIZE && fieldBytes[nameLength] != 0x0) {
            nameLength++;
        }
        return new String(fieldBytes, 0, nameLength, ASCII_CHARSET);
    }

    /**
     * Get the length of the field
     * @param lenByte the byte
     * @return the length
     */
    public static int getLength(final byte lenByte) {
        int length = lenByte;
        if (length < 0) {
            length += 256;
        }
        return length;
    }

    /**
     * A simple joiner...
     * @param delimiter the delimiter
     * @param chunks the chunks
     * @return the joined string.
     */
    public static String join(final String delimiter, final Iterable<String> chunks) {
        final Iterator<String> it = chunks.iterator();
        final StringBuilder sb = new StringBuilder();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(delimiter).append(it.next());
            }
        }
        return sb.toString();
    }
}