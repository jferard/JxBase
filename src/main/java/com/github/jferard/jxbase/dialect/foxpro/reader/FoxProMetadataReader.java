/*
* JxBase - Copyright (c) 2019-2021 Julien Férard
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

package com.github.jferard.jxbase.dialect.foxpro.reader;

import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.DB2Utils;
import com.github.jferard.jxbase.dialect.foxpro.FoxProUtils;
import com.github.jferard.jxbase.reader.XBaseMetadataReader;
import com.github.jferard.jxbase.util.BytesUtils;
import com.github.jferard.jxbase.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A foxpro meta data reader
 */
public class FoxProMetadataReader<A extends XBaseAccess, D extends XBaseDialect<A, D>> implements XBaseMetadataReader {
    private final InputStream dbfInputStream;
    private final D dialect;

    public FoxProMetadataReader(final D dialect,
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
        final int recordsQty = BytesUtils.extractLEInt4(headerBytes, 4);
        final int fullHeaderLength = BytesUtils.extractLEInt2(headerBytes, 8);
        final int oneRecordLength = BytesUtils.extractLEInt2(headerBytes, 10);
        // 12-31: Reserved; filled with zeros.

        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(FoxProUtils.META_UPDATE_DATE, updateDate);
        meta.put(FoxProUtils.META_RECORDS_QTY, recordsQty);
        return new GenericMetadata(type.toByte(), fullHeaderLength, oneRecordLength, meta);
    }
}
