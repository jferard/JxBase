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

package com.github.jferard.jxbase.field;

import com.github.jferard.jxbase.util.BytesUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * A helper class
 */
public class RawRecordWriteHelper {
    final Charset charset;

    public RawRecordWriteHelper(final Charset charset) {
        this.charset = charset;
    }

    /**
     * Write spaces
     * @param out the output stream
     * @param length the number of empties (0x20) to write
     * @throws IOException
     */
    public void writeEmpties(final OutputStream out, final int length) throws IOException {
        BytesUtils.writeEmpties(out, length);
    }

    /**
     * Write a value (padded with empties or cut at length)
     * @param out the output stream
     * @param value the string
     * @param length the min & max length
     * @throws IOException
     */
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

    /**
     * Write some bytes
     * @param out the output stream
     * @param bytes the bytes
     * @throws IOException
     */
    public void write(final OutputStream out, final byte[] bytes) throws IOException {
        out.write(bytes);
    }
}
