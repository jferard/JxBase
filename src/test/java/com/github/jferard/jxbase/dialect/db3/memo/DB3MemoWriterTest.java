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

import com.github.jferard.jxbase.dialect.db3.memo.DB3MemoWriter;
import com.github.jferard.jxbase.dialect.foxpro.memo.TextMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.Collections;

public class DB3MemoWriterTest {
    @Test
    public void test() throws IOException {
        final SeekableByteChannel channel = Mockito.mock(SeekableByteChannel.class);
        final byte[] h = new byte[512];
        h[16] = 3;
        Mockito.when(channel.write(ByteBuffer.wrap(h))).thenReturn(512);
        Mockito.when(channel.write(ByteBuffer.wrap("abcde".getBytes(JxBaseUtils.ASCII_CHARSET))))
                .thenReturn(5);

        final XBaseMemoWriter writer =
                new DB3MemoWriter(channel, Collections.<String, Object>emptyMap());
        writer.write(new TextMemoRecord("abcde", JxBaseUtils.ASCII_CHARSET));

        Mockito.verify(channel, Mockito.times(2)).write(Mockito.isA(ByteBuffer.class));
    }

}