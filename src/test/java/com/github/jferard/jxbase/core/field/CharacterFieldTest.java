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

import com.github.jferard.jxbase.core.XBaseDialect;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CharacterFieldTest {
    @Test
    public void getLengthTest() {
        final XBaseDialect dialect = Mockito.mock(XBaseDialect.class);
        final XBaseField field = new CharacterField("a", 10);

        Mockito.when(dialect.getCharacterFieldLength(10)).thenReturn(11);

        Assert.assertEquals(11, field.getByteLength(dialect));
    }
}