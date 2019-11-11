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

package com.github.jferard.jxbase.reader;

import com.github.jferard.jxbase.memo.MemoRecordFactory;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MemoReaderTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private final MemoRecordFactory dbfMemoRecordFactory = null;

    @Test
    public void testRead() throws IOException {
        final FileChannel fc = Mockito.mock(FileChannel.class);
        final MappedByteBuffer bb = Mockito.mock(MappedByteBuffer.class);

        Mockito.when(fc.size()).thenReturn(100L);
        Mockito.when(fc.map(FileChannel.MapMode.READ_ONLY, 0, 100L)).thenReturn(bb);
        final ArgumentCaptor<byte[]> argument = ArgumentCaptor.forClass(byte[].class);
        Mockito.when(bb.get(argument.capture())).then(new Answer<Void>() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                System.arraycopy(argument.getValue(), 0, new byte[] {1,2,3,4,5,6,7,8}, 0, 8);
                return null;
            }
        });

        final GenericMemoReader memoReader = new GenericMemoReader(fc, this.dbfMemoRecordFactory);
        try {
            final XBaseMemoRecord mrec = memoReader.read(10);
            Assert.assertArrayEquals(new byte[]{}, mrec.getBytes());
            Assert.assertEquals(0, mrec.getLength());
            Assert.assertEquals("", mrec.getValue());
            Assert.assertEquals(10, mrec.getOffsetInBlocks());
        } catch (final Exception e) {}
    }

    @Test
    public void testBadFile() throws IOException {
        final FileChannel fc = Mockito.mock(FileChannel.class);
        final MappedByteBuffer bb = Mockito.mock(MappedByteBuffer.class);

        Mockito.when(fc.size()).thenReturn(100L);
        Mockito.when(fc.map(FileChannel.MapMode.READ_ONLY, 0, 100L)).thenReturn(bb);
        Mockito.when(bb.get((byte[]) Mockito.anyObject()))
                .thenThrow(new BufferUnderflowException());

        this.exception.expect(IOException.class);
        this.exception.expectMessage("The file is corrupted or is not a dbf file");
        final GenericMemoReader memoReader = new GenericMemoReader(fc, this.dbfMemoRecordFactory);
    }

    @Test
    public void testOther() throws IOException {
        final FileChannel fc = Mockito.mock(FileChannel.class);
        final MappedByteBuffer bb = Mockito.mock(MappedByteBuffer.class);

        Mockito.when(fc.size()).thenReturn(100L);
        Mockito.when(fc.map(FileChannel.MapMode.READ_ONLY, 0, 100L)).thenReturn(bb);

        final GenericMemoReader memoReader = new GenericMemoReader(fc, this.dbfMemoRecordFactory);
        /*
        Assert.assertEquals("MemoFileHeader{nextFreeBlockLocation=0, blockSize=0}",
                memoReader.getMemoHeader().toString());

         */
        memoReader.close();
    }
}