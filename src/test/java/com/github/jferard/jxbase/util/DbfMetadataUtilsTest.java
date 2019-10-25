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

package com.github.jferard.jxbase.util;

import com.github.jferard.jxbase.core.DbfField;
import com.github.jferard.jxbase.core.DbfFileTypeEnum;
import com.github.jferard.jxbase.core.DbfMetadata;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

public class DbfMetadataUtilsTest {
    @Test
    public void testFromFields() throws IOException {
        DbfField f = DbfField.fromStringRepresentation("x,C,1,0");

        final DbfMetadata metadata =
                DbfMetadataUtils.fromFields(Arrays.asList(f), DbfFileTypeEnum.FoxBASEPlus1);
        metadata.setUpdateDate(new Date(1234567891011L));
        Assert.assertEquals(
                "DbfMetadata [\n" + "  type=FoxBASEPlus1, \n" + "  updateDate=2009-02-14, \n" +
                        "  recordsQty=0, \n" + "  fullHeaderLength=65, \n" +
                        "  oneRecordLength=2, \n" + "  uncompletedTxFlag=0, \n" +
                        "  ecnryptionFlag=0, \n" + "  fields=x,C,1,0\n]", metadata.toString());
    }

    @Test
    public void testFromFieldsString() throws IOException {
        final DbfMetadata metadata = DbfMetadataUtils.fromFieldsString("x,C,1,0");
        metadata.setUpdateDate(new Date(1234567891011L));
        Assert.assertEquals(
                "DbfMetadata [\n" + "  type=FoxBASEPlus1, \n" + "  updateDate=2009-02-14, \n" +
                        "  recordsQty=0, \n" + "  fullHeaderLength=65, \n" +
                        "  oneRecordLength=2, \n" + "  uncompletedTxFlag=0, \n" +
                        "  ecnryptionFlag=0, \n" + "  fields=x,C,1,0\n]", metadata.toString());
    }

    @Test
    public void testParseHeaderUpdateDate() {
        Assert.assertEquals(new Date(105, 4, 10), DbfMetadataUtils
                .parseHeaderUpdateDate((byte) 'a', (byte) 'b', (byte) 'c',
                        DbfFileTypeEnum.FoxBASEPlus1));
        Assert.assertEquals(new Date(205, 4, 10), DbfMetadataUtils
                .parseHeaderUpdateDate((byte) 'a', (byte) 'b', (byte) 'c',
                        DbfFileTypeEnum.dBASEVII1));
    }

    @Test
    public void testReadFields() throws IOException {
        DbfMetadata md = new DbfMetadata();
        InputStream is = Mockito.mock(InputStream.class);

        Mockito.when(is.read((byte[]) Mockito.anyObject()))
                .thenReturn(JdbfUtils.FIELD_RECORD_LENGTH);
        Mockito.when(is.read()).thenReturn(0, JdbfUtils.HEADER_TERMINATOR);

        DbfMetadataUtils.readFields(md, is);
    }

    @Test
    public void testCreateDbField() throws IOException {
        Assert.assertEquals("DbfField [\n" + "  name=name       , \n" + "  type=Integer, \n" +
                        "  length=233, \n" + "  numberOfDecimalPlaces=65, \n" + "  offset=0\n" +
                        "]",
                DbfMetadataUtils.createDbfField(
                        "name       I    éA".getBytes(Charset.forName("ISO-8859-1"))).toString());
    }
}