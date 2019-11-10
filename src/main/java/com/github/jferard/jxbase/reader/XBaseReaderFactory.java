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

import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMemoFileType;
import com.github.jferard.jxbase.reader.internal.GenericInternalReaderFactory;
import com.github.jferard.jxbase.reader.internal.XBaseInternalReaderFactory;
import com.github.jferard.jxbase.util.IOUtils;
import com.github.jferard.jxbase.util.XBaseMetadataUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.TimeZone;

public class XBaseReaderFactory {
    public static XBaseReader<?> createReader(final String databaseName, final Charset charset)
            throws IOException {
        return new XBaseReaderFactory().create(databaseName, charset);
    }

    private XBaseReader<?> create(final String databaseName, final Charset charset)
            throws IOException {
        final InputStream dbfInputStream = new FileInputStream(databaseName + ".dbf");
        final InputStream resettableInputStream =
                IOUtils.resettable(dbfInputStream, XBaseMetadataUtils.BUFFER_SIZE);
        final XBaseFileTypeEnum type = this.getXBaseFileType(resettableInputStream);

        final XBaseDialect dialect = XBaseFileTypeEnum.getDialect(type);
        final XBaseInternalReaderFactory readerFactory =
                new GenericInternalReaderFactory(dialect, TimeZone.getDefault());
        final XBaseMemoReader memoReader = this.getMemoReader(databaseName, charset, dialect);
        return new GenericReader(dialect, resettableInputStream, charset, readerFactory,
                memoReader);
    }

    private XBaseFileTypeEnum getXBaseFileType(final InputStream resettableInputStream)
            throws IOException {
        resettableInputStream.mark(1);
        final int firstByte = resettableInputStream.read();
        resettableInputStream.reset();
        if (firstByte == -1) {
            throw new IOException("Stream is empty");
        }
        return XBaseFileTypeEnum.fromInt((byte) firstByte);
    }

    private XBaseMemoReader getMemoReader(final String databaseName, final Charset charset,
                                          final XBaseDialect dialect) throws IOException {
        if (dialect.memoFileType() != XBaseMemoFileType.NO_MEMO_FILE) {
            // TODO: handle cases with a factory
            final File memoFile = new File(databaseName + dialect.memoFileType().getExtension());
            return GenericMemoReader.fromChannel(memoFile, charset);
        }

        return null;
    }
}
