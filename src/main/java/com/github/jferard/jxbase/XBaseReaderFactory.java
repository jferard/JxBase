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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.reader.GenericReader;
import com.github.jferard.jxbase.reader.XBaseInternalReaderFactory;
import com.github.jferard.jxbase.util.IOUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * A factory for reader. One of the hardships is that you can't guess the dialect, hence the
 * wildcard.
 */
public class XBaseReaderFactory {
    /**
     * Create a new reader
     * @param tableName a full table name : path/to/table, without the .dbf extension
     * @param charset   the charset.
     * @return          a reader
     * @throws IOException
     */
    public static XBaseReader<?, ?> createReader(final String tableName, final Charset charset)
            throws IOException {
        return new XBaseReaderFactory().create(tableName, charset);
    }

    private XBaseReader<?, ?> create(final String tableName, final Charset charset)
            throws IOException {
        final File file = IOUtils.getFile(tableName + ".dbf");
        if (file == null) {
            throw new IllegalArgumentException(
                    String.format("Can't find table %s (file %s.dbf doesn't exist)", tableName,
                            tableName));
        }
        final InputStream dbfInputStream = new FileInputStream(file);
        final InputStream resettableInputStream =
                IOUtils.resettable(dbfInputStream, JxBaseUtils.BUFFER_SIZE);
        final XBaseFileTypeEnum type = this.getXBaseFileType(resettableInputStream);

        final XBaseDialect<?, ?> dialect =
                DialectFactory.getDialect(type, tableName, charset, null);
        final XBaseInternalReaderFactory<?, ?> readerFactory =
                dialect.getInternalReaderFactory(tableName, charset);
        return new GenericReader(dialect, resettableInputStream, charset, readerFactory);
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
}
