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

package com.github.jferard.jxbase.writer;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.util.BitUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

public class GenericMetadataWriter<D extends XBaseDialect<D, A>, A> implements XBaseMetadataWriter<D, A> {
    final OutputStream out;
    final Charset charset;
    private final RandomAccessFile file;
    private final D dialect;

    public GenericMetadataWriter(final D dialect, final RandomAccessFile file,
                                 final OutputStream out, final Charset charset) {
        this.dialect = dialect;
        this.file = file;
        this.out = out;
        this.charset = charset;
    }

    @Override
    public void write(final XBaseMetadata metadata) throws IOException {
        this.out.write(metadata.getFileTypeByte());
        final Object d = metadata.get("updateDate");
        if (d instanceof Date) {
            final Date updateDate = (Date) d;
            this.writeHeaderDate(updateDate);
        } else {
            BitUtils.writeZeroes(this.out, 3);
        }
        final Object r = metadata.get("recordsQty");
        if (r instanceof Number) {
            final int recordsQty = ((Number) r).intValue();
            BitUtils.writeLEByte4(this.out, recordsQty);
        } else {
            BitUtils.writeZeroes(this.out, 4);
        }
        BitUtils.writeLEByte2(this.out, metadata.getFullHeaderLength());
        BitUtils.writeLEByte2(this.out, metadata.getOneRecordLength());
        BitUtils.writeZeroes(this.out, 2);
        final Object u = metadata.get("uncompletedTxFlag");
        if (u instanceof Number) {
            this.out.write(((Number) u).byteValue());
        } else {
            this.out.write(0);
        }
        final Object e = metadata.get("encryptionFlag");
        if (e instanceof Number) {
            this.out.write(((Number) e).byteValue());
        } else {
            this.out.write(0);
        }
        BitUtils.writeZeroes(this.out, this.dialect.getMetaDataLength() - 16);
    }

    @Override
    public void fixMetadata(final int recordQty) throws IOException {
        this.out.flush();
        this.file.seek(1);
        this.writeHeaderDate(new Date());
        BitUtils.writeLEByte4(this.out, recordQty);
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
