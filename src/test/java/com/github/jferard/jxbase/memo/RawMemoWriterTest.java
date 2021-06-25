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

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class RawMemoWriterTest {
    @Test(expected = AssertionError.class)
    public void testError() throws IOException {
        final SeekableByteChannel channel = PowerMock.createMock(SeekableByteChannel.class);
        PowerMock.resetAll();

        EasyMock.expect(channel.position(16 + 32 + 3L)).andReturn(channel);
        EasyMock.expect(channel.write(ByteBuffer.wrap(new byte[] {1, 2, 3}))).andReturn(2);
        PowerMock.replayAll();

        final RawMemoWriter writer = new RawMemoWriter(channel, 16, 32);
        writer.write(2, 3, new byte[] {1, 2, 3}, new byte[] {4, 5, 6});
        PowerMock.verifyAll();
    }

    @Test
    public void test() throws IOException {
        final SeekableByteChannel channel = PowerMock.createMock(SeekableByteChannel.class);
        PowerMock.resetAll();

        EasyMock.expect(channel.position(16 + 32 + 3L)).andReturn(channel);
        EasyMock.expect(channel.write(ByteBuffer.wrap(new byte[] {1, 2, 3}))).andReturn(3);
        EasyMock.expect(channel.write(ByteBuffer.wrap(new byte[] {4, 5, 6}))).andReturn(3);
        PowerMock.replayAll();

        final RawMemoWriter writer = new RawMemoWriter(channel, 16, 32);
        writer.write(2, 3, new byte[] {1, 2, 3}, new byte[] {4, 5, 6});
        PowerMock.verifyAll();
    }
}