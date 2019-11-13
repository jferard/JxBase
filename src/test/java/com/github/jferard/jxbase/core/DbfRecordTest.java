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

package com.github.jferard.jxbase.core;

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.XBaseRecord;
import com.github.jferard.jxbase.dialect.db3memo.DB3MemoDialect;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.dialect.db3memo.DB3MemoReader;
import com.github.jferard.jxbase.dialect.foxpro.FoxProRecordReader;
import com.github.jferard.jxbase.dialect.memo.XBaseMemoReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Collections;

public class DbfRecordTest {
    private static final Charset ASCII = Charset.forName("ASCII");
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private XBaseMemoReader mr;
    private GenericMetadata md;
    private DB3MemoDialect dbfFieldFactory;

    @Before
    public void setUp() {
        this.mr = Mockito.mock(DB3MemoReader.class);
        this.md = Mockito.mock(GenericMetadata.class);
        this.dbfFieldFactory = new DB3MemoDialect(XBaseFileTypeEnum.dBASEIV1);
    }

    @Test
    public void testDeleted() throws IOException, ParseException {
        final Charset ascii = Charset.forName("ASCII");
        final InputStream in = new ByteArrayInputStream("*abc".getBytes(ascii));
        final FoxProRecordReader reader =
                new FoxProRecordReader(null, in, ascii, new GenericFieldDescriptorArray(Collections.<XBaseField>emptyList(), 0, 4),
                        null, null);
        final XBaseRecord record = reader.read();
        Assert.assertTrue(record.isDeleted());
    }

    @Test
    public void testNotDeleted() throws IOException, ParseException {
        final Charset ascii = Charset.forName("ASCII");
        final InputStream in = new ByteArrayInputStream("abc".getBytes(ascii));
        final FoxProRecordReader reader =
                new FoxProRecordReader(null, in, ascii, new GenericFieldDescriptorArray(Collections.<XBaseField>emptyList(), 0, 3),
                        null, null);
        final XBaseRecord record = reader.read();
        Assert.assertFalse(record.isDeleted());
        Assert.assertEquals(1, record.getRecordNumber());
    }

