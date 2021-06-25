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

package com.github.jferard.jxbase.memo;

import java.util.Map;

/**
 * Header of a memo file.
 * See https://www.clicketyclick.dk/databases/xbase/format/dbt.html
 */
public class MemoFileHeader {
    private final int nextFreeBlockLocation;
    private final int blockLength;
    private final Map<String, Object> meta;

    public MemoFileHeader(final int blockLength, final int nextFreeBlockLocation,
                          final Map<String, Object> meta) {
        this.nextFreeBlockLocation = nextFreeBlockLocation;
        this.blockLength = blockLength;
        this.meta = meta;
    }

    /**
     * @return the next free block location
     */
    public int getNextFreeBlockLocation() {
        return this.nextFreeBlockLocation;
    }

    /**
     * @return the block length
     */
    public int getBlockLength() {
        return this.blockLength;
    }

    /**
     * @param key key of a meta data
     * @return the value of the meta data
     */
    public Object get(final String key) {
        return this.meta.get(key);
    }

    @Override
    public String toString() {
        return "MemoFileHeader[blockLength=" + this.blockLength + ", nextFreeBlockLocation=" +
                this.nextFreeBlockLocation + ", meta=" + this.meta + ']';
    }
}
