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

package com.github.jferard.jxbase.dialect.db3;

import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericAccess;
import com.github.jferard.jxbase.dialect.db3.field.DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Access (read/write) for DB3 fields.
 */
public class DB3Access extends DB2Access implements CDLMNFieldsAccess, XBaseAccess {
    private final DateAccess dateAccess;
    private final MemoAccess memoAccess;

    public DB3Access(final CharacterAccess characterAccess, final LogicalAccess logicalAccess,
                     final NumericAccess numericAccess, final DateAccess dateAccess,
                     final MemoAccess memoAccess) {
        super(characterAccess, logicalAccess, numericAccess);
        this.dateAccess = dateAccess;
        this.memoAccess = memoAccess;
    }

    @Override
    public int getDateValueLength() {
        return this.dateAccess.getDateValueLength();
    }

    @Override
    public Date extractDateValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.dateAccess.extractDateValue(recordBuffer, offset, length);
    }

    @Override
    public void writeDateValue(final OutputStream out, final Date value) throws IOException {
        this.dateAccess.writeDateValue(out, value);
    }

    @Override
    public FieldRepresentation getDateFieldRepresentation(final String fieldName) {
        return this.dateAccess.getDateFieldRepresentation(fieldName);
    }

    @Override
    public int getMemoValueLength() {
        return this.memoAccess.getMemoValueLength();
    }

    @Override
    public XBaseMemoRecord extractMemoValue(
            final XBaseMemoReader memoReader, final byte[] recordBuffer,
            final int offset,
            final int length) throws IOException {
        return this.memoAccess.extractMemoValue(memoReader, recordBuffer, offset, length);
    }

    @Override
    public void writeMemoAddress(final OutputStream out, final long offsetInBlocks) throws IOException {
        this.memoAccess.writeMemoAddress(out, offsetInBlocks);
    }

    @Override
    public long writeMemoValue(final XBaseMemoWriter memoWriter,
                               final XBaseMemoRecord value)
            throws IOException {
        return this.memoAccess.writeMemoValue(memoWriter, value);
    }

    @Override
    public FieldRepresentation getMemoFieldRepresentation(final String fieldName) {
        return this.memoAccess.getMemoFieldRepresentation(fieldName);
    }
}
