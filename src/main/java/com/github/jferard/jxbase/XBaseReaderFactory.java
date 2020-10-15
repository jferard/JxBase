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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class XBaseReaderFactory {
    public static XBaseReader<?, ?> createReader(final String databaseName, final Charset charset)
            throws IOException {
        return new XBaseReaderFactory().create(databaseName, charset);
    }

    private XBaseReader<?, ?> create(final String databaseName, final Charset charset)
            throws IOException {
        final InputStream dbfInputStream =
                new FileInputStream(IOUtils.getFile(databaseName + ".dbf"));
        final InputStream resettableInputStream =
                IOUtils.resettable(dbfInputStream, JxBaseUtils.BUFFER_SIZE);
        final XBaseFileTypeEnum type = this.getXBaseFileType(resettableInputStream);

        final XBaseDialect<?, ?> dialect =
                XBaseFileTypeEnum.getDialect(type, databaseName, charset, null);
        final XBaseInternalReaderFactory<?, ?> readerFactory =
                dialect.getInternalReaderFactory(databaseName, charset);
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
