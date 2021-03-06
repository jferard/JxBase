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

package com.github.jferard.jxbase;


import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestHelper {
    public static String getResourceTableName(final String filename) throws IOException {
        final String pathname = getResourcePath(filename);
        if (pathname == null) {
            return null;
        }
        return pathname.substring(0, pathname.lastIndexOf('.'));
    }

    public static String getResourcePath(final String filename) {
        final URL resource = TestHelper.class.getClassLoader().getResource(filename);
        if (resource == null) {
            return null;
        }
        return resource.getFile();
    }

    public static SeekableByteChannel fromByteBuffer(final ByteBuffer buffer) {
        return new SeekableByteChannel() {
            private int transfer(final ByteBuffer from, final ByteBuffer to) {
                // See https://stackoverflow.com/questions/570168/transferring-bytes-from-one
                // -bytebuffer
                // -to-another
                final int transferCount = Math.min(to.remaining(), from.remaining());
                if (transferCount > 0) {
                    to.put(from.array(), from.arrayOffset() + from.position(), transferCount);
                    from.position(from.position() + transferCount);
                }
                return transferCount;
            }

            @Override
            public int read(final ByteBuffer dst) {
                return this.transfer(buffer, dst);
            }

            @Override
            public int write(final ByteBuffer src) {
                return this.transfer(src, buffer);
            }

            @Override
            public long position() {
                return buffer.position();
            }

            @Override
            public SeekableByteChannel position(final long newPosition) {
                buffer.position((int) newPosition);
                return this;
            }

            @Override
            public long size() {
                return buffer.array().length;
            }

            @Override
            public SeekableByteChannel truncate(final long size) {
                return null;
            }

            @Override
            public boolean isOpen() {
                return true;
            }

            @Override
            public void close() {

            }
        };
    }

    public static byte[] getBytes(final String s, final int length) {
        final byte[] bytes = new byte[length];
        final byte[] memo = s.getBytes(JxBaseUtils.ASCII_CHARSET);
        System.arraycopy(memo, 0, bytes, 0, memo.length);
        return bytes;
    }

    /**
     * @param tableName without .dbf
     * @return
     * @throws IOException
     */
    public static String createTempTable(final String tableName) throws IOException {
        final File temp = File.createTempFile(tableName, ".dbf");
        final String path = temp.getAbsolutePath();
        return path.substring(0, path.lastIndexOf('.'));
    }

    public static <A extends XBaseAccess> XBaseField<? super A> fromStringRepresentation(final XBaseDialect<A, ?> dialect,
                                                                                         final String representation) {
        final String[] split = representation.split(",");
        assert split.length == 4;
        final String name = split[0];
        final byte[] typeBytes = split[1].getBytes(JxBaseUtils.ASCII_CHARSET);
        assert typeBytes.length == 1;
        final byte typeByte = typeBytes[0];
        final int length = Integer.parseInt(split[2]);
        final int numberOfDecimalPlaces = Integer.parseInt(split[2]);
        return dialect.createXBaseField(name, typeByte, length, numberOfDecimalPlaces);
    }

    public static <T> Set<T> newSet(final T... values) {
        final Set<T> ret = new HashSet<T>(values.length);
        ret.addAll(Arrays.asList(values));
        return ret;
    }

    public static Date createDate(final int year, final int month, final int date) {
        final Calendar calendar = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        calendar.set(year + 1900, month, date, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
