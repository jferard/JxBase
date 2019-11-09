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

package com.github.jferard.jxbase.writer.internal;

import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;

import java.io.IOException;

public interface XBaseFieldDescriptorArrayWriter {
    int write(XBaseFieldDescriptorArray array) throws IOException;

    void writeCharacterField(String name, int length, int offset) throws IOException;

    void writeDateField(String name, int offset) throws IOException;

    void writeDatetimeField(String name, int offset) throws IOException;

    void writeIntegerField(String name, int offset) throws IOException;

    void writeLogicalField(String name, int offset) throws IOException;

    void writeMemoField(String name, int offset) throws IOException;

    void writeNumericField(String name, int length, int numberOfDecimalPlaces, int offset) throws IOException;

    void writeNullFlagsField(String name, int length, int offset) throws IOException;

    void writeSmallMemoField(String memo, int offset) throws IOException;
}
