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

public class MemoReaderTest {
    /*
    private final FoxProMemoRecordFactory dbfMemoRecordFactory = null;
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testRead() throws IOException {
        final FileChannel fc = PowerMock.createMock(FileChannel.class);
        final MappedByteBuffer bb = PowerMock.createMock(MappedByteBuffer.class);

        EasyMock.expect(fc.size()).andReturn(100L);
        EasyMock.expect(fc.map(FileChannel.MapMode.READ_ONLY, 0, 100L)).andReturn(bb);
        final ArgumentCaptor<byte[]> argument = ArgumentCaptor.forClass(byte[].class);
        EasyMock.expect(bb.get(argument.capture())).then(new Answer<Void>() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                System.arraycopy(argument.getValue(), 0, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, 0, 8);
                return null;
            }
        });

        final DB4MemoReader memoReader = new DB4MemoReader(fc, this.dbfMemoRecordFactory);
        try {
            final XBaseMemoRecord mrec = memoReader.read(10);
            Assert.assertArrayEquals(new byte[]{}, mrec.getBytes());
            Assert.assertEquals(0, mrec.getLength());
            Assert.assertEquals("", mrec.getValue());
        } catch (final Exception e) {
        }
    }

    @Test
    public void testBadFile() throws IOException {
        final FileChannel fc = PowerMock.createMock(FileChannel.class);
        final MappedByteBuffer bb = PowerMock.createMock(MappedByteBuffer.class);

        EasyMock.expect(fc.size()).andReturn(100L);
        EasyMock.expect(fc.map(FileChannel.MapMode.READ_ONLY, 0, 100L)).andReturn(bb);
        EasyMock.expect(bb.get((byte[]) Mockito.anyObject()))
                .andThrow(new BufferUnderflowException());

        this.exception.expect(IOException.class);
        this.exception.expectMessage("The file is corrupted or is not a dbt file");
        final DB3MemoReader memoReader = new DB3MemoReader(fc);
    }

    @Test
    public void testOther() throws IOException {
        final FileChannel fc = PowerMock.createMock(FileChannel.class);
        final MappedByteBuffer bb = PowerMock.createMock(MappedByteBuffer.class);

        EasyMock.expect(fc.size()).andReturn(100L);
        EasyMock.expect(fc.map(FileChannel.MapMode.READ_ONLY, 0, 100L)).andReturn(bb);

        final DB3MemoReader memoReader = new DB3MemoReader(fc);
        /*
        Assert.assertEquals("MemoFileHeader{nextFreeBlockLocation=0, blockSize=0}",
                memoReader.getMemoHeader().toString());

         *//*
        memoReader.close();
    }
    */
}