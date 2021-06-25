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

import java.nio.charset.Charset;
import java.util.TimeZone;

/**
 * A factory to create a dialect.
 */
public class DialectFactory {
    /**
     * @param type           the type
     * @param charset        the charset (used for memo).
     * @return the dialect associated to the type
     */
    public static XBaseDialect<?, ?> getDialect(final XBaseFileTypeEnum type,
                                                final Charset charset) {
        return DialectFactory.getDialect(
                    new DialectFactoryAux(type, charset), type);
    }

    private static XBaseDialect<?, ?> getDialect(final DialectFactoryAux dialectFactoryAux,
                                                 final XBaseFileTypeEnum type) {
        final XBaseDialect<?, ?> dialect;
        switch (type) {
            case dBASE2:
                dialect = dialectFactoryAux.createDB2Dialect();
                break;
            case dBASE3plus:
            case dBASE3plusMemo:
            case FoxBASEPlus1:
                dialect = dialectFactoryAux.createDB3Dialect(TimeZone.getDefault());
                break;
            case dBASE4:
            case dBASE4SQLTable:
            case dBASE4Memo:
            case dBASE4SQLTableMemo:
                dialect = dialectFactoryAux
                        .createDB4Dialect(TimeZone.getDefault()
                        );
                break;
            case VisualFoxPro:
            case VisualFoxProAutoIncrement:
                dialect = dialectFactoryAux
                        .createVisualFoxProDialect(TimeZone.getDefault()
                        );
                break;
            case FoxPro2xMemo:
                dialect = dialectFactoryAux
                        .createFoxProDialect(TimeZone.getDefault()
                        );
                break;
            default:
                dialect = dialectFactoryAux
                        .createDB3Dialect(TimeZone.getDefault()
                        );
                break;
        }
        return dialect;
    }
}
