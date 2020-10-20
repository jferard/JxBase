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

package com.github.jferard.jxbase.dialect.db3.memo;

import com.github.jferard.jxbase.dialect.foxpro.memo.FoxProMemoWriter;
import com.github.jferard.jxbase.dialect.foxpro.memo.TextMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Collections;

public class DB3MemoWriterTest {
    @Test
    public void test() throws IOException {
        final SeekableByteChannel channel = PowerMock.createMock(SeekableByteChannel.class);
        final byte[] h = new byte[512];
        h[16] = 3;
        PowerMock.resetAll();

        EasyMock.expect(channel.write(ByteBuffer.wrap(h))).andReturn(512);
        EasyMock.expect(channel.write(ByteBuffer.wrap("abcde".getBytes(JxBaseUtils.ASCII_CHARSET))))
                .andReturn(5);
        EasyMock.expect(channel.position(0L)).andReturn(channel);
        EasyMock.expect(channel.position(512L)).andReturn(channel);
        PowerMock.replayAll();

        final XBaseMemoWriter writer =
                new DB3MemoWriter(channel, Collections.<String, Object>emptyMap());
        writer.write(new TextMemoRecord("abcde", JxBaseUtils.ASCII_CHARSET));
        PowerMock.verifyAll();
    }

    @Test
    public void testCloseAndFix() throws IOException {
        final SeekableByteChannel channel = PowerMock.createMock(SeekableByteChannel.class);
        final byte[] h = new byte[512];
        h[16] = 3;
        PowerMock.resetAll();

        EasyMock.expect(channel.position(0L)).andReturn(channel).times(2);
        EasyMock.expect(channel.write(ByteBuffer.wrap(h))).andReturn(512);
        EasyMock.expect(channel.write(ByteBuffer.wrap(new byte[]{1, 0, 0, 0}))).andReturn(4);
        channel.close();
        PowerMock.replayAll();

        final XBaseMemoWriter writer =
                new DB3MemoWriter(channel, Collections.<String, Object>emptyMap());
        writer.fixMetadata();
        writer.close();
        PowerMock.verifyAll();
    }
}