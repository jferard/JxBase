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

package com.github.jferard.jxbase.writer.internal;

public class GenericMetadataWriterTest {
    /*
    private RandomAccessFile raf;
    private GenericMetadataWriter gmw;
    private ByteArrayOutputStream out;

    @Before
    public void setUp() {
        this.raf = PowerMock.createMock(RandomAccessFile.class);
        this.out = new ByteArrayOutputStream();
        this.gmw =
                new GenericMetadataWriter(new DB3MemoDialect(XBaseFileTypeEnum.dBASE4SQLTable),
                this.raf,
                        this.out, JxBaseUtils.ASCII_CHARSET);
    }

    @Test
    public void write() throws IOException {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("updateDate", new Date(0));
        meta.put("dummy", "dummy");

        this.gmw.write(new GenericMetadata(XBaseFileTypeEnum.dBASE4SQLTable.toByte(), 200, 150,
        meta));
        Assert.assertArrayEquals(
                new byte[]{67, 70, 1, 1, 0, 0, 0, 0, -56, 0, -106, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, this.out.toByteArray());
    }

    @Test
    public void correctMetadata() throws IOException {
        this.gmw.fixMetadata(12);
        final byte[] bytes = this.out.toByteArray();
        Assert.assertEquals(7, bytes.length);
        Assert.assertEquals(12, bytes[3]);
        Assert.assertEquals(0, bytes[4]);
        Assert.assertEquals(0, bytes[5]);
        Assert.assertEquals(0, bytes[6]);

        Mockito.verify(this.raf).seek(1);
    }

    @Test
    public void close() {
    }

     */
}