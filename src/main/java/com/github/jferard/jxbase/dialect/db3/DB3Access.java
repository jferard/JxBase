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

import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.LogicalAccess;
import com.github.jferard.jxbase.dialect.db2.field.NumericAccess;
import com.github.jferard.jxbase.dialect.db3.field.DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class DB3Access extends DB2Access implements CDLMNFieldsAccess {
    private final DateAccess dateAccess;
    private final MemoAccess memoAccess;

    public DB3Access(final CharacterAccess characterAccess, final DateAccess dateAccess,
                     final LogicalAccess logicalAccess, final MemoAccess memoAccess,
                     final NumericAccess numericAccess) {
        super(characterAccess, logicalAccess, numericAccess);
        this.dateAccess = dateAccess;
        this.memoAccess = memoAccess;
    }

    @Override
    public int getDateValueLength() {
        return this.dateAccess.getDateValueLength();
    }

    @Override
    public Date getDateValue(final byte[] recordBuffer, final int offset, final int length) {
        return this.dateAccess.getDateValue(recordBuffer, offset, length);
    }

    @Override
    public void writeDateValue(final OutputStream out, final Date value) throws IOException {
        this.dateAccess.writeDateValue(out, value);
    }

    @Override
    public FieldRepresentation getDateFieldRepresentation(final String name) {
        return this.dateAccess.getDateFieldRepresentation(name);
    }

    @Override
    public int getMemoValueLength() {
        return this.memoAccess.getMemoValueLength();
    }

    @Override
    public XBaseMemoRecord getMemoValue(final byte[] recordBuffer, final int offset,
                                        final int length) throws IOException {
        return this.memoAccess.getMemoValue(recordBuffer, offset, length);
    }

    @Override
    public void writeMemoValue(final OutputStream out, final XBaseMemoRecord value)
            throws IOException {
        this.memoAccess.writeMemoValue(out, value);
    }

    @Override
    public FieldRepresentation getMemoFieldRepresentation(final String name) {
        return this.memoAccess.getMemoFieldRepresentation(name);
    }
}
