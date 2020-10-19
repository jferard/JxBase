/*
 * JxBase - Copyright (c) 2019-2020 Julien Férard
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

import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class DbfReaderTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /*
    @Test
    public void testEmptyStream() throws IOException {
        final InputStream dbf = new ByteArrayInputStream(new byte[]{});
        this.exception.expect(IOException.class);
        this.exception.expectMessage("The file is corrupted or is not a dbf file");
        final XBaseReader reader = new GenericReader(dbf, null);
        reader.close();
    }

    @Test
    public void testOneByteStreamWithGoodFileType() throws IOException {
        final InputStream dbf = new ByteArrayInputStream(new byte[]{0x02});
        this.exception.expect(IOException.class);
        this.exception.expectMessage("The file is corrupted or is not a dbf file");
        final XBaseReader reader = new GenericReader(dbf, null);
        reader.close();
    }

    @Test
    public void testOneByteStreamWithBadFileType() throws IOException {
        final InputStream dbf = new ByteArrayInputStream(new byte[]{0x02});
        this.exception.expect(IOException.class);
        this.exception.expectMessage("The file is corrupted or is not a dbf file");
        final XBaseReader reader = new GenericReader(dbf, null);
        reader.close();
    }

    @Test
    public void testSixteenByteStreamWithGoodFileType() throws IOException {
        final InputStream dbf = new ByteArrayInputStream(
                new byte[]{0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2});
        this.exception.expect(IOException.class);
        this.exception.expectMessage("The file is corrupted or is not a dbf file");
        final XBaseReader reader = new GenericReader(dbf, null);
        reader.close();
    }

    @Test
    public void testThirtyTwoByteStreamWithGoodFileType() throws IOException {
        final InputStream dbf = new ByteArrayInputStream(
                new byte[]{0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2});
        this.exception.expect(IOException.class);
        this.exception.expectMessage("The file is corrupted or is not a dbf file");
        final XBaseReader reader = new GenericReader(dbf, null);
        reader.close();
    }

    @Test
    public void testSixtyFourByteStreamWithGoodFileType() throws IOException {
        final InputStream dbf = new ByteArrayInputStream(
                new byte[]{0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2});
        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage("Unknown field type: \u0002 (2)");
        new GenericReader(dbf, null);
    }

    @Test
    public void testSixtyFourByteStreamWithGoodFileTypeAndCloseHeader() throws IOException {
        final InputStream dbf = new ByteArrayInputStream(
                new byte[]{0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, (byte) 0x41, 0x0, 0x3, 0x0, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, JdbfUtils.HEADER_TERMINATOR});
        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage("Unknown field type: \u0002 (2)");
        new GenericReader(dbf, null);
    }

    @Test
    public void testMemoStream() throws IOException {
        final byte[] buf =
                {0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, (byte) 0x41, 0x0, 0x2, 0x0, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        JdbfUtils.HEADER_TERMINATOR, 'a', 'b', 'c', 'd', 0, 0, 0, 0, 0, 0, 0,
                        0, 'L', 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        JdbfUtils.HEADER_TERMINATOR};
        Assert.assertEquals(65, buf.length);
        final InputStream dbf = new ByteArrayInputStream(buf);

        final FileChannel fc = PowerMock.createMock(FileChannel.class);
        final MappedByteBuffer bb = PowerMock.createMock(MappedByteBuffer.class);

        EasyMock.expect(fc.size()).andReturn(100L);
        EasyMock.expect(fc.map(FileChannel.MapMode.READ_ONLY, 0, 100L)).andReturn(bb);

        final DbfMemoRecordFactory dbfMemoRecordFactory = null;
        final XBaseReader reader = new GenericReader(dbf, new GenericMemoReader(fc, dbfMemoRecordFactory));
        Assert.assertEquals(
                "DbfMetadata[type=FoxBASE1, updateDate=2002-02-02, recordsQty=33686018, " +
                        "fullHeaderLength=65, oneRecordLength=2, uncompletedTxFlag=2, " +
                        "encryptionFlag=2, fields=OffsetDbfField[field=bcd,L,1,0, offset=1]]",
                reader.getMetadata().toString());
        reader.close();
    }

     */
}
