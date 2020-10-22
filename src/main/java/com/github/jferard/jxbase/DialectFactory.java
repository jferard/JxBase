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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TimeZone;

/**
 * A factory to create a dialect.
 */
public class DialectFactory {
    /**
     * @param type           the type
     * @param tableName      the full path/to/table (used for memo).
     * @param charset        the charset (used for memo).
     * @param memoHeaderMeta null if reader only
     * @return the dialect associated to the type
     * @throws IOException
     */
    public static XBaseDialect<?, ?> getDialect(final XBaseFileTypeEnum type,
                                                final String tableName, final Charset charset,
                                                final Map<String, Object> memoHeaderMeta)
            throws IOException {
        final XBaseDialect<?, ?> dialect;
        if (type.memoFileType() == XBaseMemoFileType.NO_MEMO_FILE) {
            dialect = getNoMemoDialect(type, charset);
        } else if (memoHeaderMeta == null) {
            dialect = DialectFactory.getDialect(
                    new ReaderDialectFactoryAux(type, charset), type, tableName, charset, null);
        } else {
            dialect = DialectFactory.getDialect(
                    new WriterDialectFactoryAux(type, charset), type, tableName, charset,
                    memoHeaderMeta);
        }
        return dialect;
    }

    public static XBaseDialect<?, ?> getNoMemoDialect(final XBaseFileTypeEnum type,
                                                      final Charset charset) throws IOException {
        return DialectFactory
                .getDialect(new NoMemoDialectFactoryAux(type, charset), type, null, charset, null);
    }

    private static XBaseDialect<?, ?> getDialect(final DialectFactoryAux dialectFactoryAux,
                                                 final XBaseFileTypeEnum type,
                                                 final String tableName, final Charset charset,
                                                 final Map<String, Object> memoHeaderMeta)
            throws IOException {
        final XBaseDialect<?, ?> dialect;
        switch (type) {
            case dBASE2:
                dialect = dialectFactoryAux.createDB2Dialect();
                break;
            case dBASE3plus:
            case dBASE3plusMemo:
            case FoxBASEPlus1:
                dialect = dialectFactoryAux
                        .createDB3Dialect(TimeZone.getDefault(), tableName,
                                memoHeaderMeta);
                break;
            case dBASE4:
            case dBASE4SQLTable:
            case dBASE4Memo:
            case dBASE4SQLTableMemo:
                dialect = dialectFactoryAux
                        .createDB4Dialect(TimeZone.getDefault(), tableName,
                                memoHeaderMeta);
                break;
            case VisualFoxPro:
            case VisualFoxProAutoIncrement:
                dialect = dialectFactoryAux
                        .createVisualFoxProDialect(TimeZone.getDefault(), tableName,
                                memoHeaderMeta);
                break;
            case FoxPro2xMemo:
                dialect = dialectFactoryAux
                        .createFoxProDialect(TimeZone.getDefault(), tableName,
                                memoHeaderMeta);
                break;
            default:
                dialect = dialectFactoryAux
                        .createDB3Dialect(TimeZone.getDefault(), tableName,
                                memoHeaderMeta);
                break;
        }
        return dialect;
    }
}
