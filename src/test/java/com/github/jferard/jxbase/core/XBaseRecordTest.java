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

package com.github.jferard.jxbase.core;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.DB2CharacterAccess;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.dialect.db3.memo.DB3MemoReader;
import com.github.jferard.jxbase.dialect.db3.reader.DB3RecordReader;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.foxpro.FoxProAccess;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialectFactory;
import com.github.jferard.jxbase.dialect.foxpro.memo.TextMemoRecord;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.reader.XBaseRecordReader;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;

public class XBaseRecordTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private XBaseMemoReader mr;
    private GenericMetadata md;
    private XBaseDialect<FoxProDialect, FoxProAccess> dialect;
    private FoxProAccess access;
    // new XBaseRecord("0000000000".getBytes(JxBaseUtils.ASCII_CHARSET), this.md, this.mr, 1);

    @Before
    public void setUp() throws IOException {
        this.mr = PowerMock.createMock(DB3MemoReader.class);
        this.md = PowerMock.createMock(GenericMetadata.class);
        this.dialect = FoxProDialectFactory
                .create(XBaseFileTypeEnum.dBASE4SQLTable, JxBaseUtils.ASCII_CHARSET, TimeZone
                        .getDefault()).reader(this.mr).build();
        this.access = this.dialect.getAccess();
    }

    @Test
    public void testDeleted() throws IOException, ParseException {
        final Charset ascii = JxBaseUtils.ASCII_CHARSET;
        final InputStream in = new ByteArrayInputStream("*abc".getBytes(ascii));
        final GenericFieldDescriptorArray<DB4Access> array =
                new GenericFieldDescriptorArray<DB4Access>(
                        Collections.<XBaseField<? super DB4Access>>emptyList(), 0, 4);
        final XBaseRecordReader reader =
                new DB3RecordReader<DB4Access>(null, in, ascii, array, JxBaseUtils.UTC_TIME_ZONE);
        final XBaseRecord record = reader.read();
        Assert.assertTrue(record.isDeleted());
    }

    @Test
    public void testString() throws IOException, ParseException {
        final Charset ascii = JxBaseUtils.ASCII_CHARSET;
        final InputStream in = new ByteArrayInputStream(" abc".getBytes(ascii));
        final GenericFieldDescriptorArray<DB2CharacterAccess> array =
                new GenericFieldDescriptorArray<DB2CharacterAccess>(
                        Collections.<XBaseField<? super DB2CharacterAccess>>singleton(
                                new CharacterField("y", 3)), 0, 4);
        final XBaseRecordReader reader = new DB3RecordReader<DB2CharacterAccess>(
                new DB2CharacterAccess(new RawRecordReadHelper(JxBaseUtils.ASCII_CHARSET), null),
                in, ascii, array, JxBaseUtils.UTC_TIME_ZONE);
        final XBaseRecord record = reader.read();
        Assert.assertFalse(record.isDeleted());
        Assert.assertEquals("abc", record.getMap().get("y"));
    }

    @Test
    public void testGetBoolean() throws IOException {
        PowerMock.resetAll();

        final XBaseField<? super FoxProAccess> field1 =
                TestHelper.fromStringRepresentation(this.dialect, "x,L,1,0");
        final XBaseField<? super FoxProAccess> field2 =
                TestHelper.fromStringRepresentation(this.dialect, "y,L,1,0");
        final XBaseField<? super FoxProAccess> field3 =
                TestHelper.fromStringRepresentation(this.dialect, "z,L,1,0");
        final byte[] recordBuffer = "tf0".getBytes(JxBaseUtils.ASCII_CHARSET);
        PowerMock.replayAll();

        final Boolean value1 = (Boolean) field1.getValue(this.access, recordBuffer, 0, 1);
        final Boolean value2 = (Boolean) field2.getValue(this.access, recordBuffer, 1, 1);
        final Boolean value3 = (Boolean) field3.getValue(this.access, recordBuffer, 2, 1);
        PowerMock.verifyAll();

        Assert.assertTrue(value1);
        Assert.assertFalse(value2);
        Assert.assertNull(value3);
    }

    @Test
    public void testVoidBoolean() {
        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage("A boolean has one char");
        TestHelper.fromStringRepresentation(this.dialect, "x,L,0,0");
    }

    @Test
    public void testVoidDate() {
        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage("A date has 8 chars");
        TestHelper.fromStringRepresentation(this.dialect, "x,D,0,0");
    }

    @Test
    public void testGetDate() throws IOException, ParseException {
        final XBaseField field = TestHelper.fromStringRepresentation(this.dialect, "x,D,8,0");
        PowerMock.resetAll();
        PowerMock.replayAll();

        final byte[] buffer = "20100501".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Object value = field.getValue(this.access, buffer, 0, 8);
        PowerMock.verifyAll();

        final Calendar cal = Calendar.getInstance();
        cal.set(2010, Calendar.MAY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Assert.assertEquals(cal.getTime(), value);
    }

    @Test
    public void testGetInteger() throws IOException, ParseException {
        final XBaseField field = TestHelper.fromStringRepresentation(this.dialect, "x,I,4,0");
        PowerMock.resetAll();
        PowerMock.replayAll();

        final byte[] buffer = "abcd".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Object value = field.getValue(this.access, buffer, 0, 4);
        PowerMock.verifyAll();

        Assert.assertEquals(1684234849L, value);
    }

    @Test
    public void testGetNullDouble() throws IOException, ParseException {
        final XBaseField field = TestHelper.fromStringRepresentation(this.dialect,"x,B,8,0");
        PowerMock.resetAll();
        PowerMock.replayAll();

        final byte[] buffer = "abcdefgh".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Object value = field.getValue(this.access, buffer, 0, 8);
        PowerMock.verifyAll();

        Assert.assertEquals(1.2926117907728089E161, (double) value, 0.01);
    }

    @Test
    public void testGetString() throws IOException {
        final XBaseField field = TestHelper.fromStringRepresentation(this.dialect,"x,C,4,0");
        PowerMock.resetAll();
        PowerMock.replayAll();

        final byte[] buffer = " bc ".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Object value = field.getValue(this.access, buffer, 0, 4);
        PowerMock.verifyAll();

        Assert.assertEquals("bc", (String) value);
    }

    @Test
    public void testMemoAsString() throws IOException {
        final TextMemoRecord mrec = PowerMock.createMock(TextMemoRecord.class);
        final XBaseField field = TestHelper.fromStringRepresentation(this.dialect,"y,M,4,4");
        PowerMock.resetAll();

        EasyMock.expect(this.mr.read(EasyMock.anyInt())).andReturn(mrec);
        EasyMock.expect(mrec.getValue()).andReturn("ok");
        PowerMock.replayAll();

        final byte[] buffer = "0123".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Object rec = field.getValue(this.access, buffer, 0, 4);
        final String value = ((TextMemoRecord) rec).getValue();
        PowerMock.verifyAll();

        Assert.assertEquals("ok", value);
    }

    /*

   @Test
    public void testNotDeleted() throws IOException, ParseException {
        final Charset ascii = JxBaseUtils.ASCII_CHARSET;
        final InputStream in = new ByteArrayInputStream("abc".getBytes(ascii));
        final DB3RecordReader<DB3Dialect, DB3Access> reader = new DB3RecordReader(null, in, ascii,
                new GenericFieldDescriptorArray(Collections.<XBaseField>emptyList(), 0, 3), null);
        final XBaseRecord record = reader.read();
        Assert.assertFalse(record.isDeleted());
        Assert.assertEquals(1, record.getRecordNumber());
    }

        Assert.assertEquals("", record.getMap());
    }

    @Test
    public void testMemoAsBytes() throws IOException {
        final TextMemoRecord mrec = PowerMock.createMock(TextMemoRecord.class);

        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x"))
                .andReturn(TestHelper.fromStringRepresentation(this.dialect,"y,M,4,4").withOffset(0));
        EasyMock.expect(this.mr.read(EasyMock.anyInt())).andReturn(mrec);
        EasyMock.expect(mrec.getBytes()).andReturn("ok".getBytes(JxBaseUtils.ASCII_CHARSET));

        final GenericRecord record = new GenericRecord("0123456789".getBytes(JxBaseUtils.ASCII_CHARSET), this.md,
                this.mr, 1);
        Assert.assertArrayEquals("ok".getBytes(JxBaseUtils.ASCII_CHARSET), record.getMemoAsBytes("x"));
    }

    @Test
    public void testMemoAsBytesFailure() throws IOException {
        final TextMemoRecord mrec = PowerMock.createMock(TextMemoRecord.class);

        EasyMock.expect(this.mr.read(EasyMock.anyInt())).andReturn(mrec);
        EasyMock.expect(mrec.getValue()).andReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x"))
                .andReturn(TestHelper.fromStringRepresentation(this.dialect,"y,C,1,0").withOffset(0));

        final GenericRecord record = new GenericRecord("a".getBytes(JxBaseUtils.ASCII_CHARSET), this.md, this.mr, 1);

        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage("Field 'x' is not MEMO field!");
        record.getMemoAsBytes("x");
    }

    @Test
    public void testMemoAsBytes10() throws IOException {
        final TextMemoRecord mrec = PowerMock.createMock(TextMemoRecord.class);

        EasyMock.expect(this.mr.read(EasyMock.anyInt())).andReturn(mrec);
        EasyMock.expect(mrec.getValue()).andReturn("ok");
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x"))
                .andReturn(TestHelper.fromStringRepresentation(this.dialect,"y,M,10,0").withOffset
                (0));

        final GenericRecord record = new GenericRecord("0000000000".getBytes(JxBaseUtils.ASCII_CHARSET), this.md,
                this.mr, 1);

        Assert.assertArrayEquals(new byte[]{}, record.getMemoAsBytes("x"));
    }

    @Test
    public void testMap() throws Exception {
        final XBaseField f1 = TestHelper.fromStringRepresentation(this.dialect,"x,C,1,2");
        final OffsetXBaseField<?> of1 = f1.withOffset(0);
        final XBaseField f2 = TestHelper.fromStringRepresentation(this.dialect,"y,D,8,0");
        final OffsetXBaseField<?> of2 = f2.withOffset(1);
        final XBaseField f3 = TestHelper.fromStringRepresentation(this.dialect,"z,N,1,2");
        final OffsetXBaseField<?> of3 = f3.withOffset(9);
        final XBaseField f4 = TestHelper.fromStringRepresentation(this.dialect,"t,L,1,0");
        final OffsetXBaseField<?> of4 = f4.withOffset(10);
        final XBaseField f5 = TestHelper.fromStringRepresentation(this.dialect,"u,I,4,2");
        final OffsetXBaseField<?> of5 = f5.withOffset(11);

        Mockito.<Collection<XBaseField>>when(this.md.getFields())
                .andReturn(Arrays.asList(f1, f2, f3, f4, f5));
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).andReturn(of1);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("y")).andReturn(of2);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("z")).andReturn(of3);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("t")).andReturn(of4);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("u")).andReturn(of5);

        final GenericRecord record = new GenericRecord("a201005018t1234".getBytes(JxBaseUtils.ASCII_CHARSET), this.md,
                this.mr, 1);
        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("x", "a");
        expected.put("y", new Date(110, 4, 1));
        expected.put("z", new BigDecimal(8));
        expected.put("t", true);
        expected.put("u", 875770417);
        Assert.assertEquals(expected, record.toMap(JxBaseUtils.ASCII_CHARSET));
    }

    @Test
    public void testStringRepresentation() throws Exception {
        final XBaseField f1 = TestHelper.fromStringRepresentation(this.dialect,"x,C,1,0");
        final OffsetXBaseField<?> of1 = f1.withOffset(0);
        final XBaseField f2 = TestHelper.fromStringRepresentation(this.dialect,"y,D,8,0");
        final OffsetXBaseField<?> of2 = f2.withOffset(1);
        final XBaseField f3 = TestHelper.fromStringRepresentation(this.dialect,"z,N,1,2");
        final OffsetXBaseField<?> of3 = f3.withOffset(9);
        final XBaseField f4 = TestHelper.fromStringRepresentation(this.dialect,"t,L,1,2");
        final OffsetXBaseField<?> of4 = f4.withOffset(10);

        Mockito.<Collection<XBaseField>>when(this.md.getFields())
                .andReturn(Arrays.asList(f1, f2, f3, f4));
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("x")).andReturn(of1);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("y")).andReturn(of2);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("z")).andReturn(of3);
        Mockito.<OffsetXBaseField<?>>when(this.md.getOffsetField("t")).andReturn(of4);


        final GenericRecord record = new GenericRecord("a201005018t".getBytes(JxBaseUtils.ASCII_CHARSET), this.md,
                this.mr, 1);
        Assert.assertEquals("x=a, y=Sat May 01 00:00:00 CEST 2010, z=8, t=true, ",
                record.getStringRepresentation(JxBaseUtils.ASCII_CHARSET));
    }

    @Test
    public void testGetBytes() {
        final GenericRecord record = new GenericRecord("abcd".getBytes(JxBaseUtils.ASCII_CHARSET), this.md, this.mr, 1);
        Assert.assertArrayEquals(new byte[]{97, 98, 99, 100}, record.getBytes());
    }

    /**
     * Specific test of new String() behavior
     **/
    @Test
    public void testImmutableString() {
        final byte[] bytes = "abcd".getBytes(JxBaseUtils.ASCII_CHARSET);
        final String s = new String(bytes, JxBaseUtils.ASCII_CHARSET);
        bytes[0] = 'z';
        Assert.assertEquals("abcd", s);
    }
}
