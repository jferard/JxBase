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

package com.github.jferard.jxbase.reader;

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.util.DbfMetadataUtils;
import com.github.jferard.jxbase.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XBaseReaderFactory {
    public static XBaseReader create(final File dbfFile) throws IOException {
        return new XBaseReaderFactory().create(new FileInputStream(dbfFile), null);
    }

    public static XBaseReader create(final File dbfFile, final File memoFile) throws IOException {
        return new XBaseReaderFactory().create(new FileInputStream(dbfFile), memoFile);
    }

    private XBaseReader create(final InputStream dbfInputStream, final File memoFile) throws IOException {
        final InputStream resettableInputStream =
                IOUtils.resettable(dbfInputStream, DbfMetadataUtils.BUFFER_SIZE);
        XBaseFileTypeEnum type = this.getXBaseFileType(resettableInputStream);
        switch (type) {
            default:
                return new DbfReader(dbfInputStream, MemoReaderFactory.fromRandomAccess(type, memoFile));
        }
    }

    private XBaseFileTypeEnum getXBaseFileType(InputStream resettableInputStream) throws IOException {
        resettableInputStream.mark(1);
        final int firstByte = resettableInputStream.read();
        resettableInputStream.reset();
        if (firstByte == -1) {
            throw new IOException("Stream is empty");
        }
        return XBaseFileTypeEnum.fromInt((byte) firstByte);
    }
}
