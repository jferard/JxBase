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

import com.github.jferard.jxbase.util.JdbfUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class DbfMetadataTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testToString() {
        final DbfMetadata metadata = DbfMetadata
                .create(DbfFileTypeEnum.FoxBASEPlus1, new Date(0), 0, 0, 0, JdbfUtils.NULL_BYTE,
                        JdbfUtils.NULL_BYTE, Collections.<DbfField>emptyList());
        Assert.assertEquals("DbfMetadata[type=FoxBASEPlus1, updateDate=1970-01-01, recordsQty=0, " +
                "fullHeaderLength=0, oneRecordLength=0, uncompletedTxFlag=0, " +
                "encryptionFlag=0, fields=]", metadata.toString());
    }

    @Test
    public void testToStringWithFields() {
        DbfField f1 = DbfFieldImpl.fromStringRepresentation("x,C,1,0");
        DbfField f2 = DbfFieldImpl.fromStringRepresentation("y,I,8,0");

        final DbfMetadata metadata = DbfMetadata
                .create(DbfFileTypeEnum.FoxBASEPlus1, new Date(0), 0, 0, 0, JdbfUtils.NULL_BYTE,
                        JdbfUtils.NULL_BYTE, Arrays.asList(f1, f2));
        Assert.assertEquals(
                "DbfMetadata[type=FoxBASEPlus1, updateDate=1970-01-01, recordsQty=0, " +
                        "fullHeaderLength=0, oneRecordLength=0, uncompletedTxFlag=0, " +
                        "encryptionFlag=0, fields=OffsetDbfField[field=x,C,1,0, " +
                        "offset=1]|OffsetDbfField[field=y,I,8,0, offset=2]]",
                metadata.toString());
    }

    @Test
    public void testSetNullType() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("File type should not be null");
        DbfMetadata.create(null, new Date(0), 0, 0, 0, JdbfUtils.NULL_BYTE, JdbfUtils.NULL_BYTE,
                Collections.<DbfField>emptyList());
    }

    @Test
    public void testGetUpdateDate() {
        final DbfMetadata metadata = DbfMetadata
                .create(DbfFileTypeEnum.FoxBASEPlus1, new Date(0), 0, 0, 0, JdbfUtils.NULL_BYTE,
                        JdbfUtils.NULL_BYTE, Collections.<DbfField>emptyList());
        Assert.assertEquals(new Date(0), metadata.getUpdateDate());
    }

    @Test
    public void testGetRecordsQty() {
        final DbfMetadata metadata = DbfMetadata
                .create(DbfFileTypeEnum.FoxBASEPlus1, new Date(0), 100, 0, 0, JdbfUtils.NULL_BYTE,
                        JdbfUtils.NULL_BYTE, Collections.<DbfField>emptyList());
        Assert.assertEquals(100, metadata.getRecordsQty());
    }

    @Test
    public void testGetUncompletedTxFlag() {
        final DbfMetadata metadata = DbfMetadata
                .create(DbfFileTypeEnum.FoxBASEPlus1, new Date(0), 0, 0, 0, (byte) 10,
                        JdbfUtils.NULL_BYTE, Collections.<DbfField>emptyList());
        Assert.assertEquals(10, metadata.getUncompletedTxFlag());
    }

    @Test
    public void testGetEncryptionFlag() {
        final DbfMetadata metadata = DbfMetadata
                .create(DbfFileTypeEnum.FoxBASEPlus1, new Date(0), 0, 0, 0, JdbfUtils.NULL_BYTE,
                        (byte) 10, Collections.<DbfField>emptyList());
        Assert.assertEquals(10, metadata.getEncryptionFlag());
    }

    @Test
    public void testGetFields() {
        DbfField f = DbfFieldImpl.fromStringRepresentation("x,C,1,0");
        final DbfMetadata metadata = DbfMetadata
                .create(DbfFileTypeEnum.FoxBASEPlus1, new Date(0), 0, 0, 0, (byte) 10,
                        JdbfUtils.NULL_BYTE, Collections.singletonList(f));
        Assert.assertEquals(Collections.singletonList(f),
                new ArrayList<DbfField>(metadata.getFields()));
        Assert.assertEquals("OffsetDbfField[field=x,C,1,0, offset=1]",
                metadata.getOffsetField("x").toString());
    }
}