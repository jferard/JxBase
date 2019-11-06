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

import com.github.jferard.jxbase.util.BitUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DBaseVIIWriter extends GenericWriter {
    private OutputStream out;

    public DBaseVIIWriter(final OutputStream out, final Charset charset) {
        super(null, null, null);
    }

    public void writeDoubleVIIIValue(final Double d) throws IOException {
        final int length = 8;
        if (d == null) {
            BitUtils.writeZeroes(this.out, length);
        } else {
            final ByteBuffer bb = ByteBuffer.allocate(length);
            bb.putDouble(d);
            this.out.write(bb.array());
        }
    }
}
