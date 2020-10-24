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

package com.github.jferard.jxbase.writer;

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/**
 * A record writer. Uses the access to convert a value to bytes.
 *
 * @param <D> the dialect
 * @param <A> the access
 */
public class GenericRecordWriter<D extends XBaseDialect<D, A>, A> implements XBaseRecordWriter<D> {
    public static <D extends XBaseDialect<D, A>, A> XBaseRecordWriter<D> create(
            final D dialect, final OutputStream outputStream, final Charset charset,
            final Collection<XBaseField<? super A>> fields) {
        return new GenericRecordWriter<D, A>(dialect.getAccess(), outputStream, charset, fields);
    }

    protected final Collection<XBaseField<? super A>> fields;
    protected final A access;
    protected final OutputStream out;
    final Charset charset;
    protected int recordCount;

    public GenericRecordWriter(final A access, final OutputStream out, final Charset charset,
                               final Collection<XBaseField<? super A>> fields) {
        this.access = access;
        this.out = out;
        this.charset = charset;
        this.fields = fields;
        this.recordCount = 0;
    }

    @Override
    public void write(final Map<String, Object> objectByName) throws IOException {
        this.out.write(JxBaseUtils.EMPTY);
        for (final XBaseField<? super A> field : this.fields) {
            final Object value = objectByName.get(field.getName());
            field.writeValue(this.access, this.out, value);
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
    }
}
