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

package com.github.jferard.jxbase.dialect.db3.memo;

import com.github.jferard.jxbase.memo.MemoFileHeader;
import com.github.jferard.jxbase.memo.RawMemoReader;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class DB3MemoReaderTest {
    private DB3MemoReader reader;
    private MemoFileHeader header;
    private RawMemoReader rawMemoReader;

    @Before
    public void setUp() {
        this.header = PowerMock.createMock(MemoFileHeader.class);
        this.rawMemoReader = PowerMock.createMock(RawMemoReader.class);
        this.reader = new DB3MemoReader(this.header, this.rawMemoReader);
    }

    @Test
    public void testCreate() throws IOException {
        final FileChannel channel = PowerMock.createMock(FileChannel.class);
        final MappedByteBuffer buffer = PowerMock.createMock(MappedByteBuffer.class);
        final ByteBuffer bb = ByteBuffer.allocate(100);
        PowerMock.resetAll();

        EasyMock.expect(channel.size()).andReturn(10L);
        EasyMock.expect(channel.map(FileChannel.MapMode.READ_ONLY, 0, 10)).andReturn(buffer);
        EasyMock.expect(buffer.get(EasyMock.isA(byte[].class))).andReturn(bb);
        PowerMock.replayAll();

        DB3MemoReader.create(channel);
        PowerMock.verifyAll();

    }

    @Test
    public void testHeader() {
        Assert.assertEquals(this.header, this.reader.getHeader());
    }

    @Test
    public void testRead() {
        PowerMock.resetAll();

        EasyMock.expect(this.rawMemoReader.read(8, 0, 512))
                .andReturn(new byte[]{1, 2, 3, JxBaseUtils.RECORDS_TERMINATOR}).times(2);
        PowerMock.replayAll();

        final int length = this.reader.read(8).getLength();
        final byte[] bytes = this.reader.read(8).getBytes();
        PowerMock.verifyAll();

        Assert.assertEquals(3, length);
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, bytes);
    }

    @Test
    public void testReadBufferUnderflow() {
        PowerMock.resetAll();

        EasyMock.expect(this.rawMemoReader.read(8, 0, 512))
                .andThrow(new BufferUnderflowException()).times(2);
        PowerMock.replayAll();

        final int length = this.reader.read(8).getLength();
        final byte[] bytes = this.reader.read(8).getBytes();
        PowerMock.verifyAll();

        Assert.assertEquals(0, length);
        Assert.assertArrayEquals(new byte[]{}, bytes);
    }
}