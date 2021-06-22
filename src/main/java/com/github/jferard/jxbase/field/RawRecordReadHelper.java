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

package com.github.jferard.jxbase.field;

import com.github.jferard.jxbase.util.BytesUtils;

import java.nio.charset.Charset;

/**
 * A helper class
 */
public class RawRecordReadHelper {
    private final Charset charset;

    public RawRecordReadHelper(final Charset charset) {
        this.charset = charset;
    }

    /**
     * Read a string
     * @param recordBuffer the source buffer
     * @param offset the offset in the buffer
     * @param length the length
     * @return a trimmed string, null if the string is empty.
     */
    public String extractTrimmedString(final byte[] recordBuffer, final int offset, final int length) {
        return BytesUtils.extractTrimmedString(recordBuffer, offset, length, this.charset);
    }

}
