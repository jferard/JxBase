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

package com.github.jferard.jxbase.dialect.db4;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
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
import com.github.jferard.jxbase.dialect.db3.reader.DB3MemoFileHeaderReader;
import com.github.jferard.jxbase.dialect.db4.field.DB4FloatAccess;
import com.github.jferard.jxbase.dialect.db4.field.FloatAccess;
import com.github.jferard.jxbase.dialect.db4.memo.DB4MemoReader;
import com.github.jferard.jxbase.dialect.db4.memo.DB4MemoWriter;
import com.github.jferard.jxbase.dialect.db4.reader.DB4MemoFileHeaderReader;
import com.github.jferard.jxbase.dialect.foxpro.memo.FoxProMemoRecordFactory;
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

public class DB4DialectFactory {
    public static DB4DialectFactory create(final XBaseFileTypeEnum type, final Charset charset,
                                           final TimeZone timeZone) {
        final RawRecordReadHelper rawRecordReader = new RawRecordReadHelper(charset);
        final RawRecordWriteHelper rawRecordWriter = new RawRecordWriteHelper(charset);
        final CharacterAccess characterAccess =
                new DB2CharacterAccess(rawRecordReader, rawRecordWriter);
        final DateAccess dateAccess = new DB3DateAccess(rawRecordReader, rawRecordWriter, timeZone);
        final FloatAccess floatAccess = new DB4FloatAccess(rawRecordReader, rawRecordWriter);
        final LogicalAccess logicalAccess = new DB2LogicalAccess(rawRecordReader, rawRecordWriter);
        final NumericAccess numericAccess = new DB2NumericAccess(rawRecordReader, rawRecordWriter);
        return new DB4DialectFactory(type, charset, characterAccess, dateAccess, floatAccess,
                logicalAccess, numericAccess);
    }

    private final CharacterAccess characterAccess;
    private final DateAccess dateAccess;
    private final FloatAccess floatAccess;
    private final LogicalAccess logicalAccess;
    private final NumericAccess numericAccess;
    private final Charset charset;
    private final XBaseFileTypeEnum type;
    private MemoAccess memoAccess;

    public DB4DialectFactory(final XBaseFileTypeEnum type, final Charset charset,
                             final CharacterAccess characterAccess, final DateAccess dateAccess,
                             final FloatAccess floatAccess, final LogicalAccess logicalAccess,
                             final NumericAccess numericAccess) {
        this.type = type;
        this.charset = charset;
        this.characterAccess = characterAccess;
        this.dateAccess = dateAccess;
        this.floatAccess = floatAccess;
        this.logicalAccess = logicalAccess;
        this.numericAccess = numericAccess;
        this.memoAccess = null;
    }

    public DB4DialectFactory reader(final String databaseName) throws IOException {
        final File memoFile = new File(databaseName + this.type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileInputStream(memoFile).getChannel();
        /* other version:
         * final FileChannel memoChannel = new RandomAccessFile(memoFile, "r").getChannel();
         */
        final XBaseMemoReader memoReader =
                DB4MemoReader.create(memoChannel, new FoxProMemoRecordFactory(this.charset),
                        new DB4MemoFileHeaderReader());
        final XBaseMemoWriter memoWriter = null;
        this.memoAccess =
                new DB3MemoAccess(memoReader, memoWriter, new RawRecordReadHelper(this.charset));
        return this;
    }

    public DB4DialectFactory writer(final String databaseName,
                                    final Map<String, Object> memoHeaderMetadata)
            throws IOException {
        final XBaseMemoReader memoReader = null;
        final File memoFile = new File(databaseName + this.type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileOutputStream(memoFile).getChannel();
        /* other version:
         * final FileChannel memoChannel = new RandomAccessFile(memoFile, "rw").getChannel();
         */
        final XBaseMemoWriter memoWriter =
                new DB4MemoWriter(memoChannel, DB3MemoFileHeaderReader.BLOCK_LENGTH,
                        memoHeaderMetadata);
        this.memoAccess =
                new DB3MemoAccess(memoReader, memoWriter, new RawRecordReadHelper(this.charset));
        return this;
    }

    public XBaseDialect<DB4Dialect, DB4Access> build() {
        final DB4Access access =
                new DB4Access(this.characterAccess, this.dateAccess, this.floatAccess,
                        this.logicalAccess, this.memoAccess, this.numericAccess);
        return new DB4Dialect(this.type, access);
    }
}
