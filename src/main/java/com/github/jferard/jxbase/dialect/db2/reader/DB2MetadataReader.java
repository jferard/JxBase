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

package com.github.jferard.jxbase.dialect.db2.reader;

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.dialect.db2.DB2Utils;
import com.github.jferard.jxbase.reader.internal.XBaseMetadataReader;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
            throw new IOException("The file is corrupted or is not a dbf file");
        }
        final byte typeByte = headerBytes[0];
        final XBaseFileTypeEnum type = XBaseFileTypeEnum.fromInt(typeByte);
        final int recordsQty = BitUtils.makeInt(headerBytes[1], headerBytes[2]);
        final Date updateDate =
                DB2Utils.createHeaderUpdateDate(headerBytes[3], headerBytes[4], headerBytes[5]);
        final int oneRecordLength = BitUtils.makeInt(headerBytes[6], headerBytes[7]);
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("updateDate", updateDate);
        meta.put("recordsQty", recordsQty);
        return new GenericMetadata(typeByte, 0x208, oneRecordLength, meta);
    }

}
