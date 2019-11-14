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

package com.github.jferard.jxbase.dialect.foxpro;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.WithMemoRecordReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.TimeZone;

public class FoxProRecordReader extends WithMemoRecordReader {

    public FoxProRecordReader(final XBaseDialect dialect, final InputStream dbfInputStream,
                              final Charset charset, final XBaseFieldDescriptorArray array,
                              final XBaseMemoReader memoReader, final TimeZone timezone) {
        super(dialect, dbfInputStream, charset, array, memoReader, timezone);
    }

    public Date getDatetimeValue(final byte[] recordBuffer, final int offset, final int length) {
        // TODO
        // https://en.wikipedia
        // .org/wiki/Julian_day#Julian_or_Gregorian_calendar_from_Julian_day_number
        return null;
    }

    public XBaseMemoRecord getSmallMemoValue(final byte[] recordBuffer, final int offset,
                                             final int length) throws IOException {
        final long offsetInBlocks =
                this.dialect.getOffsetInBlocks(this, recordBuffer, offset, length);
        return this.memoReader.read(offsetInBlocks);
    }

    public byte[] getNullFlagsValue(final byte[] recordBuffer, final int offset, final int length) {
        final byte[] bytes = new byte[length];
        System.arraycopy(recordBuffer, offset, bytes, 0, length);
        return bytes;
    }
}
