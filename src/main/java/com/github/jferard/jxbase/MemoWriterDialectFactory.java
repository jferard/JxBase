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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3DialectFactory;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4DialectFactory;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.dialect.foxpro.FoxProAccess;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialectFactory;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TimeZone;

public class MemoWriterDialectFactory implements MemoDialectFactory {
    @Override
    public XBaseDialect<DB2Dialect, DB2Access> createDB2Dialect(final XBaseFileTypeEnum type,
                                                                final Charset charset) {
        return DB2Dialect.create(type, charset);
    }

    @Override
    public XBaseDialect<DB3Dialect, DB3Access> createDB3Dialect(final XBaseFileTypeEnum type,
                                                                final Charset charset, final TimeZone timeZone,
                                                                final String databaseName,
                                                                final Map<String, Object> memoHeaderMeta)
            throws IOException {
        return DB3DialectFactory.create(type, charset, timeZone).writer(databaseName, memoHeaderMeta).build();
    }

    @Override
    public XBaseDialect<DB4Dialect, DB4Access> createDB4Dialect(final XBaseFileTypeEnum type,
                                                                final Charset charset, final TimeZone timeZone,
                                                                final String databaseName,
                                                                final Map<String, Object> memoHeaderMeta)
            throws IOException {
        return new DB4DialectFactory(type, charset, timeZone).writer(databaseName, memoHeaderMeta).build();
    }

    @Override
    public XBaseDialect<FoxProDialect, FoxProAccess> createFoxProDialect(final XBaseFileTypeEnum type,
                                                                         final Charset charset,
                                                                         final TimeZone timeZone,
                                                                         final String databaseName,
                                                                         final Map<String, Object> memoHeaderMeta)
            throws IOException {
        return FoxProDialectFactory.create(type, charset, timeZone).writer(databaseName, memoHeaderMeta).build();
    }
}
