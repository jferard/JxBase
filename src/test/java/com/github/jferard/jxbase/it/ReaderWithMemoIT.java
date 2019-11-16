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

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.XBaseDialect;
import com.github.jferard.jxbase.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseMetadata;
import com.github.jferard.jxbase.XBaseReader;
import com.github.jferard.jxbase.XBaseReaderFactory;
import com.github.jferard.jxbase.XBaseRecord;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;
import com.github.jferard.jxbase.field.CharacterField;
import com.github.jferard.jxbase.field.DateField;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.memo.MemoField;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;

public class ReaderWithMemoIT {
    @Test
    public void test1() throws FileNotFoundException, ParseException {
        final String databaseName = TestHelper.getResourceBase("memo1/texto.dbf");

        try {
            final Charset charset = Charset.forName("cp1252");
            final XBaseReader reader = XBaseReaderFactory.createReader(databaseName, charset);
            try {
                final XBaseMetadata meta = reader.getMetadata();
                System.out.println("Read DBF Metadata: " + meta);

                final XBaseFieldDescriptorArray array = reader.getFieldDescriptorArray();

                final XBaseDialect dialect = reader.getDialect();
                Assert.assertEquals(FoxProDialect.class, dialect.getClass());
                for (final XBaseField field : array.getFields()) {
                    final String name = field.getName();
                    if (name.equals("TEXVER")) {
                        Assert.assertEquals(5, field.getValueByteLength(dialect));
                        Assert.assertEquals(CharacterField.class, field.getClass());
                    } else if (name.equals("TEXTEX")) {
                        Assert.assertEquals(4, field.getValueByteLength(dialect));
                        Assert.assertEquals(MemoField.class, field.getClass());
                    } else if (name.equals("TEXDAT")) {
                        Assert.assertEquals(8, field.getValueByteLength(dialect));
                        Assert.assertEquals(DateField.class, field.getClass());
                    } else if (name.equals("TEXSTA")) {
                        Assert.assertEquals(1, field.getValueByteLength(dialect));
                        Assert.assertEquals(CharacterField.class, field.getClass());
                    } else if (name.equals("TEXCAM")) {
                        Assert.assertEquals(254, field.getValueByteLength(dialect));
                        Assert.assertEquals(CharacterField.class, field.getClass());
                    }
                }

                XBaseRecord rec;
                while ((rec = reader.read()) != null) {
                    System.out.println("Record is DELETED: " + rec.isDeleted());
                    System.out.println("Record: " + rec.getMap());
                    System.out.println("++++++++++++++++++++++++++++++++++");
                }

            } finally {
                reader.close();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } /*catch (final ParseException e) {
            e.printStackTrace();
        }*/
    }

    private File getResourceFile(final String name) {
        return new File(this.getClass().getClassLoader().getResource(name).getFile());
    }
}
