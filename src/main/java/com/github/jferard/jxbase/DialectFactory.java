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

public class DialectFactory {
    /**
     * @param type
     * @param databaseName
     * @param charset
     * @return
     * @throws IOException
     */
    public static XBaseDialect<?, ?> getMemoReaderDialect(final XBaseFileTypeEnum type,
                                                          final String databaseName,
                                                          final Charset charset)
            throws IOException {
        return DialectFactory
                .getDialect(new MemoReaderDialectFactory(), type, databaseName, charset, null);
    }

    public static XBaseDialect<?, ?> getMemoWriterDialect(final XBaseFileTypeEnum type,
                                                          final String databaseName,
                                                          final Charset charset,
                                                          final Map<String, Object> memoHeaderMeta)
            throws IOException {
        return DialectFactory
                .getDialect(new MemoWriterDialectFactory(), type, databaseName, charset,
                        memoHeaderMeta);
    }

    public static XBaseDialect<?, ?> getNoMemoDialect(final XBaseFileTypeEnum type,
                                                      final Charset charset) throws IOException {
        return DialectFactory.getDialect(new NoMemoDialectFactory(), type, null, charset, null);
    }

    /**
     * @param type
     * @param databaseName
     * @param charset
     * @param memoHeaderMeta
     * @return
     * @throws IOException
     */
    private static XBaseDialect<?, ?> getDialect(final MemoDialectFactory dialectFactory,
                                                 final XBaseFileTypeEnum type,
                                                 final String databaseName, final Charset charset,
                                                 final Map<String, Object> memoHeaderMeta)
            throws IOException {
        final XBaseDialect<?, ?> dialect;
        switch (type) {
            case dBASE2:
                dialect = dialectFactory.createDB2Dialect(type, charset);
                break;
            case dBASE3plus:
            case dBASE3plusMemo:
            case FoxBASEPlus1:
                dialect = dialectFactory
                        .createDB3Dialect(type, charset, TimeZone.getDefault(), databaseName,
                                memoHeaderMeta);
                break;
            case dBASE4:
            case dBASE4SQLTable:
            case dBASE4Memo:
            case dBASE4SQLTableMemo:
                dialect = dialectFactory
                        .createDB4Dialect(type, charset, TimeZone.getDefault(), databaseName,
                                memoHeaderMeta);
                break;
            case VisualFoxPro:
            case VisualFoxProAutoIncrement:
                dialect = dialectFactory
                        .createFoxProDialect(type, charset, TimeZone.getDefault(), databaseName,
                                memoHeaderMeta);
                break;
            default:
                dialect = dialectFactory
                        .createDB3Dialect(type, charset, TimeZone.getDefault(), databaseName,
                                memoHeaderMeta);
                break;
        }
        return dialect;
    }
}
