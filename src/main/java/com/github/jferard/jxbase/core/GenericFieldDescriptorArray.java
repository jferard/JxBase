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

package com.github.jferard.jxbase.core;

import com.github.jferard.jxbase.core.field.XBaseField;

import java.util.Collection;

public class GenericFieldDescriptorArray implements XBaseFieldDescriptorArray {
    private final Collection<XBaseField> fields;
    private final int arrayLength;
    private final int recordLength;

    public GenericFieldDescriptorArray(final Collection<XBaseField> fields, final int arrayLength,
                                       final int recordLength) {
        this.fields = fields;
        this.arrayLength = arrayLength;
        this.recordLength = recordLength;
    }

    @Override
    public Collection<XBaseField> getFields() {
        return this.fields;
    }

    @Override
    public int getArrayLength() {
        return this.arrayLength;
    }

    @Override
    public int getRecordLength() {
        return this.recordLength;
    }

    @Override
    public String toString() {
        return "GenericFieldDescriptorArray[fields=" + this.fields + "]";
    }
}
