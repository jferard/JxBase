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

package com.github.jferard.jxbase.writer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DbfWriterTest {
    /*
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private GenericFieldFactory dbfFieldFactory;

    @Before
    public void setUp() {
        this.dbfFieldFactory = new GenericFieldFactory();
    }

    @Test
    public void TestWriteCNFD() throws IOException {
        final XBaseField f1 = this.dbfFieldFactory.fromStringRepresentation("x,C,1,2");
        final OffsetXBaseField<?> of1 = f1.withOffset(0);
        final XBaseField f2 = this.dbfFieldFactory.fromStringRepresentation("y,N,4,2");
        final OffsetXBaseField<?> of2 = f2.withOffset(1);
        final XBaseField f3 = this.dbfFieldFactory.fromStringRepresentation("z,F,10,2");
        final OffsetXBaseField<?> of3 = f3.withOffset(0);
        final XBaseField f4 = this.dbfFieldFactory.fromStringRepresentation("t,D,8,0");
        final OffsetXBaseField<?> of4 = f4.withOffset(13);

        final GenericMetadata md = PowerMock.createMock(GenericMetadata.class);
        EasyMock.expect(md.getUpdateDate()).andReturn(new Date(119, 9, 25));
        Mockito.<Collection<OffsetXBaseField<?>>>when(md.getOffsetFields())
                .andReturn(Arrays.asList(of1, of2, of3, of4));
        EasyMock.expect(md.getOneRecordLength()).andReturn(24);
        EasyMock.expect(md.getFileType()).andReturn(XBaseFileTypeEnum.dBASEVII1);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DbfWriter w = new DbfWriter(md, bos);
        final Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", "X");
        m1.put("y", new BigDecimal(10));
        m1.put("z", new Float(16));
        m1.put("t", new Date(0));
        w.write(m1);
        Assert.assertArrayEquals(
                new byte[]{68, 19, 10, 5, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 67, 0, 0, 0,
                        0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 121, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 78, 1, 0, 0, 0, 4, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        122, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 0, 0, 0, 0, 10, 2, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 116, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 68, 13, 0, 0, 0,
                        8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 32, 49, 54, 44, 48, 48,
                        48, 48, 48, 48, 32, 32, 32, 49, 57, 55, 48, 48, 49, 48, 49, 32, 32, 32},
                bos.toByteArray());
    }

    @Test
    public void TestWriteY() throws IOException {
        this.testWriteUnsupported("x,Y,1,2", "Unknown or unsupported field type Currency for");
    }

    @Test
    public void TestWriteTABO() throws IOException {
        final XBaseField f1 = this.dbfFieldFactory.fromStringRepresentation("x,T,1,2");
        final OffsetXBaseField<?> of1 = f1.withOffset(0);
        final XBaseField f2 = this.dbfFieldFactory.fromStringRepresentation("y,@,10,2");
        final OffsetXBaseField<?> of2 = f2.withOffset(1);
        final XBaseField f3 = this.dbfFieldFactory.fromStringRepresentation("z,B,4,2");
        final OffsetXBaseField<?> of3 = f3.withOffset(0);
        final XBaseField f4 = this.dbfFieldFactory.fromStringRepresentation("t,O,6,2");
        final OffsetXBaseField<?> of4 = f4.withOffset(13);

        final GenericMetadata md = PowerMock.createMock(GenericMetadata.class);
        EasyMock.expect(md.getUpdateDate()).andReturn(new Date(119, 9, 25));
        Mockito.<Collection<OffsetXBaseField<?>>>when(md.getOffsetFields())
                .andReturn(Arrays.asList(of1, of2, of3, of4));
        EasyMock.expect(md.getOneRecordLength()).andReturn(30);
        EasyMock.expect(md.getFileType()).andReturn(XBaseFileTypeEnum.dBASEVII1);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DbfWriter w = new DbfWriter(md, bos);
        final Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", new Date(0));
        m1.put("y", new Date(5));
        m1.put("z", new BigDecimal("1.5"));
        m1.put("t", new Double(1.5));
        w.write(m1);
        Assert.assertArrayEquals(
                new byte[]{68, 19, 10, 5, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 84, 0, 0, 0,
                        0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 121, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 64, 1, 0, 0, 0, 10, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        122, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 78, 0, 0, 0, 0, 4, 2, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 116, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 79, 13, 0, 0, 0,
                        6, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 49, 46, 53, 99, -1, 0,
                        54, -18, -128, 32, 32, 32, 32, 63, -8, 0, 0, 0, 0, 0, 0, 32, 32, 32, 32, 32,
                        32, 32, 32, 32}, bos.toByteArray());
    }

    @Test
    public void TestWriteM() throws IOException {
        this.testWriteUnsupported("x,M,1,2", "Unknown or unsupported field type Memo for");
    }

    @Test
    public void TestWriteG() throws IOException {
        this.testWriteUnsupported("x,G,1,2", "Unknown or unsupported field type General for");
    }

    @Test
    public void TestWriteP() throws IOException {
        this.testWriteUnsupported("x,P,1,2", "Unknown or unsupported field type Picture for");
    }

    @Test
    public void TestWriteZ() throws IOException {
        this.testWriteUnsupported("x,0,1,2", "Unknown or unsupported field type NullFlags for");
    }

    @Test
    public void TestWriteILFDoubleNull() throws IOException {
        final XBaseField f1 = this.dbfFieldFactory.fromStringRepresentation("x,I,1,2");
        final OffsetXBaseField<?> of1 = f1.withOffset(0);
        final XBaseField f2 = this.dbfFieldFactory.fromStringRepresentation("y,L,1,0");
        final OffsetXBaseField<?> of2 = f2.withOffset(1);
        final XBaseField f3 = this.dbfFieldFactory.fromStringRepresentation("z,F,10,2");
        final OffsetXBaseField<?> of3 = f3.withOffset(2);
        final XBaseField f4 = this.dbfFieldFactory.fromStringRepresentation("t,F,10,2");
        final OffsetXBaseField<?> of4 = f4.withOffset(3);

        final GenericMetadata md = PowerMock.createMock(GenericMetadata.class);
        EasyMock.expect(md.getUpdateDate()).andReturn(new Date(119, 9, 25));
        Mockito.<Collection<OffsetXBaseField<?>>>when(md.getOffsetFields())
                .andReturn(Arrays.asList(of1, of2, of3, of4));
        EasyMock.expect(md.getOneRecordLength()).andReturn(30);
        EasyMock.expect(md.getFileType()).andReturn(XBaseFileTypeEnum.dBASEVII1);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DbfWriter w = new DbfWriter(md, bos);
        final Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", 5);
        m1.put("y", true);
        m1.put("z", new Double(15.6));
        m1.put("t", null);
        w.write(m1);
        Assert.assertArrayEquals(
                new byte[]{68, 19, 10, 5, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 73, 0, 0, 0,
                        0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 121, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 76, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        122, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 2, 0, 0, 0, 10, 2, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 116, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 3, 0, 0, 0,
                        10, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 0, 84, 32, 49, 53, 44,
                        54, 48, 48, 48, 48, 48, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
                        32, 32, 32, 32, 32}, bos.toByteArray());
    }

    private void testWriteUnsupported(final String ch, final String error) throws IOException {
        final XBaseField f1 = this.dbfFieldFactory.fromStringRepresentation(ch);
        final OffsetXBaseField<?> of1 = f1.withOffset(0);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        final GenericMetadata md = PowerMock.createMock(GenericMetadata.class);
        EasyMock.expect(md.getOffsetFields())
                .andReturn(Collections.<OffsetXBaseField<?>>singletonList(of1));
        EasyMock.expect(md.getFileType()).andReturn(XBaseFileTypeEnum.dBASEVII1);

        final DbfWriter w = new DbfWriter(md, bos);
        final Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", "X");

        this.exception.expect(UnsupportedOperationException.class);
        this.exception.expectMessage(error);
        w.write(m1);
    }

    @Test
    public void TestBigDecimal() throws IOException {
        final XBaseField f1 = this.dbfFieldFactory.fromStringRepresentation("x,N,1,2");
        final OffsetXBaseField<?> of1 = f1.withOffset(0);

        final GenericMetadata md = PowerMock.createMock(GenericMetadata.class);
        EasyMock.expect(md.getUpdateDate()).andReturn(new Date(119, 9, 25));
        EasyMock.expect(md.getOffsetFields())
                .andReturn(Collections.<OffsetXBaseField<?>>singletonList(of1));
        EasyMock.expect(md.getOneRecordLength()).andReturn(30);
        EasyMock.expect(md.getFileType()).andReturn(XBaseFileTypeEnum.dBASEVII1);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DbfWriter w = new DbfWriter(md, bos);
        final Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", new BigDecimal(1234465646488L));
        w.write(m1);
        w.close();
        Assert.assertArrayEquals(
                new byte[]{68, 19, 10, 5, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 78, 0, 0, 0,
                        0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 49, 32, 32, 32, 32,
                        32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
                        32, 32, 32, 32, 32, 32}, bos.toByteArray());
    }

    @Test
    public void setStringCharset() throws IOException {
        final XBaseField f1 = this.dbfFieldFactory.fromStringRepresentation("x,C,1,2");
        final OffsetXBaseField<?> of1 = f1.withOffset(0);

        final GenericMetadata md = PowerMock.createMock(GenericMetadata.class);
        EasyMock.expect(md.getUpdateDate()).andReturn(new Date(119, 9, 25));
        EasyMock.expect(md.getOffsetFields())
                .andReturn(Collections.<OffsetXBaseField<?>>singletonList(of1));
        EasyMock.expect(md.getOneRecordLength()).andReturn(30);
        EasyMock.expect(md.getFileType()).andReturn(XBaseFileTypeEnum.dBASEVII1);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DbfWriter w = new DbfWriter(md, bos);
        w.setStringCharset("ISO-8859-1");
        final Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", "é");
        w.write(m1);

        Assert.assertArrayEquals(
                new byte[]{68, 19, 10, 5, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 67, 0, 0, 0,
                        0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, -23, 32, 32, 32, 32,
                        32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
                        32, 32, 32, 32, 32, 32}, bos.toByteArray());
    }
     */
}