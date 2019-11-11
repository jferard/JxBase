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

package com.github.jferard.jxbase.reader.internal;

import com.github.jferard.jxbase.core.GenericDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.field.CharacterField;
import com.github.jferard.jxbase.core.field.XBaseField;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GenericFieldDescriptorArrayReaderTest {
    private XBaseFieldDescriptorArrayReader gfdar;

    @Test
    public void read() throws IOException {
        final byte[] bytes =
                new byte[]{'a', 'b', 'c', 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 'C', 0x0, 0x0,
                        0x0, 0x0, 10, 0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
                        0x0, 0x0, 0x0D};
        Assert.assertEquals(33, bytes.length);
        final GenericDialect dialect = new GenericDialect(XBaseFileTypeEnum.dBASEIV1);
        this.gfdar = new GenericFieldDescriptorArrayReader(dialect, new ByteArrayInputStream(bytes),
                null);
        final XBaseFieldDescriptorArray array = this.gfdar.read();
        Assert.assertEquals(33, array.getArrayLength());
        Assert.assertEquals(11, array.getRecordLength());
        Assert.assertEquals(1, array.getFields().size());
        final XBaseField field = array.getFields().iterator().next();
        Assert.assertTrue(field instanceof CharacterField);
        final CharacterField cf = (CharacterField) field;
        Assert.assertEquals("abc", cf.getName());
        Assert.assertEquals(10, cf.getValueByteLength(dialect));
    }
}