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

import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.field.CharacterAccess;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.DB2CharacterAccess;
import com.github.jferard.jxbase.field.RawRecordReadHelper;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DB2RecordReaderTest {
    @Test
    public void test() throws IOException {
        final GenericFieldDescriptorArray<DB2Access> array =
                new GenericFieldDescriptorArray<DB2Access>(
                        Collections.<XBaseField<? super DB2Access>>singleton(
                                new CharacterField("text", 2)), 12, 3);
        final DB2RecordReader<DB2Access> reader = new DB2RecordReader<DB2Access>(
                DB2Access.create(JxBaseUtils.ASCII_CHARSET),
                new ByteArrayInputStream(new byte[]{0x20, 'A', 0x20}), JxBaseUtils.ASCII_CHARSET,
                array);
        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("text", "A");
        Assert.assertEquals(expected, reader.read().getMap());
    }

    @Test
    public void testEndClose() throws IOException {
        final GenericFieldDescriptorArray<DB2Access> array =
                new GenericFieldDescriptorArray<DB2Access>(
                        Collections.<XBaseField<? super DB2Access>>singleton(
                                new CharacterField("text", 2)), 12, 3);
        final DB2RecordReader<DB2Access> reader = new DB2RecordReader<DB2Access>(
                DB2Access.create(JxBaseUtils.ASCII_CHARSET),
                new ByteArrayInputStream(new byte[]{0x20, 'A', 0x20}), JxBaseUtils.ASCII_CHARSET,
                array);
        final Map<String, Object> expected = new HashMap<String, Object>();
        expected.put("text", "A");
        Assert.assertEquals(expected, reader.read().getMap());
        Assert.assertNull(reader.read());
        reader.close();
    }
}