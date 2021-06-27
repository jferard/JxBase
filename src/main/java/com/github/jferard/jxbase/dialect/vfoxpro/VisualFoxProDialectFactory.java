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

package com.github.jferard.jxbase.dialect.vfoxpro;

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2NumericAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericAccess;
import com.github.jferard.jxbase.dialect.db3.field.DB3DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.dialect.db4.field.DB4FloatAccess;
import com.github.jferard.jxbase.dialect.db4.field.FloatAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.CurrencyAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.DatetimeAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.DoubleAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.FoxProCurrencyAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.FoxProDatetimeAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.FoxProDoubleAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.FoxProIntegerAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.FoxProMemoAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.FoxProNullFlagsAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.IntegerAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.field.NullFlagsAccess;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;

import java.nio.charset.Charset;
import java.util.TimeZone;

/**
 * A VisualFoxPro dialect factory.
 */
public class VisualFoxProDialectFactory {
    /**
     * Create the builder
     *
     * @param type     the actual type of the DBf file
     * @param charset  the charset
     * @param timeZone the time zone
     * @return the builder
     */
    public static VisualFoxProDialect create(final XBaseFileTypeEnum type, final Charset charset,
                                             final TimeZone timeZone) {
        final VisualFoxProAccess access =
                createAccess(charset, timeZone);
        return new VisualFoxProDialect(type, access);
    }

    public static VisualFoxProAccess createAccess(final Charset charset, final TimeZone timeZone) {
        final RawRecordReadHelper rawRecordReader = new RawRecordReadHelper(charset);
        final RawRecordWriteHelper rawRecordWriter = new RawRecordWriteHelper(charset);
        final CharacterAccess characterAccess =
                new DB2CharacterAccess(rawRecordReader, rawRecordWriter);
        final DateAccess dateAccess = new DB3DateAccess(rawRecordReader, rawRecordWriter, timeZone);
        final FloatAccess floatAccess = new DB4FloatAccess(rawRecordWriter);
        final LogicalAccess logicalAccess = new DB2LogicalAccess(rawRecordWriter);
        final NumericAccess numericAccess = new DB2NumericAccess(rawRecordWriter);
        final DatetimeAccess datetimeAccess = new FoxProDatetimeAccess();
        final NullFlagsAccess nullFlagsAccess = new FoxProNullFlagsAccess();
        final IntegerAccess integerAccess = new FoxProIntegerAccess();
        final DoubleAccess doubleAccess = new FoxProDoubleAccess();
        final CurrencyAccess currencyAccess = new FoxProCurrencyAccess();
        final MemoAccess memoAccess = new FoxProMemoAccess();
        return new VisualFoxProAccess(characterAccess, dateAccess, datetimeAccess,
                floatAccess, integerAccess, logicalAccess, memoAccess,
                nullFlagsAccess, numericAccess, doubleAccess, currencyAccess);
    }
}
