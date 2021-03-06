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

package com.github.jferard.jxbase.dialect.db3.writer;

import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.NumericField;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3Dialect;
import com.github.jferard.jxbase.dialect.db3.DB3DialectFactory;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.db3.memo.DB3MemoWriter;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.ByteMemoRecord;
import com.github.jferard.jxbase.memo.XBaseMemoWriter;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

public class DB3RecordWriterTest {
    @Test
    public void testWrite() throws IOException {
        final XBaseMemoWriter memoWriter = null;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DB3Dialect dialect = DB3DialectFactory
                .create(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE);
        @SuppressWarnings("unchecked")
        final DB3RecordWriter<DB3Access, DB3Dialect> writer =
                new DB3RecordWriter<DB3Access, DB3Dialect>(dialect, bos, JxBaseUtils.ASCII_CHARSET,
                        memoWriter, Arrays.<XBaseField<? super DB3Access>>asList(
                                new CharacterField("chars", 10),
                                new NumericField("num", 8, 2)));
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("chars", "some ch.");
        map.put("num", new BigDecimal("10.56"));
        writer.write(map);
        writer.close();

        Assert.assertArrayEquals(
                new byte[]{' ', 's', 'o', 'm', 'e', ' ', 'c', 'h', '.', ' ', ' ', ' ', ' ', ' ', '1', '0', '.',
                        '5', '6', 0x1A}, bos.toByteArray());
        Assert.assertEquals(1, writer.getRecordQty());
    }

    @Test
    public void testWriteMemo() throws IOException {
        final XBaseMemoWriter memoWriter = PowerMock.createMock(XBaseMemoWriter.class);

        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DB3Dialect dialect = DB3DialectFactory
                .create(XBaseFileTypeEnum.dBASE3plus, JxBaseUtils.ASCII_CHARSET,
                        JxBaseUtils.UTC_TIME_ZONE);
        @SuppressWarnings("unchecked")
        final DB3RecordWriter<DB3Access, DB3Dialect> writer =
                new DB3RecordWriter<DB3Access, DB3Dialect>(dialect, bos, JxBaseUtils.ASCII_CHARSET,
                        memoWriter, Arrays.<XBaseField<? super DB3Access>>asList(
                        new MemoField("memo")));
        final HashMap<String, Object> map = new HashMap<String, Object>();
        final byte[] bytes = "memo content".getBytes(JxBaseUtils.UTF8_CHARSET);
        final ByteMemoRecord memo = new ByteMemoRecord(bytes, bytes.length);
        map.put("memo", memo);
        PowerMock.resetAll();

        EasyMock.expect(memoWriter.write(memo)).andReturn(10L);
        memoWriter.close();
        PowerMock.replayAll();

        writer.write(map);
        writer.close();

        PowerMock.verifyAll();
        Assert.assertArrayEquals(
                new byte[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '1', '0', 0x1A}, bos.toByteArray());
        Assert.assertEquals(1, writer.getRecordQty());
    }
}