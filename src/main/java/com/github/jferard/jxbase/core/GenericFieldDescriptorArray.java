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

package com.github.jferard.jxbase.core;

import com.github.jferard.jxbase.field.XBaseField;

import java.util.Collection;

/**
 * The descriptor array.
 * @param <A> the access
 */
public class GenericFieldDescriptorArray<A extends XBaseAccess>
        implements XBaseFieldDescriptorArray<A> {
    private final Collection<XBaseField<? super A>> fields;
    private final int arrayLength;
    private final int recordLength;

    public GenericFieldDescriptorArray(final Collection<XBaseField<? super A>> fields,
                                       final int arrayLength, final int recordLength) {
        this.fields = fields;
        this.arrayLength = arrayLength;
        this.recordLength = recordLength;
    }

    @Override
    public Collection<XBaseField<? super A>> getFields() {
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
