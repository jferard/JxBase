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

package com.github.jferard.jxbase.dialect.db4.reader;

import com.github.jferard.jxbase.dialect.db3.reader.DB3MemoFileHeaderReader;
import com.github.jferard.jxbase.memo.MemoFileHeader;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * See http://msdn.microsoft.com/en-US/library/8599s21w(v=vs.80).aspx
 */
public class DB4MemoFileHeaderReader implements MemoFileHeaderReader {
    @Override
    public MemoFileHeader read(final ByteBuffer memoByteBuffer) {
        final byte[] headerBytes = new byte[DB3MemoFileHeaderReader.MEMO_HEADER_LENGTH];
        memoByteBuffer.get(headerBytes);
        final int nextFreeBlockLocation =
                BitUtils.makeInt(headerBytes[3], headerBytes[2], headerBytes[1], headerBytes[0]);
        final int blockLength =
                BitUtils.makeInt(headerBytes[7], headerBytes[6], headerBytes[5], headerBytes[4]);
        final String dbfName = new String(headerBytes, 8, 8, JxBaseUtils.ASCII_CHARSET);
        assert headerBytes[16] == 0x00;
        // final int Length = BitUtils.makeInt(headerBytes[21], headerBytes[20]);

        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("dbfName", dbfName);
        return new MemoFileHeader(blockLength, nextFreeBlockLocation, meta);
    }
}
