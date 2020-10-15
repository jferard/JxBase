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
import com.github.jferard.jxbase.memo.XBaseMemoRecord;

import java.io.IOException;
import java.io.OutputStream;

public interface MemoAccess {
    int getMemoValueLength();

    XBaseMemoRecord getMemoValue(byte[] recordBuffer, int offset, int length) throws IOException;

    void writeMemoValue(OutputStream out, XBaseMemoRecord value) throws IOException;

    FieldRepresentation getMemoFieldRepresentation(String name);
}
