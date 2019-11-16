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

package com.github.jferard.jxbase.memo;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.dialect.basic.BasicInternalWriterFactory;
import com.github.jferard.jxbase.dialect.db3memo.DB3MemoRecordWriter;
import com.github.jferard.jxbase.writer.internal.XBaseRecordWriter;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.TimeZone;

public class WithMemoInternalWriterFactory extends BasicInternalWriterFactory {
    protected final XBaseMemoWriter memoWriter;

    public WithMemoInternalWriterFactory(final XBaseDialect dialect, final TimeZone timezone,
                                         final XBaseMemoWriter memoWriter) {
        super(dialect, TimeZone.getDefault());
        // TODO : fix timezone
        this.memoWriter = memoWriter;
    }

    @Override
    public XBaseRecordWriter createRecordWriter(final XBaseDialect dialect,
                                                final OutputStream outputStream,
                                                final Charset charset, final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray array,
                                                final Object optional) {
        return new DB3MemoRecordWriter(dialect, outputStream, charset, array.getFields(),
                this.memoWriter);
    }
}
