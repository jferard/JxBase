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

package com.github.jferard.jxbase.memo;

import java.io.Closeable;
import java.io.IOException;

/**
 * A writer for a memo file
 */
public interface XBaseMemoWriter extends Closeable {
    /**
     * @param memo the memo record
     * @return the new offset in blocks
     * @throws IOException
     */
    long write(XBaseMemoRecord memo) throws IOException;

    /**
     * Write the last offset in blocks.
     * @throws IOException
     */
    void fixMetadata() throws IOException;
}
