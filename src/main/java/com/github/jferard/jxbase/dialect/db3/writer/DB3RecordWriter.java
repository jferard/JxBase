/*
 * JxBase - Copyright (c) 2019-2020 Julien Férard
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

package com.github.jferard.jxbase.dialect.db3.writer;

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.XBaseRecordWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/**
 * A writer for DB3 records.
 *
 * @param <D> the dialect
 * @param <A> the access
 */
public class DB3RecordWriter<D extends XBaseDialect<D, A>, A extends MemoAccess>
        implements XBaseRecordWriter<D> {
    private final XBaseMemoWriter memoWriter;
    protected final Collection<XBaseField<? super A>> fields;
    protected final D dialect;
    protected final OutputStream out;
    final Charset charset;
    protected int recordCount;

    public DB3RecordWriter(final D dialect, final OutputStream out, final Charset charset,
                           final XBaseMemoWriter memoWriter,
                           final Collection<XBaseField<? super A>> fields) {
        this.dialect = dialect;
        this.out = out;
        this.charset = charset;
        this.memoWriter = memoWriter;
        this.fields = fields;
        this.recordCount = 0;
    }

    @Override
    public void write(final Map<String, Object> objectByName) throws IOException {
        this.out.write(JxBaseUtils.EMPTY);
        final A access = this.dialect.getAccess();
        for (final XBaseField<? super A> field : this.fields) {
            final Object value = objectByName.get(field.getName());
            if (field instanceof MemoField) {
                final long offsetInBlocks =
                        access.writeMemoValue(this.memoWriter, (XBaseMemoRecord) value);
                access.writeMemoAddress(this.out, offsetInBlocks);
            } else {
                field.writeValue(access, this.out, value);
            }
        }
        this.recordCount++;
    }

    @Override
    public int getRecordQty() {
        return this.recordCount;
    }

    @Override
    public void close() throws IOException {
        this.out.write(0x1A);
        this.out.flush();
        if (this.memoWriter != null) {
            this.memoWriter.close();
        }
    }
}
