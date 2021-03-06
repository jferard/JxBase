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

package com.github.jferard.jxbase.dialect.db4.writer;

import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.dialect.db2.DB2Utils;
import com.github.jferard.jxbase.dialect.db3.DB3Utils;
import com.github.jferard.jxbase.dialect.db4.DB4Utils;
import com.github.jferard.jxbase.util.BytesUtils;
import com.github.jferard.jxbase.writer.XBaseMetadataWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

/**
 * A writer for DB4 meta data.
 *
 * @param <D> the dialect
 * @param <A> the access
 */
public class DB4MetadataWriter<A extends XBaseAccess, D extends XBaseDialect<A, D>>
        implements XBaseMetadataWriter<A, D> {
    final OutputStream out;
    final Charset charset;
    private final RandomAccessFile file;
    private final D dialect;

    public DB4MetadataWriter(final D dialect, final RandomAccessFile file,
                             final OutputStream out, final Charset charset) {
        this.dialect = dialect;
        this.file = file;
        this.out = out;
        this.charset = charset;
    }

    @Override
    public void write(final XBaseMetadata metadata) throws IOException {
        this.out.write(metadata.getFileTypeByte());
        DB2Utils.writeHeaderUpdateDate3(this.out, metadata.get(DB4Utils.META_UPDATE_DATE));
        DB3Utils.writeRecordQty4(this.out, metadata.get(DB4Utils.META_RECORDS_QTY));
        BytesUtils.writeLEByte2(this.out, metadata.getFullHeaderLength());
        BytesUtils.writeLEByte2(this.out, metadata.getOneRecordLength());
        BytesUtils.writeZeroes(this.out, 2);
        DB4Utils.writeFlag1(this.out, metadata.get(DB4Utils.META_UNCOMPLETED_TX_FLAG));
        DB4Utils.writeFlag1(this.out, metadata.get(DB4Utils.META_ENCRYPTION_FLAG));
        BytesUtils.writeZeroes(this.out, 12);
        DB4Utils.writeFlag1(this.out, metadata.get(DB4Utils.META_MDX_FLAG));
        DB4Utils.writeFlag1(this.out, metadata.get(DB4Utils.META_LANGUAGE_DRIVER_ID));
        BytesUtils.writeZeroes(this.out, 2);
    }

    @Override
    public void fixMetadata(final int recordQty) throws IOException {
        this.out.flush();
        this.file.seek(1);
        this.writeHeaderDate(new Date());
        BytesUtils.writeLEByte4(this.out, recordQty);
    }

    @Override
    public void close() throws IOException {
        this.out.flush();
        this.out.close();
    }

    private void writeHeaderDate(final Date updateDate) throws IOException {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(updateDate);
        this.out.write(calendar.get(Calendar.YEAR) - 1900);
        this.out.write(calendar.get(Calendar.MONTH) + 1);
        this.out.write(calendar.get(Calendar.DAY_OF_MONTH));
    }
}
