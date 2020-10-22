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
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Dialect;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.foxpro.VisualFoxProDialect;

import java.io.IOException;
import java.util.Map;
import java.util.TimeZone;

/**
 * An auxiliary interface to create dialects.
 */
interface DialectFactoryAux {
    XBaseDialect<DB2Dialect, DB2Access> createDB2Dialect();

    XBaseDialect<DB3Dialect, DB3Access> createDB3Dialect(
            TimeZone aDefault, String tableName, Map<String, Object> memoHeaderMeta)
            throws IOException;

    XBaseDialect<DB4Dialect, DB4Access> createDB4Dialect(
            TimeZone aDefault, String tableName, Map<String, Object> memoHeaderMeta)
            throws IOException;

    XBaseDialect<VisualFoxProDialect, VisualFoxProAccess> createVisualFoxProDialect(
            TimeZone timeZone, String tableName, Map<String, Object> memoHeaderMeta)
            throws IOException;

    XBaseDialect<FoxProDialect, VisualFoxProAccess> createFoxProDialect(
            TimeZone timeZone, String tableName, Map<String, Object> memoHeaderMeta)
            throws IOException;
}
