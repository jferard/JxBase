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

package com.github.jferard.jxbase.dialect.db4.memo;

import com.github.jferard.jxbase.dialect.foxpro.memo.FoxProMemoRecordFactory;
import com.github.jferard.jxbase.memo.RawMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.nio.channels.FileChannel;

public class DB4MemoReaderTest {
    private DB4MemoReader r;
    private RawMemoReader rawMemoReader;

    @Before
    public void setUp() {
        final FileChannel channel = PowerMock.createMock(FileChannel.class);
        this.rawMemoReader = EasyMock.strictMock(RawMemoReader.class);
        this.r = new DB4MemoReader(channel, this.rawMemoReader);
    }

    @Test
    public void testOk() {
        PowerMock.resetAll();
        final byte[] bytes = new byte[]{(byte) 0xff, (byte) 0xff, 0x08, 0x00, 1, 0, 0, 0};
        EasyMock.expect(this.rawMemoReader.read(0, 0, 8)).andReturn(bytes);
        EasyMock.expect(this.rawMemoReader.read(0, 8, 1)).andReturn(new byte[]{'a'});

        PowerMock.replayAll(this.rawMemoReader);
        final XBaseMemoRecord memoRecord = this.r.read(0);

        PowerMock.verifyAll();
        Assert.assertEquals(1, memoRecord.getLength());
        Assert.assertArrayEquals(new byte[]{'a'}, memoRecord.getBytes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFail() {
        PowerMock.resetAll();
        final byte[] bytes = new byte[]{0, 0, 0, 0, 0, 0, 0, 1};
        EasyMock.expect(this.rawMemoReader.read(0, 0, 8)).andReturn(bytes);

        PowerMock.replayAll(this.rawMemoReader);
        final XBaseMemoRecord memoRecord = this.r.read(0);

        PowerMock.verifyAll();
    }
}