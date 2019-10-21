/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.core.DbfMetadata;
import com.github.jferard.jxbase.core.DbfRecord;
import com.github.jferard.jxbase.reader.DbfReader;
import com.github.jferard.jxbase.util.JdbfUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestDbfReader {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test1() throws IOException, ParseException {
        Charset stringCharset = Charset.forName("Cp866");

        InputStream dbf = getClass().getClassLoader().getResourceAsStream("data1/gds_im.dbf");

        DbfRecord rec;
        DbfReader reader = new DbfReader(dbf);
        try {
            DbfMetadata meta = reader.getMetadata();

            assertEquals(5, meta.getRecordsQty());
            assertEquals(28, meta.getFields().size());

            System.out.println("Read DBF Metadata: " + meta);
            int recCounter = 0;
            while ((rec = reader.read()) != null) {
                rec.setStringCharset(stringCharset);
                System.out.println("Record is DELETED: " + rec.isDeleted());
                System.out.println(rec.getRecordNumber());
                System.out.println(rec.toMap());

                recCounter++;
                assertEquals(recCounter, rec.getRecordNumber());
            }
        } finally {
            reader.close();
        }
    }

    @Test
    public void test2() throws IOException, ParseException {
        Charset stringCharset = Charset.forName("Cp866");

        InputStream dbf = getClass().getClassLoader().getResourceAsStream("data1/tir_im.dbf");

        DbfRecord rec;
        DbfReader reader = new DbfReader(dbf);
        try {
            DbfMetadata meta = reader.getMetadata();

            assertEquals(1, meta.getRecordsQty());
            assertEquals(117, meta.getFields().size());

            System.out.println("Read DBF Metadata: " + meta);
            int recCounter = 0;
            while ((rec = reader.read()) != null) {
                rec.setStringCharset(stringCharset);
                System.out.println("Record is DELETED: " + rec.isDeleted());
                System.out.println(rec.getRecordNumber());
                System.out.println(rec.toMap());

                recCounter++;
                assertEquals(recCounter, rec.getRecordNumber());
            }
        } finally {
            reader.close();
        }
    }

    @Test
    public void testEmptyStream() throws IOException {
        InputStream dbf = new ByteArrayInputStream(new byte[]{});
        exception.expect(IOException.class);
        exception.expectMessage("The file is corrupted or is not a dbf file");
        DbfReader reader = new DbfReader(dbf);
        try {
        } finally {
            reader.close();
        }
    }

    @Test
    public void testOneByteStreamWithGoodFileType() throws IOException {
        InputStream dbf = new ByteArrayInputStream(new byte[]{0x02});
        exception.expect(IOException.class);
        exception.expectMessage("The file is corrupted or is not a dbf file");
        DbfReader reader = new DbfReader(dbf);
        try {
        } finally {
            reader.close();
        }
    }

    @Test
    public void testOneByteStreamWithBadFileType() throws IOException {
        InputStream dbf = new ByteArrayInputStream(new byte[]{0x02});
        exception.expect(IOException.class);
        exception.expectMessage("The file is corrupted or is not a dbf file");
        DbfReader reader = new DbfReader(dbf);
        try {
        } finally {
            reader.close();
        }
    }

    @Test
    public void testSixteenByteStreamWithGoodFileType() throws IOException {
        InputStream dbf = new ByteArrayInputStream(
                new byte[]{0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2});
        exception.expect(IOException.class);
        exception.expectMessage("The file is corrupted or is not a dbf file");
        DbfReader reader = new DbfReader(dbf);
        try {
        } finally {
            reader.close();
        }
    }

    @Test
    public void testThirtyTwoByteStreamWithGoodFileType() throws IOException {
        InputStream dbf = new ByteArrayInputStream(
                new byte[]{0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2});
        exception.expect(IOException.class);
        exception.expectMessage("The file is corrupted or is not a dbf file");
        DbfReader reader = new DbfReader(dbf);
        try {
        } finally {
            reader.close();
        }
    }

    @Test
    public void testSixtyFourByteStreamWithGoodFileType() throws IOException {
        InputStream dbf = new ByteArrayInputStream(
                new byte[]{0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2});
        exception.expect(IOException.class);
        exception.expectMessage("The file is corrupted or is not a dbf file");
        DbfReader reader = new DbfReader(dbf);
        try {
        } finally {
            reader.close();
        }
    }

    @Test
    public void testSixtyFourByteStreamWithGoodFileTypeAndCloseHeader() throws IOException {
        InputStream dbf = new ByteArrayInputStream(
                new byte[]{0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2, 0x2, 0x2, 0x2, JdbfUtils.HEADER_TERMINATOR});
        DbfReader reader = new DbfReader(dbf);
        try {
            assertNull(reader.read());
        } finally {
            reader.close();
        }
    }
}
