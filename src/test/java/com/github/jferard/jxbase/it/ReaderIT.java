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

package com.github.jferard.jxbase.it;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.XBaseReader;
import com.github.jferard.jxbase.XBaseReaderFactory;
import com.github.jferard.jxbase.core.XBaseRecord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class ReaderIT {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void test1() throws IOException, ParseException {
        final Charset stringCharset = Charset.forName("Cp866");

        final String databaseName = TestHelper.getResourceBase("data1/gds_im.dbf");

        XBaseRecord rec;
        final XBaseReader reader = XBaseReaderFactory.createReader(databaseName, stringCharset);
        try {
            final XBaseMetadata meta = reader.getMetadata();

            assertEquals(5, meta.get("recordsQty"));
            assertEquals(28, reader.getFieldDescriptorArray().getFields().size());

            System.out.println("Read DBF Metadata: " + meta);
            int recCounter = 0;
            while ((rec = reader.read()) != null) {
                System.out.println("Record is DELETED: " + rec.isDeleted());
                System.out.println(rec.getRecordNumber());
                System.out.println(rec.getMap());

                recCounter++;
                assertEquals(recCounter, rec.getRecordNumber());
            }
        } finally {
            reader.close();
        }
    }

    @Test
    public void test2() throws IOException, ParseException {
        final Charset stringCharset = Charset.forName("Cp866");

        final String databaseName = TestHelper.getResourceBase("data1/tir_im.dbf");

        XBaseRecord rec;
        final XBaseReader reader = XBaseReaderFactory.createReader(databaseName, stringCharset);
        try {
            final XBaseMetadata meta = reader.getMetadata();

            assertEquals(1, meta.get("recordsQty"));
            assertEquals(117, reader.getFieldDescriptorArray().getFields().size());

            System.out.println("Read DBF Metadata: " + meta);
            int recCounter = 0;
            while ((rec = reader.read()) != null) {
                System.out.println("Record is DELETED: " + rec.isDeleted());
                System.out.println(rec.getRecordNumber());
                System.out.println(rec.getMap());

                recCounter++;
                assertEquals(recCounter, rec.getRecordNumber());
            }
        } finally {
            reader.close();
        }
    }

}
