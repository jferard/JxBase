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

package com.github.jferard.jxbase.dialect.db4memo;

import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

/**
 * See http://msdn.microsoft.com/en-US/library/8599s21w(v=vs.80).aspx
 */
public class DB4MemoFileHeader {
    /**
     * @param headerBytes the source bytes
     * @return the memo header
     */
    public static DB4MemoFileHeader create(final byte[] headerBytes) {
        final int nextFreeBlockLocation =
                BitUtils.makeInt(headerBytes[3], headerBytes[2], headerBytes[1], headerBytes[0]);
        final int blockSize =
                BitUtils.makeInt(headerBytes[7], headerBytes[6], headerBytes[5], headerBytes[4]);
        final String dbfName = new String(headerBytes, 8, 8, JxBaseUtils.ASCII_CHARSET);
        assert headerBytes[16] == 0x00;
        final int blockLength = BitUtils.makeInt(headerBytes[21], headerBytes[20]);

        return new DB4MemoFileHeader(nextFreeBlockLocation, blockSize, dbfName, blockLength);
    }

    private final int nextFreeBlockLocation;
    private final int blockSize;
    private final String dbfName;
    private final int blockLength;

    public DB4MemoFileHeader(final int nextFreeBlockLocation, final int blockSize,
                             final String dbfName, final int blockLength) {
        this.nextFreeBlockLocation = nextFreeBlockLocation;
        this.blockSize = blockSize;
        this.dbfName = dbfName;
        this.blockLength = blockLength;
    }

    public int getNextFreeBlockLocation() {
        return this.nextFreeBlockLocation;
    }

    public int getBlockSize() {
        return this.blockSize;
    }

    public String getDbfName() {
        return this.dbfName;
    }

    public int getBlockLength() {
        return this.blockLength;
    }

    @Override
    public String toString() {
        return "MemoFileHeader[" + "nextFreeBlockLocation=" + this.nextFreeBlockLocation +
                ", blockSize=" + this.blockSize + ", dbfName=" + this.dbfName + ", blockLength=" +
                this.blockLength + ']';
    }
}
