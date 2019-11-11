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

package com.github.jferard.jxbase.writer.internal;

import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.core.memo.TextMemoRecord;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.field.LogicalField;
import com.github.jferard.jxbase.core.field.XBaseField;
import com.github.jferard.jxbase.dialect.foxpro.FoxProRecordWriter;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.XBaseMemoWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GenericRecordWriterTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private XBaseMemoWriter mw;
    private ByteArrayOutputStream bos;
    private FoxProRecordWriter grw;
    private Map<String, Object> map;

    @Before
    public void setUp() {
        this.mw = Mockito.mock(XBaseMemoWriter.class);
        this.bos = new ByteArrayOutputStream();
        this.grw = new FoxProRecordWriter(new FoxProDialect(XBaseFileTypeEnum.dBASEIV1), this.bos,
                JxBaseUtils.UTF8_CHARSET,
                Collections.<XBaseField>singleton(new LogicalField("bool")), this.mw);
        this.map = new HashMap<String, Object>();
        this.map.put("bool", true);
    }

    @Test
    public void write() throws IOException {
        this.grw.write(this.map);
        Assert.assertArrayEquals(new byte[]{(byte) JxBaseUtils.EMPTY, 'T'}, this.bos.toByteArray());
    }

    @Test
    public void writeLogicalValue() throws IOException {
        this.grw.writeLogicalValue(false);
        Assert.assertArrayEquals(new byte[]{'F'}, this.bos.toByteArray());
    }

    @Test
    public void writeMemoValue() throws IOException {
        this.grw.writeSmallMemoValue(new TextMemoRecord("memo", 1, JxBaseUtils.UTF8_CHARSET));
        Assert.assertArrayEquals("         1".getBytes(JxBaseUtils.ASCII_CHARSET),
                this.bos.toByteArray());
    }

    @Test
    public void writeCharacterValue() throws IOException {
        this.grw.writeCharacterValue("é", 10);
        final String expectedString = "é        ";
        Assert.assertEquals(9, expectedString.length());
        final byte[] expected = expectedString.getBytes(JxBaseUtils.UTF8_CHARSET);
        Assert.assertEquals(10, expected.length);
        Assert.assertArrayEquals(expected, this.bos.toByteArray());
    }

    @Test
    public void writeDateValue() throws IOException {
        this.grw.writeDateValue(new Date(0));
        Assert.assertArrayEquals("19700101".getBytes(JxBaseUtils.ASCII_CHARSET), this.bos.toByteArray());
    }

    @Test
    public void writeNumericValue() throws IOException {
        this.grw.writeNumericValue(new BigDecimal("-1103.5"), 18, 3);
        Assert.assertArrayEquals("         -1103.500".getBytes(JxBaseUtils.ASCII_CHARSET), this.bos.toByteArray());
    }

    @Test
    public void writeNumericValueError() throws IOException {
        this.exception.expect(IllegalArgumentException.class);
        this.grw.writeNumericValue(new BigDecimal("-99999999999991103.5"), 18, 3);
    }

    @Test
    public void writeDatetimeValue() throws IOException {
        this.grw.writeDatetimeValue(new Date(0));
//        System.out.println(Arrays.toString(this.bos.toByteArray()));
    }

    @Test
    public void writeIntegerValue() throws IOException {
        System.out.println(Arrays.toString(this.bos.toByteArray()));
    }

    @Test
    public void getRecordQty() throws IOException {
        Assert.assertEquals(0, this.grw.getRecordQty());
        this.grw.write(this.map);
        Assert.assertEquals(1, this.grw.getRecordQty());
        this.grw.write(this.map);
        Assert.assertEquals(2, this.grw.getRecordQty());
    }
}