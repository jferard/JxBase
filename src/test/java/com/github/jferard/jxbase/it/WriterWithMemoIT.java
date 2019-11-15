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

import com.github.jferard.jxbase.GenericOptional;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.XBaseWriter;
import com.github.jferard.jxbase.XBaseWriterFactory;
import com.github.jferard.jxbase.dialect.foxpro.TextMemoRecord;
import com.github.jferard.jxbase.field.CharacterField;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.MemoField;
import com.github.jferard.jxbase.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriterWithMemoIT {
    private final Map<String, Object> valueMap = new HashMap<String, Object>();
    private final List<XBaseField> fields = new ArrayList<XBaseField>();

    @Before
    public void prepareData() {
        this.fields.add(new CharacterField("NAME", 20));
        this.fields.add(new MemoField<XBaseMemoRecord>("MEMO"));
        this.valueMap.put("NAME", "some data");
        this.valueMap.put("MEMO", new TextMemoRecord("in the memo", JxBaseUtils.UTF8_CHARSET));
    }

    @Test
    public void test() throws IOException {
        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put("uncompletedTxFlag", JxBaseUtils.NULL_BYTE);
        meta.put("encryptionFlag", JxBaseUtils.NULL_BYTE);

        final XBaseWriter dbfWriter = XBaseWriterFactory
                .createWriter(XBaseFileTypeEnum.FoxBASEPlus2, "112", JxBaseUtils.UTF8_CHARSET, meta,
                        this.fields, GenericOptional.EMPTY, Collections.<String, Object>emptyMap());
        try {
            dbfWriter.write(this.valueMap);
        } finally {
            dbfWriter.close();
        }
    }
}
