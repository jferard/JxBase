/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
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
import com.github.jferard.jxbase.dialect.db4.reader.DB4MemoReader;
import com.github.jferard.jxbase.dialect.db4.writer.DB4MemoWriter;
import com.github.jferard.jxbase.dialect.foxpro.field.DatetimeAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.FoxProDatetimeAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.FoxProIntegerAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.FoxProMemoAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.FoxProNullFlagsAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.IntegerAccess;
import com.github.jferard.jxbase.dialect.foxpro.field.NullFlagsAccess;
import com.github.jferard.jxbase.dialect.foxpro.reader.FoxProMemoFileHeaderReader;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TimeZone;

public class FoxProAccessFactory {

    private final RawRecordReadHelper rawRecordReader;
    private final CharacterAccess characterAccess;
    private final DateAccess dateAccess;
    private final FloatAccess floatAccess;
    private final LogicalAccess logicalAccess;
    private final NumericAccess numericAccess;
    private final DatetimeAccess datetimeAccess;
    private final NullFlagsAccess nullFlagsAccess;
    private final IntegerAccess integerAccess;
    private final XBaseFileTypeEnum type;
    private final Charset charset;
    private MemoAccess memoAccess;

    public FoxProAccessFactory(final XBaseFileTypeEnum type, final Charset charset,
                               final TimeZone timeZone) {
        this.type = type;
        this.rawRecordReader = new RawRecordReadHelper(charset);
        this.charset = charset;
        final RawRecordWriteHelper rawRecordWriter = new RawRecordWriteHelper(charset);
        this.characterAccess = new DB2CharacterAccess(this.rawRecordReader, rawRecordWriter);
        this.dateAccess = new DB3DateAccess(this.rawRecordReader, rawRecordWriter, timeZone);
        this.floatAccess = new DB4FloatAccess(this.rawRecordReader, rawRecordWriter);
        this.logicalAccess = new DB2LogicalAccess(this.rawRecordReader, rawRecordWriter);
        this.numericAccess = new DB2NumericAccess(this.rawRecordReader, rawRecordWriter);
        this.datetimeAccess = new FoxProDatetimeAccess();
        this.nullFlagsAccess = new FoxProNullFlagsAccess();
        this.integerAccess = new FoxProIntegerAccess();
        this.memoAccess = null;
    }

    public FoxProAccessFactory reader(final String databaseName) throws IOException {
        final File memoFile = new File(databaseName + this.type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileInputStream(memoFile).getChannel();
        final XBaseMemoReader memoReader =
                new DB4MemoReader(memoChannel, new FoxProMemoRecordFactory(this.charset),
                        new FoxProMemoFileHeaderReader());
        final XBaseMemoWriter memoWriter = null;
        this.memoAccess =
                new FoxProMemoAccess(memoReader, memoWriter, new RawRecordReadHelper(this.charset));
        return this;
    }

    public FoxProAccessFactory writer(final String databaseName,
                                      final Map<String, Object> memoHeaderMetadata)
            throws IOException {
        final XBaseMemoReader memoReader = null;
        final File memoFile = new File(databaseName + this.type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileOutputStream(memoFile).getChannel();
        final XBaseMemoWriter memoWriter = new DB4MemoWriter(memoChannel, 512, memoHeaderMetadata);
        this.memoAccess =
                new FoxProMemoAccess(memoReader, memoWriter, new RawRecordReadHelper(this.charset));
        return this;
    }

    public XBaseDialect<FoxProDialect, FoxProAccess> build() {
        final FoxProAccess access =
                new FoxProAccess(this.characterAccess, this.dateAccess, this.datetimeAccess,
                        this.floatAccess, this.integerAccess, this.logicalAccess, this.memoAccess,
                        this.nullFlagsAccess, this.numericAccess);
        return new FoxProDialect(this.type, access);
    }
}
