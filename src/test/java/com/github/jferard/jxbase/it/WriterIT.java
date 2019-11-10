/*
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

package com.github.jferard.jxbase.it;

import com.github.jferard.jxbase.core.GenericOptional;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.core.field.CharacterField;
import com.github.jferard.jxbase.core.field.NumericField;
import com.github.jferard.jxbase.core.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import com.github.jferard.jxbase.writer.XBaseWriter;
import com.github.jferard.jxbase.writer.XBaseWriterFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriterIT {
    private final Map<String, Object> valueMap = new HashMap<String, Object>();
    private final List<XBaseField> fields = new ArrayList<XBaseField>();

    @Before
    public void prepareData() {
        this.fields.add(new CharacterField("FIOISP", 100));
        this.fields.add(new CharacterField("NAME", 250));
        this.fields.add(new CharacterField("SURNAME", 250));
        this.fields.add(new CharacterField("DATER", 10));
        this.fields.add(new CharacterField("SECONDNAME", 250));
        this.fields.add(new NumericField("UNICODE", 10, 0));
        this.fields.add(new CharacterField("NUMID", 100));
        this.valueMap.put("FIOISP", "Виноградова Ольга Евгеньевна");
        this.valueMap.put("NAME", "Вячеслав");
        this.valueMap.put("SURNAME", "Егоров");
        this.valueMap.put("DATER", "30.06.1971");
        this.valueMap.put("SECONDNAME", "Иванович");
        this.valueMap.put("UNICODE", new BigDecimal(1001731864));
        this.valueMap.put("NUMID", "6/14/19/69");
    }

    @Test
    public void test() throws IOException {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("uncompletedTxFlag", JxBaseUtils.NULL_BYTE);
        meta.put("encryptionFlag", JxBaseUtils.NULL_BYTE);

        final XBaseWriter dbfWriter = new XBaseWriterFactory()
                .createWriter(XBaseFileTypeEnum.dBASEIV1, "111", Charset.forName("UTF-8"), meta,
                        this.fields, GenericOptional.EMPTY);
        try {
            dbfWriter.write(this.valueMap);
        } finally {
            dbfWriter.close();
        }
    }
}
