/*
* JxBase - Copyright (c) 2019-2021 Julien Férard
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

package com.github.jferard.jxbase.core;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.db3.memo.DB3MemoReader;
import com.github.jferard.jxbase.dialect.db3.reader.DB3RecordReader;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.foxpro.memo.TextMemoRecord;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.dialect.vfoxpro.VisualFoxProDialectFactory;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.reader.XBaseRecordReader;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class XBaseRecordTest {
    private XBaseMemoReader mr;
    private GenericMetadata md;
    private VisualFoxProDialect dialect;
    private VisualFoxProAccess access;
    // new XBaseRecord("0000000000".getBytes(JxBaseUtils.ASCII_CHARSET), this.md, this.mr, 1);

    @Before
    public void setUp() {
        this.mr = PowerMock.createMock(DB3MemoReader.class);
        this.md = PowerMock.createMock(GenericMetadata.class);
        this.dialect = VisualFoxProDialectFactory
                .create(XBaseFileTypeEnum.dBASE4SQLTable, JxBaseUtils.ASCII_CHARSET, TimeZone
                        .getDefault());
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
                new DB3RecordReader<DB4Access>(null, in, null, ascii, array, JxBaseUtils.UTC_TIME_ZONE);
        final XBaseRecord record = reader.read();
        Assert.assertTrue(record.isDeleted());
    }

    @Test
    public void testString() throws IOException, ParseException {
        final Charset ascii = JxBaseUtils.ASCII_CHARSET;
        final InputStream in = new ByteArrayInputStream(" abc".getBytes(ascii));
        final GenericFieldDescriptorArray<DB2Access> array =
                new GenericFieldDescriptorArray<DB2Access>(
                        Collections.<XBaseField<? super DB2Access>>singleton(
                                new CharacterField("y", 3)), 0, 4);
        final XBaseRecordReader reader = new DB3RecordReader<DB2Access>(
                DB2Access.create(JxBaseUtils.ASCII_CHARSET),
                in, null, ascii, array, JxBaseUtils.UTC_TIME_ZONE);
        final XBaseRecord record = reader.read();
        Assert.assertFalse(record.isDeleted());
        Assert.assertEquals("abc", record.getMap().get("y"));
    }

    @Test
    public void testGetBoolean() throws IOException {
        PowerMock.resetAll();

        final XBaseField<? super VisualFoxProAccess> field1 =
                TestHelper.fromStringRepresentation(this.dialect, "x,L,1,0");
        final XBaseField<? super VisualFoxProAccess> field2 =
                TestHelper.fromStringRepresentation(this.dialect, "y,L,1,0");
        final XBaseField<? super VisualFoxProAccess> field3 =
                TestHelper.fromStringRepresentation(this.dialect, "z,L,1,0");
        final byte[] recordBuffer = "tf0".getBytes(JxBaseUtils.ASCII_CHARSET);
        PowerMock.replayAll();

        final Boolean value1 = (Boolean) field1.extractValue(this.access, recordBuffer, 0);
        final Boolean value2 = (Boolean) field2.extractValue(this.access, recordBuffer, 1);
        final Boolean value3 = (Boolean) field3.extractValue(this.access, recordBuffer, 2);
        PowerMock.verifyAll();

        Assert.assertTrue(value1);
        Assert.assertFalse(value2);
        Assert.assertNull(value3);
    }

    @Test
    public void testVoidBoolean() {
        final VisualFoxProDialect thisDialect = this.dialect;
        final IllegalArgumentException e =
                Assert.assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        TestHelper.fromStringRepresentation(thisDialect, "x,L,0,0");
                    }
                });
        Assert.assertEquals(e.getMessage(), "A boolean has one char");
    }

    @Test
    public void testVoidDate() {
        final VisualFoxProDialect thisDialect = this.dialect;
        final IllegalArgumentException e =
                Assert.assertThrows(IllegalArgumentException.class, new ThrowingRunnable() {
                    @Override
                    public void run() {
                        TestHelper.fromStringRepresentation(thisDialect, "x,D,0,0");
                    }
                });
        Assert.assertEquals(e.getMessage(), "A date has 8 chars");
    }

    @Test
    public void testGetDate() throws IOException {
        final XBaseField<? super VisualFoxProAccess> field =
                TestHelper.fromStringRepresentation(this.dialect, "x,D,8,0");
        PowerMock.resetAll();
        PowerMock.replayAll();

        final byte[] buffer = "20100501".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Object value = field.extractValue(this.access, buffer, 0);
        PowerMock.verifyAll();

        final Calendar cal = Calendar.getInstance();
        cal.set(2010, Calendar.MAY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Assert.assertEquals(cal.getTime(), value);
    }

    @Test
    public void testGetInteger() throws IOException {
        final XBaseField<? super VisualFoxProAccess> field =
                TestHelper.fromStringRepresentation(this.dialect, "x,I,4,0");
        PowerMock.resetAll();
        PowerMock.replayAll();

        final byte[] buffer = "abcd".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Object value = field.extractValue(this.access, buffer, 0);
        PowerMock.verifyAll();

        Assert.assertEquals(1684234849L, value);
    }

    @Test
    public void testGetNullDouble() throws IOException {
        final XBaseField<? super VisualFoxProAccess> field =
                TestHelper.fromStringRepresentation(this.dialect, "x,B,8,0");
        PowerMock.resetAll();
        PowerMock.replayAll();

        final byte[] buffer = "abcdefgh".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Object value = field.extractValue(this.access, buffer, 0);
        PowerMock.verifyAll();

        Assert.assertEquals(1.2926117907728089E161, (Double) value, 0.01);
    }

    @Test
    public void testGetString() throws IOException {
        final XBaseField<? super VisualFoxProAccess> field =
                TestHelper.fromStringRepresentation(this.dialect, "x,C,4,0");
        PowerMock.resetAll();
        PowerMock.replayAll();

        final byte[] buffer = " bc ".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Object value = field.extractValue(this.access, buffer, 0);
        PowerMock.verifyAll();

        Assert.assertEquals("bc", (String) value);
    }

    @Test
    public void testMemoAsString() throws IOException {
        final TextMemoRecord mrec = PowerMock.createMock(TextMemoRecord.class);
        final XBaseField<? super VisualFoxProAccess> field =
                TestHelper.fromStringRepresentation(this.dialect, "y,M,4,4");
        PowerMock.resetAll();

        EasyMock.expect(this.mr.read(EasyMock.anyInt())).andReturn(mrec);
        EasyMock.expect(mrec.getValue()).andReturn("ok");
        PowerMock.replayAll();

        final byte[] buffer = "0123".getBytes(JxBaseUtils.ASCII_CHARSET);

        final Object rec = this.access.extractMemoValue(this.mr, buffer, 0, 4);
        final String value = ((TextMemoRecord) rec).getValue();
        PowerMock.verifyAll();

        Assert.assertTrue(field instanceof MemoField);
        Assert.assertEquals("ok", value);
    }

    @Test
    public void testNotDeleted() throws IOException {
        final Charset ascii = JxBaseUtils.ASCII_CHARSET;
        final InputStream in = new ByteArrayInputStream("abc".getBytes(ascii));
        final GenericFieldDescriptorArray<DB3Access> descriptorArray =
                new GenericFieldDescriptorArray<DB3Access>(
                        Collections.<XBaseField<? super DB3Access>>emptyList(), 0, 3);
        final DB3RecordReader<DB3Access> reader = new DB3RecordReader<DB3Access>(null, in,
                null, ascii,
                descriptorArray, null);
        final XBaseRecord record = reader.read();
        Assert.assertFalse(record.isDeleted());
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertEquals(new HashMap<String, Object>(), record.getMap());
    }

    @Test
    public void test() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("x", "0123456789".getBytes(JxBaseUtils.ASCII_CHARSET));
        final XBaseRecord record =
                new XBaseRecord(false, 1, map);
        Assert.assertArrayEquals("0123456789".getBytes(JxBaseUtils.ASCII_CHARSET),
                (byte[]) record.getMap().get("x"));
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertFalse(record.isDeleted());
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
