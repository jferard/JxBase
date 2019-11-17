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

package com.github.jferard.jxbase.reader.internal;

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.XBaseRecord;
import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.foxpro.FoxProRecordReader;
import com.github.jferard.jxbase.dialect.foxpro.FoxProMemoRecordFactory;
import com.github.jferard.jxbase.memo.MemoRecordTypeEnum;
import com.github.jferard.jxbase.dialect.db3memo.DB3MemoDialect;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.field.LogicalField;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class GenericRecordReaderTest {
    private XBaseMemoReader mr;
    private ByteArrayInputStream bis;
    private FoxProRecordReader grr;

    @Before
    public void setUp() {
        this.mr = Mockito.mock(XBaseMemoReader.class);
        this.bis = new ByteArrayInputStream(" T".getBytes(JxBaseUtils.ASCII_CHARSET));
        this.grr = new FoxProRecordReader(new DB3MemoDialect(XBaseFileTypeEnum.dBASE4SQLTable), this.bis,
                JxBaseUtils.UTF8_CHARSET, new GenericFieldDescriptorArray(
                Collections.<XBaseField>singleton(new LogicalField("bool")), 11, 2), this.mr,
                TimeZone.getTimeZone("Europe/Paris"));
    }

    @Test
    public void read() throws IOException, ParseException {
        final XBaseRecord record = this.grr.read();
        Assert.assertEquals(1, record.getRecordNumber());
        Assert.assertFalse(record.isDeleted());
        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("bool", true);
        Assert.assertEquals(expected, record.getMap());
    }

    @Test
    public void getCharacterValue() {
        final String value =
                this.grr.getCharacterValue("abc".getBytes(JxBaseUtils.ASCII_CHARSET), 0, 2);
        Assert.assertEquals("ab", value);
    }

    @Test
    public void getTrimmedString() {
        final String s = this.grr.getTrimmedString(" é ".getBytes(JxBaseUtils.UTF8_CHARSET), 0, 4,
                JxBaseUtils.UTF8_CHARSET);
        Assert.assertEquals("é", s);
    }

    @Test
    public void getTrimmedASCIIString() {
        final String s = this.grr.getTrimmedString(" a ".getBytes(JxBaseUtils.ASCII_CHARSET), 0, 3,
                JxBaseUtils.ASCII_CHARSET);
        Assert.assertEquals("a", s);
    }

    @Test
    public void getDateValue() {
        final Date d = this.grr.getDateValue("19700101".getBytes(JxBaseUtils.ASCII_CHARSET), 0, 8);
        Assert.assertEquals(new Date(-3600000), d); // remove one hour
    }

    @Test
    public void getDatetimeValue() {
    }

    @Test
    public void getIntegerValue() {
        final long i = this.grr.getIntegerValue(new byte[]{0x01, 0x02, 0x03, 0x04}, 0, 4);
        Assert.assertEquals(67305985, i);
        Assert.assertEquals(((4 * 256 + 3) * 256 + 2) * 256 + 1, i); // little endian
    }

    @Test
    public void getLogicalValue() {
        final boolean bt = this.grr.getLogicalValue(new byte[]{'t'}, 0, 1);
        Assert.assertTrue(bt);
        final boolean bf = this.grr.getLogicalValue(new byte[]{'f'}, 0, 1);
        Assert.assertFalse(bf);
    }

    @Test
    public void getMemoValue() throws IOException {
        final FoxProMemoRecordFactory factory = new FoxProMemoRecordFactory(JxBaseUtils.UTF8_CHARSET);
        final XBaseMemoRecord record =
                factory.create(new byte[]{'A'}, MemoRecordTypeEnum.IMAGE, 1, 123);
        Mockito.when(this.mr.read(123)).thenReturn(record);

        final XBaseMemoRecord m =
                this.grr.getSmallMemoValue("       123".getBytes(JxBaseUtils.ASCII_CHARSET), 0, 10);

        Assert.assertEquals(record, m);
    }

    @Test
    public void getNumericValue() {
        final BigDecimal n =
                this.grr.getNumericValue("   -13.8".getBytes(JxBaseUtils.ASCII_CHARSET), 0, 8, 4);
        Assert.assertEquals("-13.8", n.toString());
    }

    @Test
    public void close() {
    }
}