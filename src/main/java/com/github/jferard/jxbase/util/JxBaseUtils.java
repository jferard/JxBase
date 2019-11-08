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

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class JxBaseUtils {

    public static final int RECORD_HEADER_LENGTH = 8;
    public static final int FIELD_RECORD_LENGTH = 32;
    public static final int HEADER_TERMINATOR = 0x0D;
    public static final int MEMO_HEADER_LENGTH = 0x200; // 512 bytes
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
    public static final int FIELD_DESCRIPTOR_SIZE = 32;
    public static final int OPTIONAL_LENGTH = 263;
    public static int EMPTY = 0x20;

    /*
    public static List<XBaseField<?>> createFieldsFromString(
            String fieldsString) {
        String[] a = fieldsString.split("\\|");
        List<XBaseField<?>> list =
                new ArrayList<XBaseField<?>>(a.length);
        for (String b : a) {
            if (b.trim().length() == 0) {
                continue;
            }
            XBaseField<?> f = createDbfFieldFromString(b);
            list.add(f);
        }
        return list;
    }

    public static XBaseField<?> createDbfFieldFromString(String s) {
        String[] a = s.split(",");
        return new DefaultDbfField(a[0], DbfFieldTypeEnum.fromChar(a[1].charAt(0)),
                Integer.parseInt(a[2]), Integer.parseInt(a[3]));
    }
    */

    @SuppressWarnings("deprecation")
    public static byte[] writeDateForHeader(final Date date) {
        final byte[] headerBytes = {(byte) (date.getYear() - 100), (byte) (date.getMonth() + 1),
                (byte) (date.getDay()),};
        return headerBytes;
    }

    public static byte[] writeDate(final Date date) {
        final String s = DATE_FORMAT.get().format(date);
        return s.getBytes();
    }

    public static Date parseDate(final String s) throws ParseException {
        return DATE_FORMAT.get().parse(s);
    }

    public static boolean compareMaps(final Map<String, Object> m1, final Map<String, Object> m2) {
        if (!compareSets(m1.keySet(), m2.keySet())) {
            return false;
        }
        for (final String s : m1.keySet()) {
            if (!compareObjects(m1.get(s), m2.get(s))) {
                return false;
            }
        }
        return true;
    }

    public static boolean compareSets(final Set<String> set1, final Set<String> set2) {
        if (set1.size() != set2.size()) {
            return false;
        }
        for (final String s : set1) {
            if (!set2.contains(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean compareObjects(final Object o1, final Object o2) {
        if (o1 == null) {
            return o2 == null;
        } else {
            if (o2 == null) {
                return false;
            } else {
                return o1.equals(o2);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static byte[] writeJulianDate(final Date d) {
        final ByteBuffer bb = ByteBuffer.allocate(8);

        bb.putInt(0, julianDay(d));
        bb.putInt(4,
                d.getHours() * 60 * 60 * 1000 + d.getMinutes() * 60 * 1000 + d.getSeconds() * 1000);

        return bb.array();
    }

    @SuppressWarnings("deprecation")
    private static int julianDay(final Date d) {
        final int year = d.getYear();
        final int month = d.getMonth() + 1;
        final int day = d.getDate();

        final double extra = (100.0 * year) + month - 190002.5;
        final long l = (long) ((367.0 * year) -
                (Math.floor(7.0 * (year + Math.floor((month + 9.0) / 12.0)) / 4.0)) +
                Math.floor((275.0 * month) / 9.0) + day);

        // Unsigned types are too complicated they said... Only having signed ones makes it
        // easier they said
        if (l > Integer.MAX_VALUE) {
            return ~((int) l & Integer.MAX_VALUE);
        } else {
            return (int) (l & Integer.MAX_VALUE);
        }
    }
}
