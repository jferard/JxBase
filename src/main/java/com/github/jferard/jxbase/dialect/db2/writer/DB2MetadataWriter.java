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

package com.github.jferard.jxbase.dialect.db2.writer;

import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.dialect.db2.DB2Utils;
import com.github.jferard.jxbase.util.BytesUtils;
import com.github.jferard.jxbase.writer.XBaseMetadataWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * A writer for DB2 meta data.
 *
 * @param <D> the dialect
 * @param <A> the access
 */
public class DB2MetadataWriter<A extends XBaseAccess, D extends XBaseDialect<A, D>>
        implements XBaseMetadataWriter<A, D> {
    final OutputStream out;
    final Charset charset;
    private final RandomAccessFile file;
    private final D dialect;

    public DB2MetadataWriter(final D dialect, final RandomAccessFile file, final OutputStream out,
                             final Charset charset) {
        this.dialect = dialect;
        this.file = file;
        this.out = out;
        this.charset = charset;
    }

    @Override
    public void write(final XBaseMetadata metadata) throws IOException {
        this.out.write(metadata.getFileTypeByte());
        final Object r = metadata.get("recordsQty");
        if (r instanceof Number) {
            final int recordsQty = ((Number) r).intValue();
            BytesUtils.writeLEByte2(this.out, recordsQty);
        } else {
            BytesUtils.writeZeroes(this.out, 2);
        }
        final Object d = metadata.get("updateDate");
        if (d instanceof Date) {
            final Date updateDate = (Date) d;
            DB2Utils.writeHeaderUpdateDate(this.out, updateDate);
        } else {
            BytesUtils.writeZeroes(this.out, 3);
        }
        BytesUtils.writeLEByte2(this.out, metadata.getOneRecordLength());
    }

    @Override
    public void fixMetadata(final int recordQty) throws IOException {
        this.out.flush();
        this.file.seek(1);
        BytesUtils.writeLEByte2(this.out, recordQty);
        DB2Utils.writeHeaderUpdateDate(this.out, new Date());
    }

    @Override
    public void close() throws IOException {
        this.out.flush();
        this.out.close();
    }
}
