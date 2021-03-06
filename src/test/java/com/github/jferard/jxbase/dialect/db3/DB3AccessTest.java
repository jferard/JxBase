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

package com.github.jferard.jxbase.dialect.db3;

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db3.field.DB3MemoAccess;
import com.github.jferard.jxbase.dialect.db3.field.DateField;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.foxpro.memo.TextMemoRecord;
import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RawRecordReadHelper.class)
public class DB3AccessTest {
    private CDLMNFieldsAccess access;
    private DateField df;
    private MemoField mf;
    private XBaseMemoReader memoReader;

    @Before
    public void setUp() {
        this.memoReader = PowerMock.createMock(XBaseMemoReader.class);
        this.access =
                DB3DialectFactory
                        .create(XBaseFileTypeEnum.dBASE3plusMemo, JxBaseUtils.ASCII_CHARSET,
                                JxBaseUtils.UTC_TIME_ZONE).getAccess();
        this.df = new DateField("date");
        this.mf = new MemoField("memo");

    }

    @Test
    public void getDateName() {
        Assert.assertEquals("date", this.df.getName());
    }

    @Test
    public void getDateByteLength() {
        Assert.assertEquals(8, this.df.getValueLength(this.access));
    }

    @Test
    public void getDateValue() throws IOException {
        final byte[] bytes = "19700101".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Date date = new Date(0);
        Assert.assertEquals(date, this.df.extractValue(this.access, bytes, 0));
    }

    @Test
    public void writeDateValue() throws IOException {
        final Date value = new Date(0);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.df.writeValue(this.access, out, value);
        Assert.assertEquals("19700101", out.toString(String.valueOf(JxBaseUtils.ASCII_CHARSET)));
    }

    @Test
    public void toDateStringRepresentation() {
        Assert.assertEquals("date,D,8,0", this.df.toStringRepresentation(this.access));
    }

    @Test
    public void testDateToString() {
        Assert.assertEquals("DateField[name=date]", this.df.toString());
    }

    @Test
    public void getMemoName() {
        Assert.assertEquals("memo", this.mf.getName());
    }

    @Test
    public void getMemoByteLength() {
        final MemoAccess memoAccess = PowerMock.createMock(MemoAccess.class);
        PowerMock.resetAll();

        EasyMock.expect(memoAccess.getMemoValueLength()).andReturn(10);
        PowerMock.replayAll();

        final DB3Access access = new DB3Access(null, null, null, null, memoAccess);
        final int valueByteLength = this.mf.getValueLength(access);
        PowerMock.verifyAll();

        Assert.assertEquals(10, valueByteLength);
    }

    @Test
    public void getMemoValue() throws IOException {
        final MemoAccess memoAccess = PowerMock.createMock(MemoAccess.class);
        final DB3Access access = new DB3Access(null, null, null, null, memoAccess);
        final byte[] bytes = {1, 2, 3, 4};
        final TextMemoRecord record = new TextMemoRecord("a", JxBaseUtils.ASCII_CHARSET);
        PowerMock.resetAll();

        EasyMock.expect(memoAccess.extractMemoValue(this.memoReader, bytes, 0, 4)).andReturn(record);
        PowerMock.replayAll();

        final XBaseMemoRecord memoRecord = access.extractMemoValue(this.memoReader, bytes, 0, 4);
        PowerMock.verifyAll();

        Assert.assertEquals(record, memoRecord);
    }

    @Test
    public void getMemoValueOffset0() throws IOException {
        final XBaseMemoReader reader = PowerMock.createMock(XBaseMemoReader.class);
        final XBaseMemoWriter writer = PowerMock.createMock(XBaseMemoWriter.class);
        final byte[] bytes = "   0".getBytes(JxBaseUtils.ASCII_CHARSET);
        PowerMock.resetAll();
        PowerMock.replayAll();

        final DB3MemoAccess access = new DB3MemoAccess();
        final XBaseMemoRecord value = access.extractMemoValue(memoReader, bytes, 0, 4);
        PowerMock.verifyAll();

        Assert.assertNull(value);
    }

    @Test
    public void getMemoValueOffsetNull() throws IOException {
        final XBaseMemoReader reader = PowerMock.createMock(XBaseMemoReader.class);
        final XBaseMemoWriter writer = PowerMock.createMock(XBaseMemoWriter.class);
        final byte[] bytes = "    ".getBytes(JxBaseUtils.ASCII_CHARSET);
        PowerMock.resetAll();
        PowerMock.replayAll();

        final DB3MemoAccess access = new DB3MemoAccess();
        final XBaseMemoRecord value = access.extractMemoValue(memoReader, bytes, 0, 4);
        PowerMock.verifyAll();

        Assert.assertNull(value);
    }

//    @Test
//    public void writeMemoValue() throws IOException {
//        final MemoAccess memoAccess = PowerMock.createMock(MemoAccess.class);
//        final XBaseMemoWriter memoWriter = PowerMock.createMock(XBaseMemoWriter.class);
//
//        final DB3Access access = new DB3Access(null, null, null, null, memoAccess);
//        final TextMemoRecord record = new TextMemoRecord("a", JxBaseUtils.ASCII_CHARSET);
//        final ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        PowerMock.resetAll();
//
//        EasyMock.expect(memoAccess.writeMemoValue(memoWriter, record)).andReturn(10L);
//        memoAccess.writeMemoAddress(out, 10L);
//        PowerMock.replayAll();
//
//        this.mf.writeValue(access, out, record);
//        PowerMock.verifyAll();
//    }

    @Test
    public void toMemoStringRepresentation() {
        final MemoAccess memoAccess = PowerMock.createMock(MemoAccess.class);
        PowerMock.resetAll();

        EasyMock.expect(memoAccess.getMemoFieldRepresentation("memo"))
                .andReturn(new FieldRepresentation("memo", 'M', 10, 0));
        PowerMock.replayAll();

        final DB3Access access = new DB3Access(null, null, null, null, memoAccess);
        final String s = this.mf.toStringRepresentation(access);
        PowerMock.verifyAll();

        Assert.assertEquals("memo,M,10,0", s);
    }

    @Test
    public void testMemoToString() {
        Assert.assertEquals("MemoField[name=memo]", this.mf.toString());
    }

    @Test
    public void testGetMemoLength() {
        Assert.assertEquals(10, this.mf.getValueLength(this.access));
    }
}