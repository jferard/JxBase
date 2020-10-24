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

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.DB2NumericAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericAccess;
import com.github.jferard.jxbase.dialect.db3.DB3Utils;
import com.github.jferard.jxbase.dialect.db3.field.DB3DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.DB3MemoAccess;
import com.github.jferard.jxbase.dialect.db3.field.DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.dialect.db4.field.DB4FloatAccess;
import com.github.jferard.jxbase.dialect.db4.field.FloatAccess;
import com.github.jferard.jxbase.dialect.db4.memo.DB4MemoReader;
import com.github.jferard.jxbase.dialect.db4.memo.DB4MemoWriter;
import com.github.jferard.jxbase.dialect.db4.reader.DB4MemoFileHeaderReader;
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

/**
 * A DB4 dialect builder.
 */
public class DB4DialectBuilder {
    /**
     * Create the builder
     *
     * @param type     the actual type of the DBf file
     * @param charset  the charset
     * @param timeZone the time zone
     * @return the builder
     */
    public static DB4DialectBuilder create(final XBaseFileTypeEnum type, final Charset charset,
                                           final TimeZone timeZone) {
        final RawRecordReadHelper rawRecordReader = new RawRecordReadHelper(charset);
        final RawRecordWriteHelper rawRecordWriter = new RawRecordWriteHelper(charset);
        final CharacterAccess characterAccess =
                new DB2CharacterAccess(rawRecordReader, rawRecordWriter);
        final DateAccess dateAccess = new DB3DateAccess(rawRecordReader, rawRecordWriter, timeZone);
        final FloatAccess floatAccess = new DB4FloatAccess(rawRecordReader, rawRecordWriter);
        final LogicalAccess logicalAccess = new DB2LogicalAccess(rawRecordReader, rawRecordWriter);
        final NumericAccess numericAccess = new DB2NumericAccess(rawRecordReader, rawRecordWriter);
        return new DB4DialectBuilder(type, characterAccess, dateAccess, floatAccess,
                logicalAccess, numericAccess, rawRecordReader);
    }

    private final CharacterAccess characterAccess;
    private final DateAccess dateAccess;
    private final FloatAccess floatAccess;
    private final LogicalAccess logicalAccess;
    private final NumericAccess numericAccess;
    private final RawRecordReadHelper rawRecordReader;
    private final XBaseFileTypeEnum type;
    private MemoAccess memoAccess;

    public DB4DialectBuilder(final XBaseFileTypeEnum type,
                             final CharacterAccess characterAccess, final DateAccess dateAccess,
                             final FloatAccess floatAccess, final LogicalAccess logicalAccess,
                             final NumericAccess numericAccess,
                             final RawRecordReadHelper rawRecordReader) {
        this.type = type;
        this.characterAccess = characterAccess;
        this.dateAccess = dateAccess;
        this.floatAccess = floatAccess;
        this.logicalAccess = logicalAccess;
        this.numericAccess = numericAccess;
        this.rawRecordReader = rawRecordReader;
        this.memoAccess = null;
    }

    /**
     * This dialect needs info about the memo file
     *
     * @param tableName the table name, to find the memo
     * @return this for fluent style
     * @throws IOException
     */
    public DB4DialectBuilder reader(final String tableName) throws IOException {
        final File memoFile = new File(tableName + this.type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileInputStream(memoFile).getChannel();
        /* other version:
         * final FileChannel memoChannel = new RandomAccessFile(memoFile, "r").getChannel();
         */
        final XBaseMemoReader memoReader =
                DB4MemoReader.create(memoChannel, new DB4MemoFileHeaderReader());
        return this.reader(memoReader);
    }

    /**
     * This dialect needs info about the memo file
     * @param memoReader the memo reader
     * @return this for fluent style
     * @throws IOException
     */
    private DB4DialectBuilder reader(final XBaseMemoReader memoReader) {
        this.memoAccess =
                new DB3MemoAccess(memoReader, null, this.rawRecordReader);
        return this;
    }

    /**
     * This dialect needs info about the memo file
     * @param tableName the table name, to find the memo
     * @param memoHeaderMetadata the memo header data.
     * @return this for fluent style
     * @throws IOException
     */
    public DB4DialectBuilder writer(final String tableName,
                                    final Map<String, Object> memoHeaderMetadata)
            throws IOException {
        final File memoFile = new File(tableName + this.type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileOutputStream(memoFile).getChannel();
        /* other version:
         * final FileChannel memoChannel = new RandomAccessFile(memoFile, "rw").getChannel();
         */
        final XBaseMemoWriter memoWriter =
                new DB4MemoWriter(memoChannel, DB3Utils.BLOCK_LENGTH,
                        memoHeaderMetadata);
        return this.writer(memoWriter);
    }

    private DB4DialectBuilder writer(final XBaseMemoWriter memoWriter) {
        this.memoAccess =
                new DB3MemoAccess(null, memoWriter, this.rawRecordReader);
        return this;
    }

    /**
     * @return the dialect
     */
    public XBaseDialect<DB4Dialect, DB4Access> build() {
        final DB4Access access =
                new DB4Access(this.characterAccess, this.dateAccess, this.floatAccess,
                        this.logicalAccess, this.memoAccess, this.numericAccess);
        return new DB4Dialect(this.type, access);
    }
}
