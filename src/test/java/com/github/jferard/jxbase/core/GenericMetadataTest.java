/*
 * JxBase - Copyright (c) 2019-2021 Julien Férard
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

import com.github.jferard.jxbase.dialect.db4.DB4Utils;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class GenericMetadataTest {
    @Test
    public void testToString() {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB4Utils.META_UPDATE_DATE, new Date(0));
        meta.put(DB4Utils.META_RECORDS_QTY, 0);
        meta.put(DB4Utils.META_UNCOMPLETED_TX_FLAG, JxBaseUtils.NULL_BYTE);
        meta.put(DB4Utils.META_ENCRYPTION_FLAG, JxBaseUtils.NULL_BYTE);
        final XBaseMetadata metadata =
                new GenericMetadata(XBaseFileTypeEnum.FoxBASEPlus1.toByte(), 0, 0, meta);
        Assert.assertEquals(
                "GenericMetadata[type=3 (FoxBASEPlus1), fullHeaderLength=0, oneRecordLength=0, " +
                        "meta={encryptionFlag=0, updateDate=1970-01-01, uncompletedTxFlag=0, " +
                        "recordsQty=0}]", metadata.toString());
    }

    @Test
    public void testGetUpdateDate() {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB4Utils.META_UPDATE_DATE, new Date(0));
        meta.put(DB4Utils.META_RECORDS_QTY, 0);
        meta.put(DB4Utils.META_UNCOMPLETED_TX_FLAG, JxBaseUtils.NULL_BYTE);
        meta.put(DB4Utils.META_ENCRYPTION_FLAG, JxBaseUtils.NULL_BYTE);
        final GenericMetadata metadata =
                new GenericMetadata(XBaseFileTypeEnum.FoxBASEPlus1.toByte(), 0, 0, meta);
        Assert.assertEquals(new Date(0), metadata.get(DB4Utils.META_UPDATE_DATE));
    }

    @Test
    public void testGetRecordsQty() {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB4Utils.META_UPDATE_DATE, new Date(0));
        meta.put(DB4Utils.META_RECORDS_QTY, 100);
        meta.put(DB4Utils.META_UNCOMPLETED_TX_FLAG, JxBaseUtils.NULL_BYTE);
        meta.put(DB4Utils.META_ENCRYPTION_FLAG, JxBaseUtils.NULL_BYTE);
        final GenericMetadata metadata =
                new GenericMetadata(XBaseFileTypeEnum.FoxBASEPlus1.toByte(), 0, 0, meta);
        Assert.assertEquals(100, metadata.get(DB4Utils.META_RECORDS_QTY));
    }

    @Test
    public void testGetUncompletedTxFlag() {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB4Utils.META_UPDATE_DATE, new Date(0));
        meta.put(DB4Utils.META_RECORDS_QTY, 0);
        meta.put(DB4Utils.META_UNCOMPLETED_TX_FLAG, (byte) 10);
        meta.put(DB4Utils.META_ENCRYPTION_FLAG, JxBaseUtils.NULL_BYTE);
        final GenericMetadata metadata =
                new GenericMetadata(XBaseFileTypeEnum.FoxBASEPlus1.toByte(), 0, 0, meta);
        Assert.assertEquals((byte) 10, metadata.get(DB4Utils.META_UNCOMPLETED_TX_FLAG));
    }

    @Test
    public void testGetEncryptionFlag() {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB4Utils.META_UPDATE_DATE, new Date(0));
        meta.put(DB4Utils.META_RECORDS_QTY, 0);
        meta.put(DB4Utils.META_UNCOMPLETED_TX_FLAG, JxBaseUtils.NULL_BYTE);
        meta.put(DB4Utils.META_ENCRYPTION_FLAG, (byte) 10);
        final GenericMetadata metadata =
                new GenericMetadata(XBaseFileTypeEnum.FoxBASEPlus1.toByte(), 0, 0, meta);
        Assert.assertEquals((byte) 10, metadata.get(DB4Utils.META_ENCRYPTION_FLAG));
    }
}