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

package com.github.jferard.jxbase.dialect.db3.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;
import com.github.jferard.jxbase.util.BytesUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * An access to DB3 date field.
 */
public class DB3DateAccess implements DateAccess {
    public static DB3DateAccess create(final Charset charset, final TimeZone timeZone) {
        final RawRecordReadHelper rawRecordReadHelper = new RawRecordReadHelper(charset);
        final RawRecordWriteHelper rawRecordWriterHelper = new RawRecordWriteHelper(charset);
        return new DB3DateAccess(rawRecordReadHelper, rawRecordWriterHelper, timeZone);
    }

    private final RawRecordReadHelper rawRecordReader;
    private final RawRecordWriteHelper rawRecordWriter;
    private final TimeZone timeZone;

    public DB3DateAccess(final RawRecordReadHelper rawRecordReader,
                         final RawRecordWriteHelper rawRecordWriter, final TimeZone timeZone) {
        this.rawRecordReader = rawRecordReader;
        this.rawRecordWriter = rawRecordWriter;
        this.timeZone = timeZone;
    }

    @Override
    public int getDateValueLength() {
        return 8;
    }

    @Override
    public Date extractDateValue(final byte[] recordBuffer, final int offset, final int length) {
        final String s =
                BytesUtils.extractTrimmedASCIIString(recordBuffer, offset, length);
        if (s == null) {
            return null;
        }
        try {
            final SimpleDateFormat format = JxBaseUtils.DATE_FORMAT.get();
            format.setTimeZone(this.timeZone);
            return format.parse(s);
        } catch (final ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeDateValue(final OutputStream out, final Date value) throws IOException {
        final int fieldLength = 8;
        if (value == null) {
            this.rawRecordWriter.writeEmpties(out, fieldLength);
        } else {
            final String s = JxBaseUtils.DATE_FORMAT.get().format(value);
            this.rawRecordWriter.write(out, s.getBytes(JxBaseUtils.ASCII_CHARSET));
        }
    }

    @Override
    public FieldRepresentation getDateFieldRepresentation(final String fieldName) {
        return new FieldRepresentation(fieldName, 'D', 8, 0);
    }
}
