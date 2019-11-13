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

package com.github.jferard.jxbase.dialect.memo;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.internal.BasicRecordWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;

public class WithMemoRecordWriter extends BasicRecordWriter {
    private static final int MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
    protected final XBaseMemoWriter memoWriter;

    public WithMemoRecordWriter(final XBaseDialect dialect, final OutputStream out,
                                final Charset charset, final Collection<XBaseField> fields,
                                final XBaseMemoWriter memoWriter) {
        super(dialect, out, charset, fields);
        this.memoWriter = memoWriter;
    }

    public void writeMemoValue(final XBaseMemoRecord value) throws IOException {
        final int length = this.dialect.getMemoValueLength();
        if (value == null) {
            BitUtils.writeEmpties(this.out, length);
        } else {
            final long offsetInBlocks = this.memoWriter.write(value);
            final String s = String.format("%10d", offsetInBlocks);
            this.out.write(s.getBytes(JxBaseUtils.ASCII_CHARSET));
        }
    }
}
