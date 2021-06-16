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

package com.github.jferard.jxbase.dialect.db3.field;

import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

public class MemoFieldTest {
    private MemoAccess memoAccess;
    private MemoField mf;

    @Before
    public void setUp() {
        this.mf = new MemoField("memo");
        this.memoAccess = PowerMock.createMock(MemoAccess.class);
    }

    @Test
    public void getMemoName() {
        Assert.assertEquals("memo", this.mf.getName());
    }

    @Test
    public void getMemoByteLength() {
        PowerMock.resetAll();

        EasyMock.expect(this.memoAccess.getMemoValueLength()).andReturn(10);
        PowerMock.replayAll();

        final DB3Access access = new DB3Access(null, null, null, null, this.memoAccess);
        final int valueByteLength = this.mf.getValueLength(access);
        PowerMock.verifyAll();

        Assert.assertEquals(10, valueByteLength);
    }

//    @Test
//    public void getMemoValue() throws IOException {
//        final MemoAccess memoAccess = PowerMock.createMock(MemoAccess.class);
//        final XBaseMemoReader memoReader = PowerMock.createMock(XBaseMemoReader.class);
//        final DB3Access access = new DB3Access(null, null, null, null, memoAccess);
//        final byte[] bytes = {1, 2, 3, 4};
//        final TextMemoRecord record = new TextMemoRecord("a", JxBaseUtils.ASCII_CHARSET);
//        PowerMock.resetAll();
//
//        EasyMock.expect(memoAccess.extractMemoValue(memoReader, bytes, 0, 4)).andReturn(record);
//        PowerMock.replayAll();
//
//        final XBaseMemoRecord memoRecord = this.mf.extractValue(access, bytes, 0, 4);
//        PowerMock.verifyAll();
//
//        Assert.assertEquals(record, memoRecord);
//    }

//    @Test
//    public void writeMemoValue() throws IOException {
//        final MemoAccess memoAccess = PowerMock.createMock(MemoAccess.class);
//        final XBaseMemoWriter memoWriter = PowerMock.createMock(XBaseMemoWriter.class);
//        final DB3Access access = new DB3Access(null, null, null, null, memoAccess);
//        final TextMemoRecord record = new TextMemoRecord("a", JxBaseUtils.ASCII_CHARSET);
//        final ByteArrayOutputStream out = new ByteArrayOutputStream();
//        PowerMock.resetAll();
//
//        EasyMock.expect(memoAccess.writeMemoValue(memoWriter, record)).andReturn(10L);
//        memoAccess.writeMemoAddress(out, 10L);
//        PowerMock.replayAll();
//
//        this.mf.writeValue(access, out, record);
//        PowerMock.verifyAll();
//    }

//    @Test
//    public void writeNullMemoValue() throws IOException {
//        final XBaseMemoReader reader = PowerMock.createMock(XBaseMemoReader.class);
//        final XBaseMemoWriter writer = PowerMock.createMock(XBaseMemoWriter.class);
//        final RawRecordReadHelper readHelper = PowerMock.createMock(RawRecordReadHelper.class);
//        final MemoAccess memoAccess = new DB3MemoAccess(reader, writer);
//        final DB3Access access = new DB3Access(null, null, null, null, memoAccess);
//        final ByteArrayOutputStream out = new ByteArrayOutputStream();
//        PowerMock.resetAll();
//
//        PowerMock.replayAll();
//
//        this.mf.writeValue(access, out, null);
//        PowerMock.verifyAll();
//
//        Assert.assertArrayEquals(
//                new byte[]{0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20},
//                out.toByteArray());
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
    public void testToStringRepresentation() {
        final XBaseMemoReader reader = PowerMock.createMock(XBaseMemoReader.class);
        final XBaseMemoWriter writer = PowerMock.createMock(XBaseMemoWriter.class);
        final RawRecordReadHelper readHelper = PowerMock.createMock(RawRecordReadHelper.class);
        final MemoAccess memoAccess = new DB3MemoAccess();
        PowerMock.resetAll();
        PowerMock.replayAll();

        Assert.assertEquals("memo,M,10,0", this.mf.toStringRepresentation(memoAccess));
        PowerMock.verifyAll();
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(3347770, this.mf.hashCode());
        Assert.assertEquals(this.mf, this.mf);
        Assert.assertNotEquals(this.mf, new Object());
        final MemoField f2 = new MemoField("d");
        Assert.assertNotEquals(this.mf, f2);
        final MemoField f3 = new MemoField("memo");
        Assert.assertEquals(this.mf, f3);
    }
}
