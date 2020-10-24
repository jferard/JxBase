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

package com.github.jferard.jxbase.dialect.foxpro;

import com.github.jferard.jxbase.core.XBaseDialect;
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
import com.github.jferard.jxbase.dialect.db4.field.DB4FloatAccess;
import com.github.jferard.jxbase.dialect.db4.field.FloatAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.CurrencyAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.DatetimeAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.DoubleAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.FoxProCurrencyAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.FoxProDatetimeAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.FoxProDoubleAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.FoxProIntegerAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.FoxProNullFlagsAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.IntegerAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.NullFlagsAccess;
import com.github.jferard.jxbase.dialect.foxpro.memo.FoxProMemoRecordFactory;
import com.github.jferard.jxbase.dialect.foxpro.memo.FoxProMemoWriter;
import com.github.jferard.jxbase.dialect.foxpro.memo.FoxProMemoReader;
import com.github.jferard.jxbase.dialect.foxpro.reader.FoxProMemoFileHeaderReader;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TimeZone;

public class FoxProDialectFactory {
    public static FoxProDialectFactory create(final XBaseFileTypeEnum type, final Charset charset,
                                              final TimeZone timeZone) {
        final RawRecordReadHelper rawRecordReader = new RawRecordReadHelper(charset);
        final RawRecordWriteHelper rawRecordWriter = new RawRecordWriteHelper(charset);
        final CharacterAccess characterAccess =
                new DB2CharacterAccess(rawRecordReader, rawRecordWriter);
        final DateAccess dateAccess = new DB3DateAccess(rawRecordReader, rawRecordWriter, timeZone);
        final FloatAccess floatAccess = new DB4FloatAccess(rawRecordReader, rawRecordWriter);
        final LogicalAccess logicalAccess = new DB2LogicalAccess(rawRecordReader, rawRecordWriter);
        final NumericAccess numericAccess = new DB2NumericAccess(rawRecordReader, rawRecordWriter);
        final DatetimeAccess datetimeAccess = new FoxProDatetimeAccess();
        final NullFlagsAccess nullFlagsAccess = new FoxProNullFlagsAccess();
        final IntegerAccess integerAccess = new FoxProIntegerAccess();
        final DoubleAccess doubleAccess = new FoxProDoubleAccess();
        final CurrencyAccess currencyAccess = new FoxProCurrencyAccess();
        return new FoxProDialectFactory(type, charset, characterAccess, dateAccess, floatAccess,
                logicalAccess, numericAccess, datetimeAccess, nullFlagsAccess, integerAccess,
                doubleAccess, currencyAccess);
    }

    private final CharacterAccess characterAccess;
    private final DateAccess dateAccess;
    private final FloatAccess floatAccess;
    private final LogicalAccess logicalAccess;
    private final NumericAccess numericAccess;
    private final DatetimeAccess datetimeAccess;
    private final NullFlagsAccess nullFlagsAccess;
    private final IntegerAccess integerAccess;
    private final DoubleAccess doubleAccess;
    private final XBaseFileTypeEnum type;
    private final Charset charset;
    private MemoAccess memoAccess;
    private final CurrencyAccess currencyAccess;

    public FoxProDialectFactory(final XBaseFileTypeEnum type, final Charset charset,
                                final CharacterAccess characterAccess, final DateAccess dateAccess,
                                final FloatAccess floatAccess, final LogicalAccess logicalAccess,
                                final NumericAccess numericAccess,
                                final DatetimeAccess datetimeAccess,
                                final NullFlagsAccess nullFlagsAccess,
                                final IntegerAccess integerAccess,
                                final DoubleAccess doubleAccess,
                                final CurrencyAccess currencyAccess) {
        this.type = type;
        this.charset = charset;
        this.characterAccess = characterAccess;
        this.dateAccess = dateAccess;
        this.floatAccess = floatAccess;
        this.logicalAccess = logicalAccess;
        this.numericAccess = numericAccess;
        this.datetimeAccess = datetimeAccess;
        this.nullFlagsAccess = nullFlagsAccess;
        this.integerAccess = integerAccess;
        this.doubleAccess = doubleAccess;
        this.currencyAccess = currencyAccess;
        this.memoAccess = null;
    }

    public FoxProDialectFactory reader(final String tableName) throws IOException {
        final String filename = tableName + this.type.memoFileType().getExtension();
        final File memoFile = IOUtils.getIgnoreCaseFile(filename);
        final XBaseMemoReader memoReader;
        if (memoFile == null) {
            memoReader = null;
        } else {
            final FileChannel memoChannel = new FileInputStream(memoFile).getChannel();
            memoReader =
                    FoxProMemoReader.create(memoChannel, new FoxProMemoRecordFactory(this.charset),
                            new FoxProMemoFileHeaderReader());
        }
        return this.reader(memoReader);
    }

    public FoxProDialectFactory reader(final XBaseMemoReader memoReader) {
        this.memoAccess =
                new DB3MemoAccess(memoReader, null, new RawRecordReadHelper(this.charset));
        return this;
    }

    public FoxProDialectFactory writer(final String tableName,
                                       final Map<String, Object> memoHeaderMetadata)
            throws IOException {
        final XBaseMemoReader memoReader = null;
        final File memoFile = new File(tableName + this.type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileOutputStream(memoFile).getChannel();
        final XBaseMemoWriter memoWriter =
                new FoxProMemoWriter(memoChannel, 512, memoHeaderMetadata);
        this.memoAccess =
                new DB3MemoAccess(memoReader, memoWriter, new RawRecordReadHelper(this.charset));
        return this;
    }

    public XBaseDialect<FoxProDialect, VisualFoxProAccess> build() {
        final VisualFoxProAccess access =
                new VisualFoxProAccess(this.characterAccess, this.dateAccess, this.datetimeAccess,
                        this.floatAccess, this.integerAccess, this.logicalAccess, this.memoAccess,
                        this.nullFlagsAccess, this.numericAccess, this.doubleAccess, this.currencyAccess);
        return new FoxProDialect(this.type, access);
    }
}
