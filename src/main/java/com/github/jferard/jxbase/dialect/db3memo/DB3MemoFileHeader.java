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

package com.github.jferard.jxbase.dialect.db3memo;

import com.github.jferard.jxbase.util.BitUtils;

/**
 * Should be a generic object
 * See http://msdn.microsoft.com/en-US/library/8599s21w(v=vs.80).aspx
 */
public class DB3MemoFileHeader {
    static final int SIZE = 512;

    /**
     * @param headerBytes the source bytes
     * @return the memo header
     */
    public static DB3MemoFileHeader create(final byte[] headerBytes) {
        final int nextFreeBlockLocation =
                BitUtils.makeInt(headerBytes[3], headerBytes[2], headerBytes[1], headerBytes[0]);
        // assert headerBytes[16] == 0x03;
        return new DB3MemoFileHeader(nextFreeBlockLocation);
    }

    private final int nextFreeBlockLocation;

    public DB3MemoFileHeader(final int nextFreeBlockLocation) {
        this.nextFreeBlockLocation = nextFreeBlockLocation;
    }

    public int getNextFreeBlockLocation() {
        return this.nextFreeBlockLocation;
    }

    @Override
    public String toString() {
        return "DB3MemoFileHeader[" + "nextFreeBlockLocation=" + this.nextFreeBlockLocation + "]";
    }
}
