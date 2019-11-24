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

package com.github.jferard.jxbase.field;

import com.github.jferard.jxbase.util.BitUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class RawRecordWriter {
    final Charset charset;

    public RawRecordWriter(final Charset charset) {
        this.charset = charset;
    }

    public void writeEmpties(final OutputStream out, final int length) throws IOException {
        BitUtils.writeEmpties(out, length);
    }

    public void write(final OutputStream out, final String value, final int length)
            throws IOException {
        final byte[] bytes = value.getBytes(this.charset);
        if (bytes.length >= length) {
            for (int i = 0; i < length; i++) {
                out.write(bytes[i]);
            }
        } else {
            out.write(bytes);
            this.writeEmpties(out, length - bytes.length);
        }

    }

    public void write(final OutputStream out, final byte[] bytes) throws IOException {
        out.write(bytes);
    }
}
