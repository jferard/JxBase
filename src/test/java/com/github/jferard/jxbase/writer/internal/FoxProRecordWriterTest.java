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

package com.github.jferard.jxbase.writer.internal;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.foxpro.VisualFoxProAccess;
import com.github.jferard.jxbase.dialect.foxpro.VisualFoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.VisualFoxProDialectFactory;
import com.github.jferard.jxbase.dialect.foxpro.memo.TextMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class FoxProRecordWriterTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private XBaseMemoWriter mw;
    private ByteArrayOutputStream bos;
    private VisualFoxProAccess access;
    private Map<String, Object> map;

    @Before
    public void setUp() throws IOException {
        this.mw = PowerMock.createMock(XBaseMemoWriter.class);
        this.bos = new ByteArrayOutputStream();
        final XBaseDialect<VisualFoxProDialect, VisualFoxProAccess> dialect =
                VisualFoxProDialectFactory.create(XBaseFileTypeEnum.VisualFoxPro, JxBaseUtils.UTF8_CHARSET,
                        TimeZone.getTimeZone("GMT")).writer("foo", new HashMap<String, Object>())
                        .build();
        this.access =
                dialect.getAccess();
        this.map = new HashMap<String, Object>();
        this.map.put("bool", true);
    }

    @Test
    public void writeLogicalValue() throws IOException {
        this.access.writeLogicalValue(this.bos, false);
        Assert.assertArrayEquals(new byte[]{'F'}, this.bos.toByteArray());
    }

    @Test
    public void writeMemoValue() throws IOException {
        final TextMemoRecord memo = new TextMemoRecord("memo", JxBaseUtils.UTF8_CHARSET);
        PowerMock.resetAll();

//        EasyMock.expect(this.mw.write(memo)).andReturn(10L);
        PowerMock.replayAll();

        this.access.writeMemoValue(this.bos, memo);
        PowerMock.verifyAll();

        Assert.assertArrayEquals(new byte[]{32, 32, 32, 32, 32, 32, 32, 32, 32, 49}, this.bos.toByteArray());
    }

    @Test
    public void writeCharacterValue() throws IOException {
        this.access.writeCharacterValue(this.bos, "é", 10);
        final String expectedString = "é        ";
        Assert.assertEquals(9, expectedString.length());
        final byte[] expected = expectedString.getBytes(JxBaseUtils.UTF8_CHARSET);
        Assert.assertEquals(10, expected.length);
        Assert.assertArrayEquals(expected, this.bos.toByteArray());
    }

    @Test
    public void writeDateValue() throws IOException {
        this.access.writeDateValue(this.bos, new Date(0));
        Assert.assertArrayEquals("19700101".getBytes(JxBaseUtils.ASCII_CHARSET),
                this.bos.toByteArray());
    }

    @Test
    public void writeNumericValue() throws IOException {
        this.access.writeNumericValue(this.bos, new BigDecimal("-1103.5"), 18, 3);
        Assert.assertArrayEquals("         -1103.500".getBytes(JxBaseUtils.ASCII_CHARSET),
                this.bos.toByteArray());
    }

    @Test
    public void writeNumericValueError() throws IOException {
        this.exception.expect(IllegalArgumentException.class);
        this.access.writeNumericValue(this.bos, new BigDecimal("-99999999999991103.5"), 18, 3);
    }

    @Test
    public void writeDatetimeValue() throws IOException {
        this.access.writeDatetimeValue(this.bos, new Date(0));
    }

    @Test
    public void writeIntegerValue() throws IOException {
        this.access.writeIntegerValue(this.bos, 31415L);
    }

    /*
    @Test
    public void getRecordQty() throws IOException {
        Assert.assertEquals(0, this.access.getRecordQty());
        this.access.write(this.map);
        Assert.assertEquals(1, this.access.getRecordQty());
        this.access.write(this.map);
        Assert.assertEquals(2, this.access.getRecordQty());
    }
     */
}