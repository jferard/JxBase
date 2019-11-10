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

import com.github.jferard.jxbase.core.FoxProDialect;
import com.github.jferard.jxbase.core.XBaseDialect;

import java.io.IOException;
import java.io.OutputStream;

public class FoxProFieldDescriptorArrayWriter extends GenericFieldDescriptorArrayWriter {

    public FoxProFieldDescriptorArrayWriter(final XBaseDialect dialect, final OutputStream out) {
        super(dialect, out);
    }

    public void writeDatetimeField(final String name, final int offset) throws IOException {

    }

    public void writeSmallMemoField(final String name, final int offset) throws IOException {
        this.writeField(name, 'M', ((FoxProDialect) this.dialect).getSmallMemoFieldLength(), 0, offset);
    }

    public void writeNullFlagsField(final String name, final int length, final int offset)
            throws IOException {
        this.writeField(name, '0', ((FoxProDialect) this.dialect).getNullFlagsFieldLength(length), 0, offset);
    }

}
