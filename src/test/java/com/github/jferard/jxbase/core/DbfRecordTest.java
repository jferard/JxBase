/*
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

import com.github.jferard.jxbase.reader.MemoReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DbfRecordTest {
    private static final Charset ASCII = Charset.forName("ASCII");
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
    public void testStringCharset() {
        DbfRecord record = new DbfRecord("abc".getBytes(ASCII), md, mr, -1);
        record.setStringCharset(ASCII);
        Assert.assertEquals(ASCII, record.getStringCharset());
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
        Mockito.when(md.getField("x")).thenReturn(DbfField.fromStringRepresentation("y,I,3,4"));

        DbfRecord record = new DbfRecord("abc".getBytes(ASCII), md, mr, -1);
        record.setStringCharset(ASCII);
        Assert.assertEquals("abc", record.getString("x"));
    }

    @Test
    public void testMemoAsString() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.when(md.getField("x")).thenReturn(DbfField.fromStringRepresentation("y,M,4,4"));

        DbfRecord record = new DbfRecord("abcd".getBytes(ASCII), md, mr, 1);
        record.setStringCharset(ASCII);
        Assert.assertEquals("ok", record.getMemoAsString("x"));
    }

    @Test
    public void testMemoAsBytes() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.when(md.getField("x")).thenReturn(DbfField.fromStringRepresentation("y,M,4,4"));

        DbfRecord record = new DbfRecord("abcd".getBytes(ASCII), md, mr, 1);
        record.setStringCharset(ASCII);
        Assert.assertArrayEquals(null, record.getMemoAsBytes("x"));
    }

    @Test
    public void testMap() throws Exception {
        final DbfField f1 = DbfField.fromStringRepresentation("x,C,1,2");
        f1.setOffset(0);
        final DbfField f2 = DbfField.fromStringRepresentation("y,D,8,2");
        f2.setOffset(1);
        final DbfField f3 = DbfField.fromStringRepresentation("z,N,1,2");
        f3.setOffset(9);
        final DbfField f4 = DbfField.fromStringRepresentation("t,L,1,2");
        f4.setOffset(10);

        Mockito.when(md.getFields()).thenReturn(Arrays.asList(f1, f2, f3, f4));
        Mockito.when(md.getField("x")).thenReturn(f1);
        Mockito.when(md.getField("y")).thenReturn(f2);
        Mockito.when(md.getField("z")).thenReturn(f3);
        Mockito.when(md.getField("t")).thenReturn(f4);

        DbfRecord record = new DbfRecord("a201005018t".getBytes(ASCII), md, mr, 1);
        record.setStringCharset(ASCII);
        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("x", "a");
        expected.put("y", new Date(110, 4, 1));
        expected.put("z", new BigDecimal(8));
        expected.put("t", true);
        Assert.assertEquals(expected, record.toMap());
    }

    @Test
    public void testStringRepresentation() throws Exception {
        final DbfField f1 = DbfField.fromStringRepresentation("x,C,1,2");
        f1.setOffset(0);
        final DbfField f2 = DbfField.fromStringRepresentation("y,D,8,2");
        f2.setOffset(1);
        final DbfField f3 = DbfField.fromStringRepresentation("z,N,1,2");
        f3.setOffset(9);
        final DbfField f4 = DbfField.fromStringRepresentation("t,L,1,2");
        f4.setOffset(10);

        Mockito.when(md.getFields()).thenReturn(Arrays.asList(f1, f2, f3, f4));
        Mockito.when(md.getField("x")).thenReturn(f1);
        Mockito.when(md.getField("y")).thenReturn(f2);
        Mockito.when(md.getField("z")).thenReturn(f3);
        Mockito.when(md.getField("t")).thenReturn(f4);


        DbfRecord record = new DbfRecord("a201005018t".getBytes(ASCII), md, mr, 1);
        record.setStringCharset(ASCII);
        Assert.assertEquals("x=a, y=Sat May 01 00:00:00 CEST 2010, z=8, t=true, ",
                record.getStringRepresentation());
    }

    @Test
    public void testGetBytes() throws IOException {
        DbfRecord record = new DbfRecord("abcd".getBytes(ASCII), md, mr, 1);
        Assert.assertArrayEquals(new byte[]{97, 98, 99, 100}, record.getBytes());
    }

    @Test
    public void testGetBoolean() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final DbfField ft = DbfField.fromStringRepresentation("x,L,1,0");
        final DbfField ff = DbfField.fromStringRepresentation("y,L,1,0");
        ff.setOffset(1);

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.when(md.getField("x")).thenReturn(ft);
        Mockito.when(md.getField("y")).thenReturn(ff);

        DbfRecord record = new DbfRecord("tf".getBytes(ASCII), md, mr, 2);
        Assert.assertTrue(record.getBoolean("x"));
        Assert.assertFalse(record.getBoolean("y"));
    }

    @Test
    public void testGetInteger() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final DbfField f = DbfField.fromStringRepresentation("x,I,4,0");

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.when(md.getField("x")).thenReturn(f);

        DbfRecord record = new DbfRecord("abcd".getBytes(ASCII), md, mr, 1);
        Assert.assertEquals(Integer.valueOf(1684234849), record.getInteger("x"));
    }

    @Test
    public void testFieldBytes() throws IOException {
        MemoRecord mrec = Mockito.mock(MemoRecord.class);
        final DbfField f = DbfField.fromStringRepresentation("x,I,4,0");

        Mockito.when(mr.read(Mockito.anyInt())).thenReturn(mrec);
        Mockito.when(mrec.getValueAsString(ASCII)).thenReturn("ok");
        Mockito.when(md.getField("x")).thenReturn(f);

        DbfRecord record = new DbfRecord("abcd".getBytes(ASCII), md, mr, 1);
        System.out.println(Arrays.toString(record.getBytes("x")));
        Assert.assertArrayEquals(new byte[] {97, 98, 99, 100}, record.getBytes("x"));
    }
}
