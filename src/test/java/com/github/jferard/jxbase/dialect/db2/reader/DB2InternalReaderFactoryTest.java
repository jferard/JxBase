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

package com.github.jferard.jxbase.dialect.db2.reader;

import com.github.jferard.jxbase.core.GenericMetadata;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.dialect.db2.DB2Utils;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.reader.XBaseFieldDescriptorArrayReader;
import com.github.jferard.jxbase.reader.XBaseMetadataReader;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class DB2InternalReaderFactoryTest {
    @Test
    public void testCreateArrayReader() throws IOException {
        final byte[] buf = new byte[524];
        System.arraycopy(
                new byte[]{'t', 'e', 's', 't', 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 'C', 0xA, 0x0,
                        0x0, 0x0, 0xD}, 0, buf, 0, 17);
        System.arraycopy(new byte[]{'v', 'a', 'l', 'u', 'e', ' ', ' ', ' ', ' ', ' '}, 0, buf, 514,
                10);
        final ByteArrayInputStream bis = new ByteArrayInputStream(buf);
        final XBaseMetadata metadata = PowerMock.createMock(GenericMetadata.class);
        final DB2Dialect dialect = PowerMock.createMock(DB2Dialect.class);
        final CharacterField field = new CharacterField("test", 10);
        final DB2Access access = DB2Access.create(JxBaseUtils.ASCII_CHARSET);
        PowerMock.resetAll();

        EasyMock.expect(dialect.getFieldDescriptorLength()).andReturn(16);
        EasyMock.expect((CharacterField) dialect.createXBaseField("test", (byte) 'C', 10, 0))
                .andReturn(field);
        EasyMock.expect(dialect.getAccess()).andReturn(access);
        PowerMock.replayAll();

        final DB2ChunkReaderFactory factory =
                new DB2ChunkReaderFactory(dialect, JxBaseUtils.UTC_TIME_ZONE);

        final XBaseFieldDescriptorArrayReader<DB2Access, DB2Dialect> arrayReader =
                factory.createFieldDescriptorArrayReader(bis, metadata);
        final XBaseFieldDescriptorArray<DB2Access> array = arrayReader.read();
        PowerMock.verifyAll();

        Assert.assertEquals(Arrays.asList(field), array.getFields());
    }

    @Test
    public void testCreateMetaDataReader() throws IOException {
        final byte[] buf = new byte[520];
        System.arraycopy(
                new byte[]{0x2, 0x3, 0x0, 99, 5, 1, 0x10, 0}, 0, buf, 0, 7);
        final ByteArrayInputStream bis = new ByteArrayInputStream(buf);
        final DB2Dialect dialect = PowerMock.createMock(DB2Dialect.class);
        PowerMock.resetAll();

        PowerMock.replayAll();

        final DB2ChunkReaderFactory factory =
                new DB2ChunkReaderFactory(dialect, JxBaseUtils.UTC_TIME_ZONE);

        final XBaseMetadataReader reader = factory.createMetadataReader(bis);
        final XBaseMetadata metadata = reader.read();
        PowerMock.verifyAll();

        Assert.assertEquals(0x2, metadata.getFileTypeByte());
        Assert.assertEquals(XBaseFileTypeEnum.FoxBASE1, metadata.getFileType());
        Assert.assertEquals(16, metadata.getOneRecordLength());
        Assert.assertEquals(520, metadata.getFullHeaderLength());
        Assert.assertEquals(this.createDate(1999, 4, 1), metadata.get(DB2Utils.META_UPDATE_DATE));
        Assert.assertEquals(3, metadata.get(DB2Utils.META_RECORDS_QTY));
    }

    private Date createDate(final int year, final int month, final int day) {
        final Calendar calendar = Calendar.getInstance(JxBaseUtils.UTC_TIME_ZONE);
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();

    }
}