    /*
    @Test
    public void testString() {
        final OffsetXBaseField<?> of =
                this.dbfFieldFactory.fromStringRepresentation("y,I,3,4").withOffset(0);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).thenReturn(of);

        final GenericRecord record = new GenericRecord("abc".getBytes(ASCII), this.md, this.mr, -1);
        Assert.assertEquals("abc", record.getString("x", ASCII));
    }

    @Test
    public void testMemoAsString() throws IOException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);

        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x"))
                .thenReturn(this.dbfFieldFactory.fromStringRepresentation("y,M,4,4").withOffset(0));
        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");

        final GenericRecord record = new GenericRecord("0123456789".getBytes(ASCII), this.md,
                this.mr, 1);
        Assert.assertEquals("ok", record.getMemoAsString("x"));
    }

    @Test
    public void testMemoAsStringFailure() throws IOException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x"))
                .thenReturn(this.dbfFieldFactory.fromStringRepresentation("y,C,4,4").withOffset(0));

        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage("Field 'x' is not MEMO field!");
        final GenericRecord record = new GenericRecord("abcd".getBytes(ASCII), this.md, this.mr, 1);
        record.getMemoAsString("x");
    }

    @Test
    public void testMemoAsString10() throws IOException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x"))
                .thenReturn(this.dbfFieldFactory.fromStringRepresentation("y,M,10,0").withOffset(0));

        final GenericRecord record = new GenericRecord("0000000000".getBytes(ASCII), this.md,
                this.mr, 1);

        Assert.assertEquals("", record.getMemoAsString("x"));
    }

    @Test
    public void testMemoAsBytes() throws IOException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);

        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x"))
                .thenReturn(this.dbfFieldFactory.fromStringRepresentation("y,M,4,4").withOffset(0));
        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getBytes()).thenReturn("ok".getBytes(ASCII));

        final GenericRecord record = new GenericRecord("0123456789".getBytes(ASCII), this.md,
                this.mr, 1);
        Assert.assertArrayEquals("ok".getBytes(ASCII), record.getMemoAsBytes("x"));
    }

    @Test
    public void testMemoAsBytesFailure() throws IOException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x"))
                .thenReturn(this.dbfFieldFactory.fromStringRepresentation("y,C,1,0").withOffset(0));

        final GenericRecord record = new GenericRecord("a".getBytes(ASCII), this.md, this.mr, 1);

        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage("Field 'x' is not MEMO field!");
        record.getMemoAsBytes("x");
    }

    @Test
    public void testMemoAsBytes10() throws IOException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x"))
                .thenReturn(this.dbfFieldFactory.fromStringRepresentation("y,M,10,0").withOffset(0));

        final GenericRecord record = new GenericRecord("0000000000".getBytes(ASCII), this.md,
                this.mr, 1);

        Assert.assertArrayEquals(new byte[]{}, record.getMemoAsBytes("x"));
    }

    @Test
    public void testMap() throws Exception {
        final XBaseField f1 = this.dbfFieldFactory.fromStringRepresentation("x,C,1,2");
        final OffsetXBaseField<?> of1 = f1.withOffset(0);
        final XBaseField f2 = this.dbfFieldFactory.fromStringRepresentation("y,D,8,0");
        final OffsetXBaseField<?> of2 = f2.withOffset(1);
        final XBaseField f3 = this.dbfFieldFactory.fromStringRepresentation("z,N,1,2");
        final OffsetXBaseField<?> of3 = f3.withOffset(9);
        final XBaseField f4 = this.dbfFieldFactory.fromStringRepresentation("t,L,1,0");
        final OffsetXBaseField<?> of4 = f4.withOffset(10);
        final XBaseField f5 = this.dbfFieldFactory.fromStringRepresentation("u,I,4,2");
        final OffsetXBaseField<?> of5 = f5.withOffset(11);

        Mockito.<Collection<XBaseField>>when(this.md.getFields())
                .thenReturn(Arrays.asList(f1, f2, f3, f4, f5));
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).thenReturn(of1);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("y")).thenReturn(of2);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("z")).thenReturn(of3);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("t")).thenReturn(of4);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("u")).thenReturn(of5);

        final GenericRecord record = new GenericRecord("a201005018t1234".getBytes(ASCII), this.md,
                this.mr, 1);
        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("x", "a");
        expected.put("y", new Date(110, 4, 1));
        expected.put("z", new BigDecimal(8));
        expected.put("t", true);
        expected.put("u", 875770417);
        Assert.assertEquals(expected, record.toMap(ASCII));
    }

    @Test
    public void testStringRepresentation() throws Exception {
        final XBaseField f1 = this.dbfFieldFactory.fromStringRepresentation("x,C,1,0");
        final OffsetXBaseField<?> of1 = f1.withOffset(0);
        final XBaseField f2 = this.dbfFieldFactory.fromStringRepresentation("y,D,8,0");
        final OffsetXBaseField<?> of2 = f2.withOffset(1);
        final XBaseField f3 = this.dbfFieldFactory.fromStringRepresentation("z,N,1,2");
        final OffsetXBaseField<?> of3 = f3.withOffset(9);
        final XBaseField f4 = this.dbfFieldFactory.fromStringRepresentation("t,L,1,2");
        final OffsetXBaseField<?> of4 = f4.withOffset(10);

        Mockito.<Collection<XBaseField>>when(this.md.getFields())
                .thenReturn(Arrays.asList(f1, f2, f3, f4));
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).thenReturn(of1);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("y")).thenReturn(of2);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("z")).thenReturn(of3);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("t")).thenReturn(of4);


        final GenericRecord record = new GenericRecord("a201005018t".getBytes(ASCII), this.md,
                this.mr, 1);
        Assert.assertEquals("x=a, y=Sat May 01 00:00:00 CEST 2010, z=8, t=true, ",
                record.getStringRepresentation(ASCII));
    }

    @Test
    public void testGetBytes() {
        final GenericRecord record = new GenericRecord("abcd".getBytes(ASCII), this.md, this.mr, 1);
        Assert.assertArrayEquals(new byte[]{97, 98, 99, 100}, record.getBytes());
    }

    @Test
    public void testGetBoolean() throws IOException, ParseException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);
        final XBaseField field1 = this.dbfFieldFactory.fromStringRepresentation("x,L,1,0");
        final OffsetXBaseField<?> of1 = field1.withOffset(0);
        final XBaseField field2 = this.dbfFieldFactory.fromStringRepresentation("y,L,1,0");
        final OffsetXBaseField<?> of2 = field2.withOffset(1);
        final XBaseField field3 = this.dbfFieldFactory.fromStringRepresentation("z,L,1,0");
        final OffsetXBaseField<?> of3 = field3.withOffset(2);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).thenReturn(of1);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("y")).thenReturn(of2);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("z")).thenReturn(of3);

        final GenericRecord record = new GenericRecord("tf0".getBytes(ASCII), this.md, this.mr, 3);
        Assert.assertTrue((Boolean) field1.getValue(record, ASCII));
        Assert.assertFalse((Boolean) field2.getValue(record, ASCII));
        Assert.assertNull(field3.getValue(record, ASCII));
    }

    @Test
    public void testVoidBoolean() {
        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage("A boolean has one char");
        this.dbfFieldFactory.fromStringRepresentation("x,L,0,0");
    }

    @Test
    public void testVoidDate() {
        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage("A date has 8 chars");
        this.dbfFieldFactory.fromStringRepresentation("x,D,0,0");
    }

    @Test
    public void testGetDate() throws IOException, ParseException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);
        final XBaseField field = this.dbfFieldFactory.fromStringRepresentation("x,D,8,0");
        final OffsetXBaseField<?> of = field.withOffset(0);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).thenReturn(of);

        final GenericRecord record = new GenericRecord("20100501".getBytes(ASCII), this.md,
                this.mr, 2);
        Assert.assertEquals(new Date(110, 4, 1), field.getValue(record, JdbfUtils.ASCII_CHARSET));
    }

    @Test
    public void testGetInteger() throws IOException, ParseException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);
        final XBaseField field = this.dbfFieldFactory.fromStringRepresentation("x,I,4,0");
        final OffsetXBaseField<?> of = field.withOffset(0);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).thenReturn(of);

        final GenericRecord record = new GenericRecord("abcd".getBytes(ASCII), this.md, this.mr, 1);
        Assert.assertEquals(1684234849, field.getValue(record, ASCII));
    }

    @Test
    public void testGetNullBigDecimal() throws IOException, ParseException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);
        final XBaseField field = this.dbfFieldFactory.fromStringRepresentation("x,B,0,0");
        final OffsetXBaseField<?> of = field.withOffset(0);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).thenReturn(of);

        final GenericRecord record = new GenericRecord("abcd".getBytes(ASCII), this.md, this.mr, 1);
        Assert.assertNull(field.getValue(record, ASCII));
    }

    @Test
    public void testGetNumericOverflowBigDecimal() throws IOException, ParseException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);
        final XBaseField field = this.dbfFieldFactory.fromStringRepresentation("x,B,4,0");
        final OffsetXBaseField<?> of = field.withOffset(0);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).thenReturn(of);

        final GenericRecord record = new GenericRecord("a*cd".getBytes(ASCII), this.md, this.mr, 1);
        Assert.assertNull(field.getValue(record, ASCII));
    }

    @Test
    public void testGetString() throws IOException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);
        final OffsetXBaseField<?> of =
                this.dbfFieldFactory.fromStringRepresentation("x,I,4,0").withOffset(0);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).thenReturn(of);

        final GenericRecord record = new GenericRecord(" bc ".getBytes(ASCII), this.md, this.mr, 1);
        Assert.assertEquals("bc", record.getString("x", Charset.forName("UTF-8")));
    }

    @Test
    public void testGetStringWithCharset() throws IOException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);
        final OffsetXBaseField<?> of =
                this.dbfFieldFactory.fromStringRepresentation("x,I,4,0").withOffset(0);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).thenReturn(of);

        final GenericRecord record = new GenericRecord("abcd".getBytes(ASCII), this.md, this.mr, 1);
        Assert.assertEquals("abcd", record.getString("x", UTF8_CHARSET));
    }

    @Test
    public void testFieldBytes() throws IOException {
        final DbfTextMemoRecord mrec = Mockito.mock(DbfTextMemoRecord.class);
        final OffsetXBaseField<?> of =
                this.dbfFieldFactory.fromStringRepresentation("x,I,4,0").withOffset(0);

        Mockito.<XBaseMemoRecord<?>>when(this.mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).thenReturn(of);

        final GenericRecord record = new GenericRecord("abcd".getBytes(ASCII), this.md, this.mr, 1);
        Assert.assertArrayEquals(new byte[]{97, 98, 99, 100}, record.getBytes("x"));
    }

    /**
     * Specific test of new String() behavior
     *
    @Test
    public void testImmutableString() {
        final byte[] bytes = "abcd".getBytes(ASCII);
        final String s = new String(bytes, ASCII);
        bytes[0] = 'z';
        Assert.assertEquals("abcd", s);
    }
    */
}
