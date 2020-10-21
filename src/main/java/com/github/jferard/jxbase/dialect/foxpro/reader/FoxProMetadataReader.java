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

package com.github.jferard.jxbase.dialect.foxpro.reader;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.dialect.foxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.foxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.FoxProUtils;
import com.github.jferard.jxbase.reader.XBaseMetadataReader;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FoxProMetadataReader implements XBaseMetadataReader {
    private final InputStream dbfInputStream;
    private final XBaseDialect<VisualFoxProDialect, VisualFoxProAccess> dialect;

    public FoxProMetadataReader(final XBaseDialect dialect,
                                final InputStream dbfInputStream) {
        this.dialect = dialect;
        this.dbfInputStream = dbfInputStream;
    }

    /**
     * https://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm, 1.1 Table File Header
     */
    @Override
    public GenericMetadata read() throws IOException {
        final int metadataLength = this.dialect.getMetaDataLength();
        final byte[] headerBytes = new byte[metadataLength];
        if (IOUtils.readFully(this.dbfInputStream, headerBytes) != metadataLength) {
            throw new IOException("The file is corrupted or is not a dbf file");
        }
        final byte typeByte = headerBytes[0];
        final XBaseFileTypeEnum type = XBaseFileTypeEnum.fromInt(typeByte);
        final Date updateDate =
                FoxProUtils.createHeaderUpdateDate(headerBytes[1], headerBytes[2], headerBytes[3]);
        final int recordsQty =
                BitUtils.makeInt(headerBytes[4], headerBytes[5], headerBytes[6], headerBytes[7]);
        final int fullHeaderLength = BitUtils.makeInt(headerBytes[8], headerBytes[9]);
        final int oneRecordLength = BitUtils.makeInt(headerBytes[10], headerBytes[11]);
        // 12-31: Reserved; filled with zeros.

        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("updateDate", updateDate);
        meta.put("recordsQty", recordsQty);
        return new GenericMetadata(type.toByte(), fullHeaderLength, oneRecordLength, meta);
    }
}
