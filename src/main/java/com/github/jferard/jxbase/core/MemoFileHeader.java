/*
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

import com.github.jferard.jxbase.util.BitUtils;

/**
 * See http://msdn.microsoft.com/en-US/library/8599s21w(v=vs.80).aspx
 */
public class MemoFileHeader {
    private byte[] headerBytes;
    private int nextFreeBlockLocation;
    private int blockSize;

    public static MemoFileHeader create(byte[] headerBytes) {
        MemoFileHeader h = new MemoFileHeader();
        h.setHeaderBytes(headerBytes);
        h.calculateHeaderFields();
        return h;
    }

    private void calculateHeaderFields() {
        this.nextFreeBlockLocation = BitUtils.makeInt(headerBytes[3],headerBytes[2],headerBytes[1],headerBytes[0]);
        this.blockSize = BitUtils.makeInt(headerBytes[7],headerBytes[6]);
    }

    private void setHeaderBytes(byte[] headerBytes) {
        this.headerBytes = headerBytes;
    }

    public int getNextFreeBlockLocation() {
        return nextFreeBlockLocation;
    }

    public int getBlockSize() {
        return blockSize;
    }

    @Override
    public String toString() {
        return "MemoFileHeader{" +
                "nextFreeBlockLocation=" + nextFreeBlockLocation +
                ", blockSize=" + blockSize +
                '}';
    }
}