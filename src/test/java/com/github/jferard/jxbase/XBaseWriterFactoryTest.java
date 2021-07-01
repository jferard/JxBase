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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.core.GenericOptional;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.DB2Access;
import com.github.jferard.jxbase.dialect.db2.DB2Dialect;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

public class XBaseWriterFactoryTest {
    @Test
    public void test() throws IOException {
        final String tableName = TestHelper.createTempTable("mybase");
        final XBaseWriter myBase =
                XBaseWriterFactory.<DB2Access, DB2Dialect>createWriter(XBaseFileTypeEnum.dBASE2,
                        tableName, JxBaseUtils.UTF8_CHARSET, Collections.<String, Object>emptyMap(),
                        Collections.<XBaseField<? super DB2Access>>emptyList(),
                        GenericOptional.DB234_EMPTY, null);
        myBase.close();
    }

}