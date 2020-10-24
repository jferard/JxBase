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

package com.github.jferard.jxbase.dialect.foxpro.memo;

import com.github.jferard.jxbase.dialect.db4.reader.MemoFileHeaderReader;
import com.github.jferard.jxbase.memo.MemoFileHeader;
import com.github.jferard.jxbase.memo.MemoRecordType;
import com.github.jferard.jxbase.memo.RawMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileChannel.class, AbstractInterruptibleChannel.class})
public class FoxProMemoReaderTest {
    private FoxProMemoReader r;
    private RawMemoReader rawMemoReader;
    private FoxProMemoRecordFactory memoRecordFactory;

    @Before
    public void setUp() {
        final FileChannel channel = PowerMock.createMock(FileChannel.class);
        this.memoRecordFactory = new FoxProMemoRecordFactory(JxBaseUtils.ASCII_CHARSET);
        this.rawMemoReader = EasyMock.strictMock(RawMemoReader.class);
        this.r = new FoxProMemoReader(channel, this.memoRecordFactory, this.rawMemoReader);
    }

    @Test
    public void testOk() {
        final byte[] bytes = new byte[]{0, 0, 0, 1, 0, 0, 0, 1};
        PowerMock.resetAll();

        EasyMock.expect(this.rawMemoReader.read(0, 0, 8)).andReturn(bytes);
        EasyMock.expect(this.rawMemoReader.read(0, 8, 1)).andReturn(new byte[]{'a'});
        PowerMock.replayAll(this.rawMemoReader);

        final XBaseMemoRecord memoRecord = this.r.read(0);
        PowerMock.verifyAll();

        Assert.assertEquals(1, memoRecord.getLength());
        Assert.assertArrayEquals(new byte[]{'a'}, memoRecord.getBytes());
    }

    @Test
    public void testFail() {
        final byte[] bytes = new byte[]{0, 0, 0, 10, 0, 0, 0, 1};
        PowerMock.resetAll();

        EasyMock.expect(this.rawMemoReader.read(0, 0, 8)).andReturn(bytes);
        EasyMock.expect(this.rawMemoReader.read(0, 8, 1)).andReturn(new byte[]{'a'});
        PowerMock.replayAll(this.rawMemoReader);

        final XBaseMemoRecord memoRecord = this.r.read(0);
        PowerMock.verifyAll();
    }

    @Test
    public void testCreate() throws IOException, NoSuchMethodException {
        final byte[] bytes = new byte[]{0, 0, 0, 10, 0, 0, 0, 1};
        final ByteBuffer bbuffer = ByteBuffer.allocateDirect(8192);
        bbuffer.put(bytes);
        final TextMemoRecord memoRecord = new TextMemoRecord("foo", JxBaseUtils.ASCII_CHARSET);

        final FileChannel channel = PowerMock.createMock(FileChannel.class);
        final FoxProMemoRecordFactory memoRecordFactory =
                PowerMock.createMock(FoxProMemoRecordFactory.class);
        final MemoFileHeaderReader memoFileHeaderReader =
                PowerMock.createMock(MemoFileHeaderReader.class);
        final MemoFileHeader header = PowerMock.createMock(MemoFileHeader.class);
        PowerMock.resetAll();

        EasyMock.expect(channel.size()).andReturn(8192L);
        EasyMock.expect(channel.map(FileChannel.MapMode.READ_ONLY, 0L, 8192L))
                .andReturn((MappedByteBuffer) bbuffer);
        EasyMock.expect(memoFileHeaderReader.read(bbuffer)).andReturn(header);
        EasyMock.expect(header.getBlockLength()).andReturn(512);
        EasyMock.expect(memoRecordFactory.create(new byte[]{0}, MemoRecordType.NO_TYPE, 1))
                .andReturn(memoRecord);
        // channel.close();
        PowerMock.replayAll();

        final XBaseMemoReader reader =
                FoxProMemoReader.create(channel, memoRecordFactory, memoFileHeaderReader);
        final XBaseMemoRecord record = reader.read(0);
        // TODO: find a method to mock the final method of AbstractInterruptibleChannel
        // reader.close();
        PowerMock.verifyAll();

        Assert.assertSame(memoRecord, record);
    }
}