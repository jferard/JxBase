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

package com.github.jferard.jxbase.dialect.db3.reader;

import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseRecord;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.dialect.db3.DB3DialectFactory;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.foxpro.memo.TextMemoRecord;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.XBaseMemoReader;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.powermock.api.easymock.PowerMock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class DB3RecordReaderTest {
    @Test
    public void testEmptyStream() throws IOException {
        final DB3RecordReader<DB3Access>
                reader = this.getDb3AccessDB3RecordReader(new byte[]{});
        Assert.assertNull(reader.read());
    }

    @Test
    public void testTerminator() throws IOException {
        final DB3RecordReader<DB3Access>
                reader = this.getDb3AccessDB3RecordReader(
                new byte[]{JxBaseUtils.RECORDS_TERMINATOR, 0, 0, 0, 0});
        Assert.assertNull(reader.read());
    }

    @Test
    public void testShortText() throws IOException {
        final DB3RecordReader<DB3Access>
                reader =
                this.getDb3AccessDB3RecordReader("abc".getBytes(JxBaseUtils.ASCII_CHARSET));
        Assert.assertThrows(IOException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                reader.read();
            }
        });
    }

    @Test
    public void testLongText() throws IOException {
        final DB3RecordReader<DB3Access>
                reader =
                this.getDb3AccessDB3RecordReader("*abc       ".getBytes(JxBaseUtils.ASCII_CHARSET));
        final XBaseRecord record = reader.read();
        Assert.assertTrue(record.isDeleted());
        Assert.assertEquals(1, record.getRecordNumber());
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("char", "abc");

        Assert.assertEquals(map, record.getMap());

    }

    public DB3RecordReader<DB3Access> getDb3AccessDB3RecordReader(final byte[] buf) {
        final TimeZone timezone = TimeZone.getDefault();
        final Charset charset = JxBaseUtils.ASCII_CHARSET;
        final ByteArrayInputStream in = new ByteArrayInputStream(buf);
        @SuppressWarnings("unchecked") final List<XBaseField<? super DB3Access>> fields =
                Arrays.<XBaseField<? super DB3Access>>asList(new CharacterField("char", 10));
        final XBaseFieldDescriptorArray<DB3Access> array =
                new GenericFieldDescriptorArray<DB3Access>(fields, 1, 11);
        final DB3RecordReader<DB3Access> reader =
                new DB3RecordReader<DB3Access>(DB3DialectFactory.createAccess(charset, timezone),
                        in, null, charset, array, timezone);
        return reader;
    }

    @Test
    public void testMemo() throws IOException {
        final XBaseMemoReader memoReader = PowerMock.createMock(XBaseMemoReader.class);
        final TimeZone timezone = TimeZone.getDefault();
        final Charset charset = JxBaseUtils.ASCII_CHARSET;
        final ByteArrayInputStream in = new ByteArrayInputStream("*123       ".getBytes(JxBaseUtils.ASCII_CHARSET));
        @SuppressWarnings("unchecked") final List<XBaseField<? super DB3Access>> fields =
                Arrays.<XBaseField<? super DB3Access>>asList(new MemoField("memo"));
        final XBaseFieldDescriptorArray<DB3Access> array =
                new GenericFieldDescriptorArray<DB3Access>(fields, 1, 11);
        final TextMemoRecord memoRecord = new TextMemoRecord("text", JxBaseUtils.ASCII_CHARSET);

        PowerMock.resetAll();
        EasyMock.expect(memoReader.read(123L)).andReturn(memoRecord);
        memoReader.close();

        PowerMock.replayAll();
        final DB3RecordReader<DB3Access> reader =
                new DB3RecordReader<DB3Access>(DB3DialectFactory.createAccess(charset, timezone),
                        in, memoReader, charset, array, timezone);
        final XBaseRecord record = reader.read();
        reader.close();

        PowerMock.verifyAll();
        Assert.assertTrue(record.isDeleted());
        Assert.assertEquals(1, record.getRecordNumber());
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("memo", memoRecord);

        Assert.assertEquals(map, record.getMap());

    }
}