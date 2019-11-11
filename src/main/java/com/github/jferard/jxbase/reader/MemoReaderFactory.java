/*
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

package com.github.jferard.jxbase.reader;

import com.github.jferard.jxbase.core.memo.MemoRecordFactory;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Reader of memo files (tested of *.FPT files - Visual FoxPro)
 * See links:
 * <p>
 * Visual FoxPro file formats:
 * http://msdn.microsoft.com/en-us/library/aa977077(v=vs.71).aspx
 * <p>
 * DBase file formats:
 * http://www.dbase.com/Knowledgebase/INT/db7_file_fmt.htm
 * <p>
 * See: https://www.clicketyclick.dk/databases/xbase/format/fpt.html
 */
public class MemoReaderFactory {
    public static XBaseMemoReader fromRandomAccess(final XBaseFileTypeEnum type,
                                                   final File memoFile) throws IOException {
        final RandomAccessFile randomAccessFile = new RandomAccessFile(memoFile, "r");
        switch (type) {
            default:
                final MemoRecordFactory memoRecordFactory = null;
                return new GenericMemoReader(randomAccessFile.getChannel(), memoRecordFactory);
        }
    }

    public static XBaseMemoReader fromChannel(final XBaseFileTypeEnum type, final File memoFile)
            throws IOException {
        final FileInputStream fileInputStream = new FileInputStream(memoFile);
        switch (type) {
            default:
                final MemoRecordFactory memoRecordFactory = null;
                return new GenericMemoReader(fileInputStream.getChannel(), memoRecordFactory);
        }
    }
}