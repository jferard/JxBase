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

import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.field.XBaseField;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class GenericFieldDescriptorArrayTest {
    @Test
    public void test() {
        final List<XBaseField<? super DB2Access>> fields =
                Collections.<XBaseField<? super DB2Access>>singletonList(
                        new CharacterField("char", 10));
        final GenericFieldDescriptorArray<DB2Access> array =
                new GenericFieldDescriptorArray<DB2Access>(fields, 10, 20);
        Assert.assertEquals(fields, array.getFields());
        Assert.assertEquals(10, array.getArrayLength());
        Assert.assertEquals(20, array.getRecordLength());
        Assert.assertEquals(
                "GenericFieldDescriptorArray[fields=[CharacterField[name=char, length=10]]]",
                array.toString());
    }

}