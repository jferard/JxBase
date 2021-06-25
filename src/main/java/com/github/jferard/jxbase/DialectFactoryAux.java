/*
* JxBase - Copyright (c) 2019-2021 Julien Férard
* JDBF - Copyright (c) 2012-2018 Ivan Ryndin (https://github.com/iryndin)
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
import com.github.jferard.jxbase.dialect.db3.DB3DialectFactory;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.dialect.db4.DB4DialectFactory;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialectFactory;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialectFactory;

import java.nio.charset.Charset;
import java.util.TimeZone;

class DialectFactoryAux {
    private final XBaseFileTypeEnum type;
    private final Charset charset;

    public DialectFactoryAux(final XBaseFileTypeEnum type, final Charset charset) {
        this.type = type;
        this.charset = charset;
    }

    public XBaseDialect<DB2Access, DB2Dialect> createDB2Dialect() {
        return DB2Dialect.create(this.type, this.charset);
    }

    public XBaseDialect<DB3Access, DB3Dialect> createDB3Dialect(final TimeZone timeZone) {
        return DB3DialectFactory.create(this.type, this.charset, timeZone);
    }

    public XBaseDialect<DB4Access, DB4Dialect> createDB4Dialect(final TimeZone timeZone) {
        return DB4DialectFactory.create(this.type, this.charset, timeZone);
    }

    public XBaseDialect<VisualFoxProAccess, VisualFoxProDialect> createVisualFoxProDialect(
            final TimeZone timeZone) {
        return VisualFoxProDialectFactory.create(this.type, this.charset, timeZone);
    }

    public XBaseDialect<DB4Access, FoxProDialect> createFoxProDialect(
            final TimeZone timeZone) {
        return FoxProDialectFactory.create(this.type, this.charset, timeZone);
    }
}
