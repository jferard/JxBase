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

package com.github.jferard.jxbase.dialect.db2;

import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

public class DB2Utils {
    public static final int DB2_FIELD_DESCRIPTOR_LENGTH = 16;
    public static final int DB2_MAX_FIELDS = 32;
    public static final byte[] DB2_BLANK_FIELD = {JxBaseUtils.NULL_BYTE, JxBaseUtils.NULL_BYTE,
            JxBaseUtils.NULL_BYTE, JxBaseUtils.NULL_BYTE, JxBaseUtils.NULL_BYTE,
            JxBaseUtils.NULL_BYTE, JxBaseUtils.NULL_BYTE, JxBaseUtils.NULL_BYTE,
            JxBaseUtils.NULL_BYTE, JxBaseUtils.NULL_BYTE, JxBaseUtils.NULL_BYTE,
            JxBaseUtils.NULL_BYTE, JxBaseUtils.NULL_BYTE, JxBaseUtils.NULL_BYTE,
            JxBaseUtils.NULL_BYTE, JxBaseUtils.NULL_BYTE};
    public static final int DB2_DELETED_RECORD_HEADER = 0x2A;

    /**
     * Write the date
     * @param out the output stream
     * @param updateDate the date of today
     * @throws IOException
     */
    public static void writeHeaderUpdateDate(final OutputStream out, final Date updateDate)
            throws IOException {
        final Calendar calendar = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        calendar.setTime(updateDate);
        out.write(calendar.get(Calendar.YEAR) - 1900);
        out.write(calendar.get(Calendar.MONTH) + 1);
        out.write(calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Build the data
     * @param yearByte year
     * @param monthByte month
     * @param dayByte date
     * @return a date
     */
    public static Date createHeaderUpdateDate(final byte yearByte, final byte monthByte,
                                              final byte dayByte) {
        final int year = yearByte + 1900;
        final int month = monthByte - 1;
        final Calendar calendar = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        calendar.set(year, month, dayByte, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private DB2Utils() {}
}
