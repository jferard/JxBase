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

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.dialect.db3.DB3DialectBuilder;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.dialect.db4.DB4DialectBuilder;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialectBuilder;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialectBuilder;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialect;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.TimeZone;

class NoMemoDialectFactoryAux implements DialectFactoryAux {
    private final XBaseFileTypeEnum type;
    private final Charset charset;

    public NoMemoDialectFactoryAux(final XBaseFileTypeEnum type, final Charset charset) {
        this.type = type;
        this.charset = charset;
    }

    @Override
    public XBaseDialect<DB2Dialect, DB2Access> createDB2Dialect() {
        return DB2Dialect.create(this.type, this.charset);
    }

    @Override
    public XBaseDialect<DB3Dialect, DB3Access> createDB3Dialect(final TimeZone timeZone,
                                                                final String tableName,
                                                                final Map<String, Object> memoHeaderMeta) {
        return DB3DialectBuilder.create(this.type, this.charset, timeZone).build();
    }

    @Override
    public XBaseDialect<DB4Dialect, DB4Access> createDB4Dialect(final TimeZone timeZone,
                                                                final String tableName,
                                                                final Map<String, Object> memoHeaderMeta) {
        return DB4DialectBuilder.create(this.type, this.charset, timeZone).build();
    }

    @Override
    public XBaseDialect<VisualFoxProDialect, VisualFoxProAccess> createVisualFoxProDialect(
            final TimeZone timeZone,
            final String tableName,
            final Map<String, Object> memoHeaderMeta) {
        return VisualFoxProDialectBuilder.create(this.type, this.charset, timeZone).build();
    }

    @Override
    public XBaseDialect<FoxProDialect, DB4Access> createFoxProDialect(
            final TimeZone timeZone,
            final String tableName,
            final Map<String, Object> memoHeaderMeta) {
        return FoxProDialectBuilder.create(this.type, this.charset, timeZone).build();
    }
}
