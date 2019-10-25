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

package com.github.jferard.jxbase.core;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class DbfMetadataTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testToString() {
        final DbfMetadata metadata = new DbfMetadata();
        metadata.setUpdateDate(new Date(0));
        Assert.assertEquals("DbfMetadata [\n" + "  type=null, \n" + "  updateDate=1970-01-01, \n" +
                        "  recordsQty=0, \n" + "  fullHeaderLength=0, \n" + "  oneRecordLength=0,"
                        + " \n" +
                        "  uncompletedTxFlag=0, \n" + "  ecnryptionFlag=0, \n" + "  fields=null\n"
                        + "]",
                metadata.toString());
    }

    @Test
    public void testToStringWithFields() {
        DbfField f1 = DbfField.fromStringRepresentation("x,C,1,0");
        DbfField f2 = DbfField.fromStringRepresentation("y,I,8,0");

        final DbfMetadata metadata = new DbfMetadata();
        metadata.setUpdateDate(new Date(0));
        metadata.setFields(Arrays.asList(f1, f2));
        Assert.assertEquals("DbfMetadata [\n" + "  type=null, \n" + "  updateDate=1970-01-01, \n" +
                "  recordsQty=0, \n" + "  fullHeaderLength=0, \n" + "  oneRecordLength=0," + " \n" +
                "  uncompletedTxFlag=0, \n" + "  ecnryptionFlag=0, \n" +
                "  fields=x,C,1,0|y,I,8,0\n" + "]", metadata.toString());
    }

    @Test
    public void testSetNullType() throws IOException {
        final DbfMetadata metadata = new DbfMetadata();

        exception.expect(IOException.class);
        exception.expectMessage("The file is corrupted or is not a dbf file");
        metadata.setType(null);
    }

    @Test
    public void testGetUpdateDate() {
        final DbfMetadata metadata = new DbfMetadata();
        metadata.setUpdateDate(new Date(0));
        Assert.assertEquals(new Date(0), metadata.getUpdateDate());
    }

    @Test
    public void testGetRecordsQty() {
        final DbfMetadata metadata = new DbfMetadata();
        metadata.setRecordsQty(100);
        Assert.assertEquals(100, metadata.getRecordsQty());
    }

    @Test
    public void testGetUncompletedTxFlag() {
        final DbfMetadata metadata = new DbfMetadata();
        metadata.setUncompletedTxFlag((byte) 10);
        Assert.assertEquals(10, metadata.getUncompletedTxFlag());
    }

    @Test
    public void testGetEcnryptionFlag() {
        final DbfMetadata metadata = new DbfMetadata();
        metadata.setEcnryptionFlag((byte) 10);
        Assert.assertEquals(10, metadata.getEcnryptionFlag());
    }

    @Test
    public void testGetFields() {
        DbfField f = DbfField.fromStringRepresentation("x,C,1,0");

        final DbfMetadata metadata = new DbfMetadata();
        metadata.setUpdateDate(new Date(0));
        metadata.setFields(Arrays.asList(f));

        Assert.assertEquals(Arrays.asList(f), new ArrayList<DbfField>(metadata.getFields()));
        Assert.assertEquals(f, metadata.getField("x"));
    }
}