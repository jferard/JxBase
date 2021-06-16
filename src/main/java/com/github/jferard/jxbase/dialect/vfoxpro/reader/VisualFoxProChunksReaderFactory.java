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

package com.github.jferard.jxbase.dialect.vfoxpro.reader;

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.dialect.db3.reader.DB3FieldDescriptorArrayReader;
import com.github.jferard.jxbase.dialect.db3.reader.DB3RecordReader;
import com.github.jferard.jxbase.dialect.foxpro.memo.FoxProMemoReader;
import com.github.jferard.jxbase.dialect.foxpro.memo.FoxProMemoRecordFactory;
import com.github.jferard.jxbase.dialect.foxpro.reader.FoxProMemoFileHeaderReader;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.reader.FoxProMetadataReader;
import com.github.jferard.jxbase.dialect.foxpro.reader.FoxProOptionalReader;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.reader.XBaseFieldDescriptorArrayReader;
import com.github.jferard.jxbase.reader.XBaseChunkReaderFactory;
import com.github.jferard.jxbase.reader.XBaseMetadataReader;
import com.github.jferard.jxbase.reader.XBaseOptionalReader;
import com.github.jferard.jxbase.reader.XBaseRecordReader;
import com.github.jferard.jxbase.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.TimeZone;

/**
 * A factory for VisualFoxPro chunks readers.
 */
public class VisualFoxProChunksReaderFactory implements
        XBaseChunkReaderFactory<VisualFoxProDialect, VisualFoxProAccess> {
    protected final VisualFoxProDialect dialect;
    protected final TimeZone timezone;

    public VisualFoxProChunksReaderFactory(final VisualFoxProDialect dialect, final TimeZone timezone) {
        this.dialect = dialect;
        this.timezone = timezone;
    }

    @Override
    public XBaseMetadataReader createMetadataReader(final InputStream inputStream) {
        return new FoxProMetadataReader<VisualFoxProDialect, VisualFoxProAccess>(this.dialect, inputStream);
    }

    @Override
    public XBaseFieldDescriptorArrayReader<VisualFoxProDialect, VisualFoxProAccess> createFieldDescriptorArrayReader(
            final InputStream inputStream, final XBaseMetadata metadata) {
        return new DB3FieldDescriptorArrayReader<VisualFoxProDialect, VisualFoxProAccess>(this.dialect, inputStream,
                metadata);
    }

    @Override
    public XBaseRecordReader createRecordReader(final InputStream inputStream,
                                                final Charset charset,
                                                final XBaseMemoReader memoReader,
                                                final XBaseMetadata metadata,
                                                final XBaseFieldDescriptorArray<VisualFoxProAccess> array,
                                                final Object optional) {
        return new DB3RecordReader<VisualFoxProAccess>(this.dialect.getAccess(), inputStream,
                memoReader, charset, array,
                this.timezone);
    }

    @Override
    public XBaseMemoReader createMemoReader(final XBaseFileTypeEnum type, final String tableName,
                                            final Charset charset)
            throws IOException {
        final String filename = tableName + type.memoFileType().getExtension();
        final File memoFile = IOUtils.getIgnoreCaseFile(filename);
        final XBaseMemoReader memoReader;
        if (memoFile == null) {
            memoReader = null;
        } else {
            final FileChannel memoChannel = new FileInputStream(memoFile).getChannel();
            memoReader =
                    FoxProMemoReader.create(memoChannel, new FoxProMemoRecordFactory(charset),
                            new FoxProMemoFileHeaderReader());
        }
        return memoReader;
    }

    @Override
    public XBaseOptionalReader createOptionalReader(final InputStream inputStream,
                                                    final Charset charset,
                                                    final XBaseMetadata metadata,
                                                    final XBaseFieldDescriptorArray<VisualFoxProAccess> array) {
        return new FoxProOptionalReader<VisualFoxProDialect, VisualFoxProAccess>(this.dialect, inputStream, metadata, array);
    }
}
