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

import com.github.jferard.jxbase.reader.MemoReader;
import com.github.jferard.jxbase.util.JdbfUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DbfRecordTest {
    private static final Charset ASCII = Charset.forName("ASCII");
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private MemoReader mr;
    private DbfMetadata md;

    @Before
    public void setUp() {
        mr = Mockito.mock(MemoReader.class);
        md = Mockito.mock(DbfMetadata.class);
    }

    @Test
    public void testDeleted() {
        DbfRecord record = new DbfRecord("*abc".getBytes(Charset.forName("ASCII")), md, mr, -1);
        Assert.assertTrue(record.isDeleted());
    }

    @Test
    public void testNotDeleted() {
        DbfRecord record = new DbfRecord("abc".getBytes(Charset.forName("ASCII")), md, mr, -1);
        Assert.assertFalse(record.isDeleted());
    }

    @Test
    public void testBytes() {
        DbfRecord record = new DbfRecord("abc".getBytes(ASCII), md, mr, -1);
        Assert.assertArrayEquals(new byte[]{97, 98, 99}, record.getBytes());
    }

    @Test
    public void testRecordNumber() {
        DbfRecord record = new DbfRecord("abc".getBytes(Charset.forName("ASCII")), md, mr, -1);
        Assert.assertEquals(-1, record.getRecordNumber());
    }

    @Test
    public void testString() {
        final OffsetDbfField<?> of = DbfFieldImpl.fromStringRepresentation("y,I,3,4").withOffset(0);
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of);

        DbfRecord record = new DbfRecord("abc".getBytes(ASCII), md, mr, -1);
        Assert.assertEquals("abc", record.getString("x", ASCII));
    }

    @Test
    public void testMemoAsString() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);

        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x"))
                .thenReturn(DbfFieldImpl.fromStringRepresentation("y,M,4,4").withOffset(0));
        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");

        DbfRecord record = new DbfRecord("0123456789".getBytes(ASCII), md, mr, 1);
        Assert.assertEquals("ok", record.getMemoAsString("x", ASCII));
    }

    @Test
    public void testMemoAsStringFailure() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x"))
                .thenReturn(DbfFieldImpl.fromStringRepresentation("y,C,4,4").withOffset(0));

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Field 'x' is not MEMO field!");
        DbfRecord record = new DbfRecord("abcd".getBytes(ASCII), md, mr, 1);
        record.getMemoAsString("x", ASCII);
    }

    @Test
    public void testMemoAsString10() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x"))
                .thenReturn(DbfFieldImpl.fromStringRepresentation("y,M,10,0").withOffset(0));

        DbfRecord record = new DbfRecord("0000000000".getBytes(ASCII), md, mr, 1);

        Assert.assertEquals("", record.getMemoAsString("x", ASCII));
    }

    @Test
    public void testMemoAsBytes() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);

        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x"))
                .thenReturn(DbfFieldImpl.fromStringRepresentation("y,M,4,4").withOffset(0));
        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValue()).thenReturn("ok".getBytes(ASCII));

        DbfRecord record = new DbfRecord("0123456789".getBytes(ASCII), md, mr, 1);
        Assert.assertArrayEquals("ok".getBytes(ASCII), record.getMemoAsBytes("x"));
    }

    @Test
    public void testMemoAsBytesFailure() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x"))
                .thenReturn(DbfFieldImpl.fromStringRepresentation("y,C,1,0").withOffset(0));

        DbfRecord record = new DbfRecord("a".getBytes(ASCII), md, mr, 1);

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Field 'x' is not MEMO field!");
        record.getMemoAsBytes("x");
    }

    @Test
    public void testMemoAsBytes10() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x"))
                .thenReturn(DbfFieldImpl.fromStringRepresentation("y,M,10,0").withOffset(0));

        DbfRecord record = new DbfRecord("0000000000".getBytes(ASCII), md, mr, 1);

        Assert.assertArrayEquals(new byte[]{}, record.getMemoAsBytes("x"));
    }

    @Test
    public void testMap() throws Exception {
        final DbfField<?> f1 = DbfFieldImpl.fromStringRepresentation("x,C,1,2");
        final OffsetDbfField<?> of1 = f1.withOffset(0);
        final DbfField<?> f2 = DbfFieldImpl.fromStringRepresentation("y,D,8,0");
        final OffsetDbfField<?> of2 = f2.withOffset(1);
        final DbfField<?> f3 = DbfFieldImpl.fromStringRepresentation("z,N,1,2");
        final OffsetDbfField<?> of3 = f3.withOffset(9);
        final DbfField<?> f4 = DbfFieldImpl.fromStringRepresentation("t,L,1,0");
        final OffsetDbfField<?> of4 = f4.withOffset(10);
        final DbfField<?> f5 = DbfFieldImpl.fromStringRepresentation("u,I,4,2");
        final OffsetDbfField<?> of5 = f5.withOffset(11);

        Mockito.when(md.getFields()).thenReturn(Arrays.asList(f1, f2, f3, f4, f5));
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of1);
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("y")).thenReturn(of2);
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("z")).thenReturn(of3);
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("t")).thenReturn(of4);
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("u")).thenReturn(of5);

        DbfRecord record = new DbfRecord("a201005018t1234".getBytes(ASCII), md, mr, 1);
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
        final DbfField<?> f1 = DbfFieldImpl.fromStringRepresentation("x,C,1,0");
        final OffsetDbfField<?> of1 = f1.withOffset(0);
        final DbfField<?> f2 = DbfFieldImpl.fromStringRepresentation("y,D,8,0");
        final OffsetDbfField<?> of2 = f2.withOffset(1);
        final DbfField<?> f3 = DbfFieldImpl.fromStringRepresentation("z,N,1,2");
        final OffsetDbfField<?> of3 = f3.withOffset(9);
        final DbfField<?> f4 = DbfFieldImpl.fromStringRepresentation("t,L,1,2");
        final OffsetDbfField<?> of4 = f4.withOffset(10);

        Mockito.when(md.getFields()).thenReturn(Arrays.asList(f1, f2, f3, f4));
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of1);
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("y")).thenReturn(of2);
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("z")).thenReturn(of3);
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("t")).thenReturn(of4);


        DbfRecord record = new DbfRecord("a201005018t".getBytes(ASCII), md, mr, 1);
        Assert.assertEquals("x=a, y=Sat May 01 00:00:00 CEST 2010, z=8, t=true, ",
                record.getStringRepresentation(ASCII));
    }

    @Test
    public void testGetBytes() {
        DbfRecord record = new DbfRecord("abcd".getBytes(ASCII), md, mr, 1);
        Assert.assertArrayEquals(new byte[]{97, 98, 99, 100}, record.getBytes());
    }

    @Test
    public void testGetBoolean() throws IOException, ParseException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final DbfField<?> field1 = DbfFieldImpl.fromStringRepresentation("x,L,1,0");
        final OffsetDbfField<?> of1 = field1.withOffset(0);
        final DbfField<?> field2 = DbfFieldImpl.fromStringRepresentation("y,L,1,0");
        final OffsetDbfField<?> of2 = field2.withOffset(1);
        final DbfField<?> field3 = DbfFieldImpl.fromStringRepresentation("z,L,1,0");
        final OffsetDbfField<?> of3 = field3.withOffset(2);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of1);
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("y")).thenReturn(of2);
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("z")).thenReturn(of3);

        DbfRecord record = new DbfRecord("tf0".getBytes(ASCII), md, mr, 3);
        Assert.assertTrue((Boolean) field1.getValue(record, ASCII));
        Assert.assertFalse((Boolean) field2.getValue(record, ASCII));
        Assert.assertNull(field3.getValue(record, ASCII));
    }

    @Test
    public void testVoidBoolean() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("A boolean has one char");
        DbfFieldImpl.fromStringRepresentation("x,L,0,0");
    }

    @Test
    public void testVoidDate() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("A date has 8 chars");
        DbfFieldImpl.fromStringRepresentation("x,D,0,0");
    }

    @Test
    public void testSetBoolean() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final OffsetDbfField<?> oft =
                DbfFieldImpl.fromStringRepresentation("x,L,1,0").withOffset(0);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(oft);

        DbfRecord record = new DbfRecord("tf0".getBytes(ASCII), md, mr, 3);
        record.setBoolean("x", true);
    }

    @Test
    public void testSetBytes() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final OffsetDbfField<?> of = DbfFieldImpl.fromStringRepresentation("x,C,3,0").withOffset(0);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of);

        DbfRecord record = new DbfRecord("efg".getBytes(ASCII), md, mr, 3);
        record.setBytes("x", "abc".getBytes(ASCII));
        Assert.assertEquals("abc", record.getASCIIString("x"));
    }

    @Test
    public void testGetDate() throws IOException, ParseException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final DbfField<?> field = DbfFieldImpl.fromStringRepresentation("x,D,8,0");
        final OffsetDbfField<?> of = field.withOffset(0);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of);

        DbfRecord record = new DbfRecord("20100501".getBytes(ASCII), md, mr, 2);
        Assert.assertEquals(new Date(110, 4, 1), field.getValue(record, JdbfUtils.ASCII_CHARSET));
    }

    @Test
    public void testGetInteger() throws IOException, ParseException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final DbfField<?> field = DbfFieldImpl.fromStringRepresentation("x,I,4,0");
        final OffsetDbfField<?> of = field.withOffset(0);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of);

        DbfRecord record = new DbfRecord("abcd".getBytes(ASCII), md, mr, 1);
        Assert.assertEquals(1684234849, field.getValue(record, ASCII));
    }

    @Test
    public void testGetNullBigDecimal() throws IOException, ParseException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final DbfField<?> field = DbfFieldImpl.fromStringRepresentation("x,B,0,0");
        final OffsetDbfField<?> of = field.withOffset(0);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of);

        DbfRecord record = new DbfRecord("abcd".getBytes(ASCII), md, mr, 1);
        Assert.assertNull(field.getValue(record, ASCII));
    }

    @Test
    public void testGetNumericOverflowBigDecimal() throws IOException, ParseException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final DbfField<?> field = DbfFieldImpl.fromStringRepresentation("x,B,4,0");
        final OffsetDbfField<?> of = field.withOffset(0);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of);

        DbfRecord record = new DbfRecord("a*cd".getBytes(ASCII), md, mr, 1);
        Assert.assertNull(field.getValue(record, ASCII));
    }

    @Test
    public void testGetString() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final OffsetDbfField<?> of = DbfFieldImpl.fromStringRepresentation("x,I,4,0").withOffset(0);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of);

        DbfRecord record = new DbfRecord(" bc ".getBytes(ASCII), md, mr, 1);
        Assert.assertEquals("bc", record.getString("x", Charset.forName("UTF-8")));
    }

    @Test
    public void testGetStringWithCharset() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final OffsetDbfField<?> of = DbfFieldImpl.fromStringRepresentation("x,I,4,0").withOffset(0);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of);

        DbfRecord record = new DbfRecord("abcd".getBytes(ASCII), md, mr, 1);
        Assert.assertEquals("abcd", record.getString("x", UTF8_CHARSET));
    }

    @Test
    public void testFieldBytes() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final OffsetDbfField<?> of = DbfFieldImpl.fromStringRepresentation("x,I,4,0").withOffset(0);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.<OffsetDbfField<?>>when(md.getOffsetField("x")).thenReturn(of);

        DbfRecord record = new DbfRecord("abcd".getBytes(ASCII), md, mr, 1);
        Assert.assertArrayEquals(new byte[]{97, 98, 99, 100}, record.getBytes("x"));
    }

    /**
     * Specific test of new String() behavior
     */
    @Test
    public void testImmutableString() {
        byte[] bytes = "abcd".getBytes(ASCII);
        String s = new String(bytes, ASCII);
        bytes[0] = 'z';
        Assert.assertEquals("abcd", s);
    }
}
