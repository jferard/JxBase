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

package com.github.jferard.jxbase.it;

import com.github.jferard.jxbase.XBaseReader;
import com.github.jferard.jxbase.XBaseReaderFactory;
import com.github.jferard.jxbase.XBaseWriter;
import com.github.jferard.jxbase.XBaseWriterFactory;
import com.github.jferard.jxbase.core.GenericOptional;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.db4.DB4Access;
import com.github.jferard.jxbase.dialect.db4.DB4Utils;
import com.github.jferard.jxbase.dialect.foxpro.memo.TextMemoRecord;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.ByteMemoRecord;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteThenReadIT {

    @Test
    public void test() throws IOException, ParseException {
        final XBaseWriter dbfWriter = this.createWriter("112", JxBaseUtils.UTF8_CHARSET);
        this.addData(dbfWriter);
        final XBaseReader dbfReader = this.createReader("112", JxBaseUtils.UTF8_CHARSET);

        final Map<String, Object> map1 = dbfReader.read().getMap();
        Assert.assertEquals(2, map1.size());
        Assert.assertEquals("name1", map1.get("NAME"));
        Assert.assertEquals(this.createString(503, 'a'),
                new String(((ByteMemoRecord) map1.get("MEMO")).getBytes(), JxBaseUtils.UTF8_CHARSET));

        final Map<String, Object> map2 = dbfReader.read().getMap();
        Assert.assertEquals(2, map2.size());
        Assert.assertEquals("name2", map2.get("NAME"));
        Assert.assertEquals(this.createString(505, 'b'),
                new String(((ByteMemoRecord) map2.get("MEMO")).getBytes(), JxBaseUtils.UTF8_CHARSET));

        final Map<String, Object> map3 = dbfReader.read().getMap();
        Assert.assertEquals(2, map3.size());
        Assert.assertEquals("name3", map3.get("NAME"));
        Assert.assertEquals(this.createString(504, 'c'),
                new String(((ByteMemoRecord) map3.get("MEMO")).getBytes(), JxBaseUtils.UTF8_CHARSET));

        Assert.assertNull(dbfReader.read());
    }

    private XBaseReader createReader(final String tableName, final Charset charset)
            throws IOException {
        return XBaseReaderFactory.createReader(tableName, charset);
    }

    private XBaseWriter createWriter(final String tableName, final Charset charset)
            throws IOException {
        final List<XBaseField<? super DB4Access>>
                fields = new ArrayList<XBaseField<? super DB4Access>>();
        fields.add(new CharacterField("NAME", 20));
        fields.add(new MemoField("MEMO"));


        final Map<String, Object> meta = new HashMap<String, Object>();
        meta.put(DB4Utils.META_UNCOMPLETED_TX_FLAG, JxBaseUtils.NULL_BYTE);
        meta.put(DB4Utils.META_ENCRYPTION_FLAG, JxBaseUtils.NULL_BYTE);

        return XBaseWriterFactory
                .createWriter(XBaseFileTypeEnum.dBASE4Memo, tableName, charset, meta,
                        fields, Collections.<String, Object>emptyMap());
    }

    private void addData(final XBaseWriter dbfWriter) throws IOException {
        final Map<String, Object> valueMap = new HashMap<String, Object>();
        try {
            valueMap.put("NAME", "name1");
            valueMap.put("MEMO",
                    new TextMemoRecord(this.createString(503, 'a'), JxBaseUtils.UTF8_CHARSET));
            dbfWriter.write(valueMap);
            valueMap.put("NAME", "name2");
            valueMap.put("MEMO",
                    new TextMemoRecord(this.createString(505, 'b'), JxBaseUtils.UTF8_CHARSET));
            dbfWriter.write(valueMap);
            valueMap.put("NAME", "name3");
            valueMap.put("MEMO",
                    new TextMemoRecord(this.createString(504, 'c'), JxBaseUtils.UTF8_CHARSET));
            dbfWriter.write(valueMap);
        } finally {
            dbfWriter.close();
        }
    }

    private String createString(final int len, final char c) {
        final StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
