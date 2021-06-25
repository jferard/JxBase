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

package com.github.jferard.jxbase.dialect.db3.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Access (read/write) to a memo field.
 */
public interface MemoAccess {
    /**
     * @return the actual length of the field (*in the DBF file*)
     */
    int getMemoValueLength();

    /**
     * Get the memo value.
     *
     * @param memoReader the memo reader
     * @param recordBuffer the record buffer
     * @param offset the offset
     * @param length the actual length
     * @return the memo record
     * @throws IOException
     */
    XBaseMemoRecord extractMemoValue(XBaseMemoReader memoReader,
                                     byte[] recordBuffer, int offset, int length) throws IOException;

    /**
     * Write the memo address.
     * @param out   the output stream
     * @param offsetInBlocks the memo value
     * @throws IOException
     */
    void writeMemoAddress(OutputStream out,
                        long offsetInBlocks) throws IOException;

    /**
     * Write the memo value.
     * @param memoWriter the memo writer
     * @param value the memo value
     * @return the offset in blocks or -1
     * @throws IOException
     * @return
     */
    long writeMemoValue(XBaseMemoWriter memoWriter,
                        XBaseMemoRecord value) throws IOException;

    /**
     * @param fieldName the field name
     * @return the field representation.
     */
    FieldRepresentation getMemoFieldRepresentation(String fieldName);
}
