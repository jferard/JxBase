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

package com.github.jferard.jxbase.dialect.db4memo;

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.XBaseMemoFileType;
import com.github.jferard.jxbase.dialect.db3memo.DB3MemoDialect;
import com.github.jferard.jxbase.dialect.memo.WithMemoInternalWriterFactory;
import com.github.jferard.jxbase.dialect.memo.XBaseMemoReader;
import com.github.jferard.jxbase.dialect.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.writer.internal.XBaseInternalWriterFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.TimeZone;

public class DB4MemoDialect extends DB3MemoDialect {
    public DB4MemoDialect(final XBaseFileTypeEnum type) {
        super(type);
    }

    protected XBaseMemoReader getMemoReader(final String databaseName, final Charset charset)
            throws IOException {
        final XBaseMemoFileType memoFileType = this.type.memoFileType();
        if (memoFileType != XBaseMemoFileType.NO_MEMO_FILE) {
            // TODO: handle cases with a factory
            final File memoFile = new File(databaseName + memoFileType.getExtension());
            return DB4MemoReader.fromChannel(memoFile, charset);
        }

        return null;
    }

    @Override
    public XBaseInternalWriterFactory getInternalWriterFactory(final String databaseName,
                                                               final Charset charset)
            throws IOException {
        final File memoFile = new File(databaseName + ".dbt");
        final XBaseMemoWriter memoWriter = DB4MemoWriter.fromChannel(memoFile, charset);
        return new WithMemoInternalWriterFactory(this, TimeZone.getDefault(), memoWriter);
    }
}
