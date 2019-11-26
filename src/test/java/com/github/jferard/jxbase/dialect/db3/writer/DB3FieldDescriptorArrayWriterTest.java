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

package com.github.jferard.jxbase.dialect.db3.writer;

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DB3FieldDescriptorArrayWriterTest {
    @Test
    public void test() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DB3FieldDescriptorArrayWriter<DB2Access> arrayWriter =
                new DB3FieldDescriptorArrayWriter<>(
                        DB2Dialect.create(XBaseFileTypeEnum.dBASE2, JxBaseUtils.ASCII_CHARSET)
                                .getAccess(), out);
        final List<XBaseField<? super DB2Access>> fields =
                Arrays.<XBaseField<? super DB2Access>>asList(new CharacterField("char", 10));
        arrayWriter.write(new GenericFieldDescriptorArray<DB2Access>(fields, 10, 10));

        Assert.assertArrayEquals(
                new byte[]{'c', 'h', 'a', 'r', 0, 0, 0, 0, 0, 0, 0, 'C', 0, 0, 0, 0, 10, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 13}, out.toByteArray());
    }

}