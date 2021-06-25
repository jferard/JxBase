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

package com.github.jferard.jxbase.dialect.db3;

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2NumericAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericAccess;
import com.github.jferard.jxbase.dialect.db3.field.DB3DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.DB3MemoAccess;
import com.github.jferard.jxbase.dialect.db3.field.DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;

import java.nio.charset.Charset;
import java.util.TimeZone;

/**
 * A factory for DB3 dialect.
 */
public class DB3DialectFactory {
    /**
     * Create the builder
     * @param type the actual type of the DBf file
     * @param charset the charset
     * @param timeZone the time zone
     * @return the builder
     */
    public static DB3Dialect create(final XBaseFileTypeEnum type, final Charset charset,
                                    final TimeZone timeZone) {
        final DB3Access access = createAccess(charset, timeZone);
        return new DB3Dialect(type, access);
    }

    public static DB3Access createAccess(final Charset charset, final TimeZone timeZone) {
        final RawRecordReadHelper rawRecordReadHelper = new RawRecordReadHelper(charset);
        final RawRecordWriteHelper rawRecordWriterHelper = new RawRecordWriteHelper(charset);
        final CharacterAccess characterAccess =
                new DB2CharacterAccess(rawRecordReadHelper, rawRecordWriterHelper);
        final LogicalAccess logicalAccess =
                new DB2LogicalAccess(rawRecordReadHelper, rawRecordWriterHelper);
        final NumericAccess numericAccess =
                new DB2NumericAccess(rawRecordReadHelper, rawRecordWriterHelper);
        final DateAccess dateAccess =
                new DB3DateAccess(rawRecordReadHelper, rawRecordWriterHelper, timeZone);
        final MemoAccess memoAccess = new DB3MemoAccess();
        return new DB3Access(characterAccess, logicalAccess, numericAccess, dateAccess,
                memoAccess);
    }
}
