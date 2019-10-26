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

package com.github.jferard.jxbase.writer;

import com.github.jferard.jxbase.core.DbfField;
import com.github.jferard.jxbase.core.DbfFileTypeEnum;
import com.github.jferard.jxbase.core.DbfMetadata;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DbfWriterTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void TestWriteCNFD() throws IOException {
        final DbfField f1 = DbfField.fromStringRepresentation("x,C,1,2");
        f1.setOffset(0);
        final DbfField f2 = DbfField.fromStringRepresentation("y,N,4,2");
        f2.setOffset(1);
        final DbfField f3 = DbfField.fromStringRepresentation("z,F,10,2");
        f3.setOffset(9);
        final DbfField f4 = DbfField.fromStringRepresentation("t,D,6,2");
        f4.setOffset(13);

        DbfMetadata md = Mockito.mock(DbfMetadata.class);
        Mockito.when(md.getUpdateDate()).thenReturn(new Date(119,9,25));
        Mockito.when(md.getFields()).thenReturn(Arrays.asList(f1, f2, f3, f4));
        Mockito.when(md.getOneRecordLength()).thenReturn(24);
        Mockito.when(md.getFileType()).thenReturn(DbfFileTypeEnum.dBASEVII1);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DbfWriter w = new DbfWriter(md, bos);
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", "X");
        m1.put("y", new BigDecimal(10));
        m1.put("z", new Float(16));
        m1.put("t", new Date(0));
        w.write(m1);
        Assert.assertArrayEquals(
                new byte[]{68, 19, 10, 5, 0, 0, 0, 0, 0, 0, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 67, 0, 0, 0,
                        0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 121, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 78, 1, 0, 0, 0, 4, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        122, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 9, 0, 0, 0, 10, 2, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 116, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 68, 13, 0, 0, 0,
                        6, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 88, 49, 48, 32, 32, 32,
                        32, 32, 32, 32, 49, 54, 44, 49, 57, 55, 48, 48, 49, 48, 49, 32, 32, 32},
                bos.toByteArray());
    }

    @Test
    public void TestWriteY() throws IOException {
        testWriteUnsupported("x,Y,1,2", "Unknown or unsupported field type Currency for");
    }

    @Test
    public void TestWriteTABO() throws IOException {
        final DbfField f1 = DbfField.fromStringRepresentation("x,T,1,2");
        f1.setOffset(0);
        final DbfField f2 = DbfField.fromStringRepresentation("y,@,10,2");
        f2.setOffset(1);
        final DbfField f3 = DbfField.fromStringRepresentation("z,B,4,2");
        f3.setOffset(9);
        final DbfField f4 = DbfField.fromStringRepresentation("t,O,6,2");
        f4.setOffset(13);

        DbfMetadata md = Mockito.mock(DbfMetadata.class);
        Mockito.when(md.getUpdateDate()).thenReturn(new Date(119,9,25));
        Mockito.when(md.getFields()).thenReturn(Arrays.asList(f1, f2, f3, f4));
        Mockito.when(md.getOneRecordLength()).thenReturn(30);
        Mockito.when(md.getFileType()).thenReturn(DbfFileTypeEnum.dBASEVII1);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DbfWriter w = new DbfWriter(md, bos);
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", new Date(0));
        m1.put("y", new Date(5));
        m1.put("z", new Double(1.5));
        m1.put("t", new Double(1.5));
        w.write(m1);
        Assert.assertArrayEquals(
                new byte[]{68, 19, 10, 5, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 84, 0, 0, 0,
                        0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 121, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 64, 1, 0, 0, 0, 10, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        122, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 66, 9, 0, 0, 0, 4, 2, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 116, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 79, 13, 0, 0, 0,
                        6, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 0, 0, 0, 99, -1, 0, 54,
                        -18, -128, 63, -8, 0, 0, 63, -8, 0, 0, 0, 0, 0, 0, 32, 32, 32, 32, 32, 32,
                        32, 32, 32}, bos.toByteArray());
    }

    @Test
    public void TestWriteM() throws IOException {
        testWriteUnsupported("x,M,1,2", "Unknown or unsupported field type Memo for");
        return;
    }

    @Test
    public void TestWriteG() throws IOException {
        testWriteUnsupported("x,G,1,2", "Unknown or unsupported field type General for");
    }

    @Test
    public void TestWriteP() throws IOException {
        testWriteUnsupported("x,P,1,2", "Unknown or unsupported field type Picture for");
    }

    @Test
    public void TestWriteZ() throws IOException {
        testWriteUnsupported("x,0,1,2", "Unknown or unsupported field type NullFlags for");
    }

    @Test
    public void TestWriteILFDoubleNull() throws IOException {
        final DbfField f1 = DbfField.fromStringRepresentation("x,I,1,2");
        f1.setOffset(0);
        final DbfField f2 = DbfField.fromStringRepresentation("y,L,10,2");
        f2.setOffset(1);
        final DbfField f3 = DbfField.fromStringRepresentation("z,F,10,2");
        f3.setOffset(2);
        final DbfField f4 = DbfField.fromStringRepresentation("t,F,10,2");
        f4.setOffset(3);

        DbfMetadata md = Mockito.mock(DbfMetadata.class);
        Mockito.when(md.getUpdateDate()).thenReturn(new Date(119,9,25));
        Mockito.when(md.getFields()).thenReturn(Arrays.asList(f1, f2, f3, f4));
        Mockito.when(md.getOneRecordLength()).thenReturn(30);
        Mockito.when(md.getFileType()).thenReturn(DbfFileTypeEnum.dBASEVII1);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DbfWriter w = new DbfWriter(md, bos);
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", 5);
        m1.put("y", true);
        m1.put("z", new Double(15.6));
        m1.put("t", null);
        w.write(m1);
        Assert.assertArrayEquals(
                new byte[]{68, 19, 10, 5, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 73, 0, 0, 0,
                        0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 121, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 76, 1, 0, 0, 0, 10, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        122, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 2, 0, 0, 0, 10, 2, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 116, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 70, 3, 0, 0, 0,
                        10, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 0, 84, 32, 49, 53, 44,
                        54, 48, 48, 48, 48, 48, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
                        32, 32, 32, 32, 32}, bos.toByteArray());
    }

    private void testWriteUnsupported(String ch, String error) throws IOException {
        final DbfField f1 = DbfField.fromStringRepresentation(ch);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        DbfMetadata md = Mockito.mock(DbfMetadata.class);
        Mockito.when(md.getFields()).thenReturn(Arrays.asList(f1));
        Mockito.when(md.getFileType()).thenReturn(DbfFileTypeEnum.dBASEVII1);

        DbfWriter w = new DbfWriter(md, bos);
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", "X");

        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage(error);
        w.write(m1);
    }

    @Test
    public void TestBigDecimal() throws IOException {
        final DbfField f1 = DbfField.fromStringRepresentation("x,N,1,2");
        f1.setOffset(0);

        DbfMetadata md = Mockito.mock(DbfMetadata.class);
        Mockito.when(md.getUpdateDate()).thenReturn(new Date(119,9,25));
        Mockito.when(md.getFields()).thenReturn(Arrays.asList(f1));
        Mockito.when(md.getOneRecordLength()).thenReturn(30);
        Mockito.when(md.getFileType()).thenReturn(DbfFileTypeEnum.dBASEVII1);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DbfWriter w = new DbfWriter(md, bos);
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", new BigDecimal(1234465646488L));
        w.write(m1);
        w.close();
        Assert.assertArrayEquals(
                new byte[]{68, 19, 10, 5, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 78, 0, 0, 0,
                        0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, 49, 32, 32, 32, 32,
                        32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
                        32, 32, 32, 32, 32, 32}, bos.toByteArray());
    }

    @Test
    public void setStringCharset() throws IOException {
        final DbfField f1 = DbfField.fromStringRepresentation("x,C,1,2");
        f1.setOffset(0);

        DbfMetadata md = Mockito.mock(DbfMetadata.class);
        Mockito.when(md.getUpdateDate()).thenReturn(new Date(119,9,25));
        Mockito.when(md.getFields()).thenReturn(Arrays.asList(f1));
        Mockito.when(md.getOneRecordLength()).thenReturn(30);
        Mockito.when(md.getFileType()).thenReturn(DbfFileTypeEnum.dBASEVII1);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DbfWriter w = new DbfWriter(md, bos);
        w.setStringCharset("ISO-8859-1");
        Map<String, Object> m1 = new HashMap<String, Object>();
        m1.put("x", "é");
        w.write(m1);

        Assert.assertArrayEquals(
                new byte[]{68, 19, 10, 5, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 67, 0, 0, 0,
                        0, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13, -23, 32, 32, 32, 32,
                        32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32,
                        32, 32, 32, 32, 32, 32}, bos.toByteArray());
    }
}