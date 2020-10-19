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

package com.github.jferard.jxbase.core;

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class DbfMetadataTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testToString() {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("updateDate", new Date(0));
        meta.put("recordsQty", 0);
        meta.put("uncompletedTxFlag", JxBaseUtils.NULL_BYTE);
        meta.put("encryptionFlag", JxBaseUtils.NULL_BYTE);
        final XBaseMetadata metadata =
                new GenericMetadata(XBaseFileTypeEnum.FoxBASEPlus1.toByte(), 0, 0, meta);
        Assert.assertEquals("GenericMetadata[type=3, fullHeaderLength=0, oneRecordLength=0, " +
                "meta={encryptionFlag=0, updateDate=1970-01-01, uncompletedTxFlag=0, " +
                "recordsQty=0}]", metadata.toString());
    }

    @Test
    public void testGetUpdateDate() {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("updateDate", new Date(0));
        meta.put("recordsQty", 0);
        meta.put("uncompletedTxFlag", JxBaseUtils.NULL_BYTE);
        meta.put("encryptionFlag", JxBaseUtils.NULL_BYTE);
        final GenericMetadata metadata =
                new GenericMetadata(XBaseFileTypeEnum.FoxBASEPlus1.toByte(), 0, 0, meta);
        Assert.assertEquals(new Date(0), metadata.get("updateDate"));
    }

    @Test
    public void testGetRecordsQty() {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("updateDate", new Date(0));
        meta.put("recordsQty", 100);
        meta.put("uncompletedTxFlag", JxBaseUtils.NULL_BYTE);
        meta.put("encryptionFlag", JxBaseUtils.NULL_BYTE);
        final GenericMetadata metadata =
                new GenericMetadata(XBaseFileTypeEnum.FoxBASEPlus1.toByte(), 0, 0, meta);
        Assert.assertEquals(100, metadata.get("recordsQty"));
    }

    @Test
    public void testGetUncompletedTxFlag() {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("updateDate", new Date(0));
        meta.put("recordsQty", 0);
        meta.put("uncompletedTxFlag", (byte) 10);
        meta.put("encryptionFlag", JxBaseUtils.NULL_BYTE);
        final GenericMetadata metadata =
                new GenericMetadata(XBaseFileTypeEnum.FoxBASEPlus1.toByte(), 0, 0, meta);
        Assert.assertEquals((byte) 10, metadata.get("uncompletedTxFlag"));
    }

    @Test
    public void testGetEncryptionFlag() {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("updateDate", new Date(0));
        meta.put("recordsQty", 0);
        meta.put("uncompletedTxFlag", JxBaseUtils.NULL_BYTE);
        meta.put("encryptionFlag", (byte) 10);
        final GenericMetadata metadata =
                new GenericMetadata(XBaseFileTypeEnum.FoxBASEPlus1.toByte(), 0, 0, meta);
        Assert.assertEquals((byte) 10, metadata.get("encryptionFlag"));
    }


    /*
    @Test
    public void testToStringWithFields() {
        final XBaseField f1 = this.dbfFieldFactory.fromStringRepresentation("x,C,1,0");
        final XBaseField f2 = this.dbfFieldFactory.fromStringRepresentation("y,I,8,0");

        final XBaseMetadata metadata = GenericMetadata
                .create(XBaseFileTypeEnum.FoxBASEPlus1, new Date(0), 0, 0, 0, JdbfUtils.NULL_BYTE,
                        JdbfUtils.NULL_BYTE, Arrays.<XBaseField>asList(f1, f2));
        Assert.assertEquals("DbfMetadata[type=FoxBASEPlus1, updateDate=1970-01-01, recordsQty=0, " +
                "fullHeaderLength=0, oneRecordLength=0, uncompletedTxFlag=0, " +
                "encryptionFlag=0, fields=OffsetDbfField[field=x,C,1,0, " +
                "offset=1]|OffsetDbfField[field=y,I,8,0, offset=2]]", metadata.toString());
    }

    @Test
    public void testGetFields() {
        final XBaseField f = this.dbfFieldFactory.fromStringRepresentation("x,C,1,0");
        final XBaseMetadata metadata = GenericMetadata
                .create(XBaseFileTypeEnum.FoxBASEPlus1, new Date(0), 0, 0, 0, (byte) 10,
                        JdbfUtils.NULL_BYTE, Collections.<XBaseField>singletonList(f));
        Assert.assertEquals(Collections.singletonList(f),
                new ArrayList<XBaseField>(metadata.getFields()));
        Assert.assertEquals("OffsetDbfField[field=x,C,1,0, offset=1]",
                metadata.getOffsetField("x").toString());
    }

     */
}