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

import com.github.jferard.jxbase.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.memo.WithMemoInternalReaderFactory;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.TimeZone;

public class FoxProInternalReaderFactory extends WithMemoInternalReaderFactory {
    public FoxProInternalReaderFactory(final FoxProDialect dialect, final TimeZone timeZone,
                                       final XBaseMemoReader memoReader) {
        super(dialect, timeZone, memoReader);
    }

    @Override
    public XBaseRecordReader createRecordReader(final InputStream inputStream,
                                                final Charset charset, final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray array,
                                                final Object optional) {
        return new FoxProRecordReader(this.dialect, inputStream, charset, array, this.memoReader,
                this.timezone);
    }
}
