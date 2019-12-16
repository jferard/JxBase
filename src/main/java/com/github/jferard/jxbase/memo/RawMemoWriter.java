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

package com.github.jferard.jxbase.memo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;

public class RawMemoWriter {
    private final SeekableByteChannel channel;
    private final int blockSize;
    private final int headerSize;

    public RawMemoWriter(final SeekableByteChannel channel, final int blockSize,
                         final int headerSize) {
        this.channel = channel;
        this.blockSize = blockSize;
        this.headerSize = headerSize;
    }

    public long write(final long offsetInBlocks, final int from, final byte[]... arrayOfBytes)
            throws IOException {
        final long start = this.blockSize * (offsetInBlocks - 1) + this.headerSize + from;
        this.channel.position(start);
        int len = 0;
        for (final byte[] bytes : arrayOfBytes) {
            final int n = this.channel.write(ByteBuffer.wrap(bytes));
            // Javadoc: "Unless otherwise specified, a write operation will return only after
            // writing all of the r requested bytes"
            if (n != bytes.length) {
                throw new AssertionError("Expected to write " + bytes.length + " bytes, but only " + n + " were written");
            }
            len += bytes.length;
        }
        return offsetInBlocks + from + len / this.blockSize;
    }

    public void close() throws IOException {
        this.channel.close();
    }
}
