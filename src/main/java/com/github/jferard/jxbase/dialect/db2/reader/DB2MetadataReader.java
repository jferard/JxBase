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

package com.github.jferard.jxbase.dialect.db2.reader;

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.dialect.db2.DB2Utils;
import com.github.jferard.jxbase.reader.XBaseMetadataReader;
import com.github.jferard.jxbase.util.BytesUtils;
import com.github.jferard.jxbase.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A meta data reader for DB2. The DB2 meta data format is different from the DB3 meta data format.  
 */
public class DB2MetadataReader implements XBaseMetadataReader {
    private final InputStream dbfInputStream;
    private final DB2Dialect dialect;

    public DB2MetadataReader(final DB2Dialect dialect, final InputStream dbfInputStream) {
        this.dialect = dialect;
        this.dbfInputStream = dbfInputStream;
    }

    /**
     * http://www.fileformat.info/format/dbf/corion-dbase-ii.htm
     */
    @Override
    public GenericMetadata read() throws IOException {
        final byte[] headerBytes = new byte[0x208];
        if (IOUtils.readFully(this.dbfInputStream, headerBytes) != 0x208) {
            throw new IOException("A DB2 file has a header of 520 bytes");
        }
        final byte typeByte = headerBytes[0];
        XBaseFileTypeEnum.fromInt(typeByte);
        final int recordsQty = BytesUtils.extractLEInt2(headerBytes, 1);
        final Date updateDate =
                DB2Utils.createHeaderUpdateDate(headerBytes[3], headerBytes[4], headerBytes[5]);
        final int oneRecordLength = BytesUtils.extractLEInt2(headerBytes, 6);
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB2Utils.META_UPDATE_DATE, updateDate);
        meta.put(DB2Utils.META_RECORDS_QTY, recordsQty);
        return new GenericMetadata(typeByte, 0x208, oneRecordLength, meta);
    }

}
