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

package com.github.jferard.jxbase.dialect.db3;

import com.github.jferard.jxbase.dialect.db3.field.DateField;
import com.github.jferard.jxbase.dialect.db3.field.MemoAccess;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.foxpro.TextMemoRecord;
import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.TimeZone;

public class DB3AccessTest {
    private CDLMNFieldsAccess access;
    private DateField df;
    private MemoField mf;

    @Before
    public void setUp() throws Exception {
        final FileChannel channel = Mockito.mock(FileChannel.class);
        this.access =
                new DB3DialectFactory(null, JxBaseUtils.ASCII_CHARSET, TimeZone.getTimeZone("UTC"))
                        .build().getAccess();
        this.df = new DateField("date");
        this.mf = new MemoField("memo");

    }

    @Test
    public void getDateName() {
        Assert.assertEquals("date", this.df.getName());
    }

    @Test
    public void getDateByteLength() {
        Assert.assertEquals(8, this.df.getValueByteLength(this.access));
    }

    @Test
    public void getDateValue() throws IOException {
        final byte[] bytes = "19700101".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Date date = new Date(0);
        Assert.assertEquals(date, this.df.getValue(this.access, bytes, 0, 8));
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
        final MemoAccess memoAccess = Mockito.mock(MemoAccess.class);
        Mockito.when(memoAccess.getMemoValueLength()).thenReturn(10);
        final DB3Access access = new DB3Access(null, null, null, memoAccess, null);
        Assert.assertEquals(10, this.mf.getValueByteLength(access));
    }

    @Test
    public void getMemoValue() throws IOException {
        final MemoAccess memoAccess = Mockito.mock(MemoAccess.class);
        Mockito.when(memoAccess.getMemoValueLength()).thenReturn(10);
        final DB3Access access = new DB3Access(null, null, null, memoAccess, null);
        final byte[] bytes = {1, 2, 3, 4};
        final TextMemoRecord record = new TextMemoRecord("a", JxBaseUtils.ASCII_CHARSET);
        Mockito.when(memoAccess.getMemoValue(bytes, 0, 4)).thenReturn(record);
        Assert.assertEquals(record, this.mf.getValue(access, bytes, 0, 4));
    }

    @Test
    public void writeMemoValue() throws IOException {
        final MemoAccess memoAccess = Mockito.mock(MemoAccess.class);
        Mockito.when(memoAccess.getMemoValueLength()).thenReturn(10);
        final DB3Access access = new DB3Access(null, null, null, memoAccess, null);
        final TextMemoRecord record = new TextMemoRecord("a", JxBaseUtils.ASCII_CHARSET);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        this.mf.writeValue(access, out, record);
        Mockito.verify(memoAccess).writeMemoValue(out, record);
    }

    @Test
    public void toMemoStringRepresentation() {
        final MemoAccess memoAccess = Mockito.mock(MemoAccess.class);
        Mockito.when(memoAccess.getMemoFieldRepresentation("memo"))
                .thenReturn(new FieldRepresentation("memo", 'M', 10, 0));
        final DB3Access access = new DB3Access(null, null, null, memoAccess, null);
        Assert.assertEquals("memo,M,10,0", this.mf.toStringRepresentation(access));
    }

    @Test
    public void testMemoToString() {
        Assert.assertEquals("MemoField[name=memo]", this.mf.toString());
    }
}