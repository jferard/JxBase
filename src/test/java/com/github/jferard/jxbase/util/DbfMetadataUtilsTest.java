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

package com.github.jferard.jxbase.util;

import com.github.jferard.jxbase.core.DbfField;
import com.github.jferard.jxbase.core.DbfFieldImpl;
import com.github.jferard.jxbase.core.DbfFileTypeEnum;
import com.github.jferard.jxbase.core.DbfMetadata;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

public class DbfMetadataUtilsTest {
    @Test
    public void testFromFields() {
        DbfField<?> f = DbfFieldImpl.fromStringRepresentation("x,C,1,0");

        final DbfMetadata metadata = DbfMetadataUtils
                .fromFields(DbfFileTypeEnum.FoxBASEPlus1, new Date(1234567891011L), 0,
                        Collections.<DbfField<?>>singletonList(f));
        Assert.assertEquals("DbfMetadata[type=FoxBASEPlus1, updateDate=2009-02-14, recordsQty=0, " +
                        "fullHeaderLength=65, oneRecordLength=2, uncompletedTxFlag=0, " +
                        "encryptionFlag=0, fields=OffsetDbfField[field=x,C,1,0, offset=1]]",
                metadata.toString());
    }

    @Test
    public void testFromFieldsString() {
        final DbfMetadata metadata = DbfMetadataUtils
                .fromFieldsString(DbfFileTypeEnum.FoxBASEPlus1, new Date(1234567891011L), 0,
                        "x,C,1,0");
        Assert.assertEquals("DbfMetadata[type=FoxBASEPlus1, updateDate=2009-02-14, recordsQty=0, " +
                        "fullHeaderLength=65, oneRecordLength=2, uncompletedTxFlag=0, " +
                        "encryptionFlag=0, fields=OffsetDbfField[field=x,C,1,0, offset=1]]",
                metadata.toString());
    }

    /*
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

        List<DbfField> fields = DbfMetadataUtils.getDbfFields(md, is);
        md.setFields(fields);
    }

    @Test
    public void testCreateDbField() throws IOException {
        Assert.assertEquals("DbfField [\n" + "  name=name       , \n" + "  type=Integer, \n" +
                        "  length=233, \n" + "  numberOfDecimalPlaces=65, \n" + "  offset=0\n" +
                        "]",
                DbfMetadataUtils.createDbfField(
                        "name       I    éA".getBytes(Charset.forName("ISO-8859-1"))).toString());
    }

    @Test
    public void testFillHeaderFields() throws IOException {
        DbfMetadata md = new DbfMetadata();
        DbfMetadataUtils.fillHeaderFields(md,
                new byte[]{0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2, 0x2,
                        0x2, 0x2});
        Assert.assertEquals(
                "DbfMetadata [\n" + "  type=FoxBASE1, \n" + "  updateDate=2002-02-02, \n" +
                        "  recordsQty=33686018, \n" + "  fullHeaderLength=514, \n" +
                        "  oneRecordLength=514, \n" + "  uncompletedTxFlag=2, \n" +
                        "  encryptionFlag=2, \n" + "  fields=null\n" + "]", md.toString());

    }
     */
}