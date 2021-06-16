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

package com.github.jferard.jxbase.dialect.db3.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * A memo field.
 */
public class MemoField implements XBaseField<MemoAccess> {
    private final String name;

    public MemoField(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getValueLength(final MemoAccess memoAccess) {
        return memoAccess.getMemoValueLength();
    }

    @Override
    public XBaseMemoRecord extractValue(final MemoAccess memoAccess, final byte[] recordBuffer,
                                        final int offset, final int length) throws IOException {
        throw new UnsupportedEncodingException();
//        final XBaseMemoReader memoReader = memoAccess.getMemoReader();
//        return memoAccess.extractMemoValue(memoReader, recordBuffer, offset, length);
    }

    @Override
    public void writeValue(final MemoAccess memoAccess, final OutputStream out, final Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toStringRepresentation(final MemoAccess memoAccess) {
        return this.toRepresentation(memoAccess).toString();
    }

    @Override
    public FieldRepresentation toRepresentation(final MemoAccess memoAccess) {
        return memoAccess.getMemoFieldRepresentation(this.name);
    }

    @Override
    public String toString() {
        return "MemoField[name=" + this.name + "]";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemoField)) {
            return false;
        }

        final MemoField that = (MemoField) o;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
