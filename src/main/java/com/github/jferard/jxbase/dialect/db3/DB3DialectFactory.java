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

package com.github.jferard.jxbase.dialect.db3;

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
import com.github.jferard.jxbase.dialect.db3.memo.DB3MemoReader;
import com.github.jferard.jxbase.dialect.db3.memo.DB3MemoWriter;
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

public class DB3DialectFactory {
    public static DB3DialectFactory create(final XBaseFileTypeEnum type, final Charset charset,
                                           final TimeZone timeZone) {
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
        final MemoAccess memoAccess = null;
        return new DB3DialectFactory(type, characterAccess, logicalAccess, numericAccess,
                dateAccess, rawRecordReadHelper);
    }


    private final CharacterAccess characterAccess;
    private final LogicalAccess logicalAccess;
    private final NumericAccess numericAccess;
    private final DateAccess dateAccess;
    private final RawRecordReadHelper rawRecordReadHelper;
    private final XBaseFileTypeEnum type;
    private MemoAccess memoAccess; // late init

    DB3DialectFactory(final XBaseFileTypeEnum type, final CharacterAccess characterAccess,
                      final LogicalAccess logicalAccess, final NumericAccess numericAccess,
                      final DateAccess dateAccess, final RawRecordReadHelper rawRecordReadHelper) {
        this.type = type;
        this.characterAccess = characterAccess;
        this.logicalAccess = logicalAccess;
        this.numericAccess = numericAccess;
        this.dateAccess = dateAccess;
        this.memoAccess = null; // late init
        this.rawRecordReadHelper = rawRecordReadHelper;
    }

    public DB3DialectFactory reader(final String databaseName) throws IOException {
        // TODO: nio reader and randomAccess reader
        // new RandomAccessFile(memoFile, "r").getChannel();
        final File memoFile = new File(databaseName + this.type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileInputStream(memoFile).getChannel();
        final XBaseMemoReader memoReader = DB3MemoReader.create(memoChannel);
        return this.reader(memoReader);
    }

    public DB3DialectFactory reader(final XBaseMemoReader memoReader) {
        this.memoAccess = new DB3MemoAccess(memoReader, null, this.rawRecordReadHelper);
        return this;
    }

    public DB3DialectFactory writer(final String databaseName,
                                    final Map<String, Object> memoHeaderMetadata)
            throws IOException {
        final File memoFile = new File(databaseName + this.type.memoFileType().getExtension());
        final FileChannel memoChannel = new FileOutputStream(memoFile).getChannel();
        final XBaseMemoWriter memoWriter = new DB3MemoWriter(memoChannel, memoHeaderMetadata);
        this.memoAccess = new DB3MemoAccess(null, memoWriter, null);
        return this;
    }

    public DB3Dialect build() {
        final DB3Access access =
                new DB3Access(this.characterAccess, this.logicalAccess, this.numericAccess, this.dateAccess,
                        this.memoAccess);
        return new DB3Dialect(this.type, access);
    }
}
