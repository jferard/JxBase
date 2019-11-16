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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.core.XBaseFieldFactory;
import com.github.jferard.jxbase.core.XBaseLengths;
import com.github.jferard.jxbase.core.XBaseRepresentations;
import com.github.jferard.jxbase.reader.internal.XBaseInternalReaderFactory;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.writer.internal.XBaseInternalWriterFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

public interface XBaseDialect extends XBaseLengths, XBaseRepresentations, XBaseFieldFactory {
    long getOffsetInBlocks(XBaseRecordReader genericRecordReader, byte[] recordBuffer, int offset,
                           int length);

    XBaseFileTypeEnum getType();

    int getMetaDataLength();

    int getFieldDescriptorLength();

    int getOptionalLength();

    //    XBaseMemoFileType memoFileType();

    XBaseInternalReaderFactory getInternalReaderFactory(String databaseName, Charset charset)
            throws IOException;

    XBaseInternalWriterFactory getInternalWriterFactory(String databaseName, Charset charset,
                                                        Map<String, Object> headerMeta)
            throws IOException;
}
