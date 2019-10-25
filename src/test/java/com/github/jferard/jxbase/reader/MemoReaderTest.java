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

package com.github.jferard.jxbase.reader;

import com.github.jferard.jxbase.core.MemoRecord;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class MemoReaderTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testRead() throws IOException {
        FileInputStream fis = Mockito.mock(FileInputStream.class);
        FileChannel fc = Mockito.mock(FileChannel.class);
        MappedByteBuffer bb = Mockito.mock(MappedByteBuffer.class);

        Mockito.when(fis.getChannel()).thenReturn(fc);
        Mockito.when(fc.size()).thenReturn(100L);
        Mockito.when(fc.map(FileChannel.MapMode.READ_ONLY, 0, 100L)).thenReturn(bb);

        final MemoReader memoReader = new MemoReader(fis);
        MemoRecord mrec = memoReader.read(10);
        Assert.assertEquals(0, mrec.getBlockSize());
        Assert.assertArrayEquals(new byte[]{}, mrec.getValue());
        Assert.assertEquals(0, mrec.getLength());
        Assert.assertEquals("", mrec.getValueAsString(Charset.defaultCharset()));
        Assert.assertEquals(10, mrec.getOffsetInBlocks());
    }

    @Test
    public void testBadFile() throws IOException {
        FileInputStream fis = Mockito.mock(FileInputStream.class);
        FileChannel fc = Mockito.mock(FileChannel.class);
        MappedByteBuffer bb = Mockito.mock(MappedByteBuffer.class);

        Mockito.when(fis.getChannel()).thenReturn(fc);
        Mockito.when(fc.size()).thenReturn(100L);
        Mockito.when(fc.map(FileChannel.MapMode.READ_ONLY, 0, 100L)).thenReturn(bb);
        Mockito.when(bb.get((byte[]) Mockito.anyObject()))
                .thenThrow(new BufferUnderflowException());

        exception.expect(IOException.class);
        exception.expectMessage("The file is corrupted or is not a dbf file");
        final MemoReader memoReader = new MemoReader(fis);
    }

    @Test
    public void testOther() throws IOException {
        FileInputStream fis = Mockito.mock(FileInputStream.class);
        FileChannel fc = Mockito.mock(FileChannel.class);
        MappedByteBuffer bb = Mockito.mock(MappedByteBuffer.class);

        Mockito.when(fis.getChannel()).thenReturn(fc);
        Mockito.when(fc.size()).thenReturn(100L);
        Mockito.when(fc.map(FileChannel.MapMode.READ_ONLY, 0, 100L)).thenReturn(bb);

        final MemoReader memoReader = new MemoReader(fis);
        Assert.assertEquals("MemoFileHeader{nextFreeBlockLocation=0, blockSize=0}",
                memoReader.getMemoHeader().toString());
        memoReader.close();
    }
}