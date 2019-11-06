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

package com.github.jferard.jxbase.core.field;

import com.github.jferard.jxbase.core.DbfFileTypeEnumTest;
import com.github.jferard.jxbase.core.GenericDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.field.NumericField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NumericFieldTest {
    private GenericDialect dialect;

    @Before
    public void setUp() {
        this.dialect = new GenericDialect(XBaseFileTypeEnum.dBASEIV1);
    }

    @Test
    public void testNumeric() {
        Assert.assertEquals("a,N,10,2",
                new NumericField("a", 10, 2).toStringRepresentation(this.dialect));
    }
}