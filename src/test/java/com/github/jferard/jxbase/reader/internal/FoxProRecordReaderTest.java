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

package com.github.jferard.jxbase.geter.internal;

import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.foxpro.FoxProAccess;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialectFactory;
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

public class FoxProRecordReaderTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private XBaseMemoWriter mw;
    private ByteArrayOutputStream bos;
    private FoxProAccess access;
    private Map<String, Object> map;

    @Before
    public void setUp() throws IOException {
        this.mw = PowerMock.createMock(XBaseMemoWriter.class);
        this.bos = new ByteArrayOutputStream();
        final XBaseDialect<FoxProDialect, FoxProAccess> dialect =
                FoxProDialectFactory.create(XBaseFileTypeEnum.VisualFoxPro, JxBaseUtils.UTF8_CHARSET,
                        TimeZone.getTimeZone("GMT")).reader("foo")
                        .build();
        this.access =
                dialect.getAccess();
        this.map = new HashMap<String, Object>();
        this.map.put("bool", true);
    }

    @Test
    public void getLogicalValue() throws IOException {
        Assert.assertFalse(this.access.extractLogicalValue(new byte[]{'F'}, 0, 1));
    }

//    @Test
//    public void getMemoValue() throws IOException {
//        final TextMemoRecord memo = new TextMemoRecord("memo", JxBaseUtils.UTF8_CHARSET);
//        PowerMock.resetAll();
//
////        EasyMock.expect(this.mw.get(memo)).andReturn(10L);
//        PowerMock.replayAll();
//
//        this.access.getMemoValue(this.bos, memo);
//        PowerMock.verifyAll();
//
//        Assert.assertArrayEquals(new byte[]{32, 32, 32, 32, 32, 32, 32, 32, 32, 49}, this.bos.toByteArray());
//    }
//
//    @Test
//    public void getCharacterValue() throws IOException {
//        this.access.extractCharacterValue(this.bos, "é", 10);
//        final String expectedString = "é        ";
//        Assert.assertEquals(9, expectedString.length());
//        final byte[] expected = expectedString.getBytes(JxBaseUtils.UTF8_CHARSET);
//        Assert.assertEquals(10, expected.length);
//        Assert.assertArrayEquals(expected, this.bos.toByteArray());
//    }
//
//    @Test
//    public void getDateValue() throws IOException {
//        this.access.getDateValue(this.bos, new Date(0));
//        Assert.assertArrayEquals("19700101".getBytes(JxBaseUtils.ASCII_CHARSET),
//                this.bos.toByteArray());
//    }
//
//    @Test
//    public void getNumericValue() throws IOException {
//        this.access.getNumericValue(this.bos, new BigDecimal("-1103.5"), 18, 3);
//        Assert.assertArrayEquals("         -1103.500".getBytes(JxBaseUtils.ASCII_CHARSET),
//                this.bos.toByteArray());
//    }
//
//    @Test
//    public void getNumericValueError() throws IOException {
//        this.exception.expect(IllegalArgumentException.class);
//        this.access.getNumericValue(this.bos, new BigDecimal("-99999999999991103.5"), 18, 3);
//    }
//
//    @Test
//    public void getDatetimeValue() throws IOException {
//        this.access.getDatetimeValue(this.bos, new Date(0));
//    }
//
//    @Test
//    public void getIntegerValue() throws IOException {
//        this.access.getIntegerValue(this.bos, 31415L);
//    }
//
//    /*
//    @Test
//    public void getRecordQty() throws IOException {
//        Assert.assertEquals(0, this.access.getRecordQty());
//        this.access.get(this.map);
//        Assert.assertEquals(1, this.access.getRecordQty());
//        this.access.get(this.map);
//        Assert.assertEquals(2, this.access.getRecordQty());
//    }
//     */
}