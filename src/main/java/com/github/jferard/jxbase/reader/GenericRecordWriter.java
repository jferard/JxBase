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

package com.github.jferard.jxbase.reader;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.XBaseRecordWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

public class GenericRecordWriter<D extends XBaseDialect<D, A>, A> implements XBaseRecordWriter<D> {
    private static final int MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
    protected final Collection<XBaseField<? super A>> fields;
    protected final D dialect;
    protected final OutputStream out;
    final Charset charset;
    protected int recordCount;

    public GenericRecordWriter(final D dialect, final OutputStream out, final Charset charset,
                               final Collection<XBaseField<? super A>> fields) {
        this.dialect = dialect;
        this.out = out;
        this.charset = charset;
        this.fields = fields;
        this.recordCount = 0;
    }

    public void write(final Map<String, Object> objectByName) throws IOException {
        this.out.write(JxBaseUtils.EMPTY);
        for (final XBaseField<? super A> field : this.fields) {
            final Object value = objectByName.get(field.getName());
            field.writeValue(this.dialect.getAccess(), this.out, value);
        }
        this.recordCount++;
    }

    public int getRecordQty() {
        return this.recordCount;
    }

    public void close() throws IOException {
        this.out.write(0x1A);
        this.out.flush();
    }
}
