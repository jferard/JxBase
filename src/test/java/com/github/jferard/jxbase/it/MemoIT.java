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

import com.github.jferard.jxbase.core.DbfFieldTypeEnum;
import com.github.jferard.jxbase.core.DbfMetadata;
import com.github.jferard.jxbase.core.DbfRecord;
import com.github.jferard.jxbase.reader.DbfReader;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class MemoIT {

    @Test
    public void test1() throws FileNotFoundException {
        Charset stringCharset = Charset.forName("cp1252");

        File dbf = getResourceFile("memo1/texto.dbf");
        File memo = getResourceFile("memo1/texto.fpt");

        try {
            DbfReader reader = DbfReader.create(dbf, memo);
            try {
                DbfMetadata meta = reader.getMetadata();
                System.out.println("Read DBF Metadata: " + meta);

                assertEquals(5, meta.getOffsetField("TEXVER").getField().getLength());
                assertEquals(DbfFieldTypeEnum.Character,
                        meta.getOffsetField("TEXVER").getField().getType());

                assertEquals(4, meta.getOffsetField("TEXTEX").getField().getLength());
                assertEquals(DbfFieldTypeEnum.Memo,
                        meta.getOffsetField("TEXTEX").getField().getType());

                assertEquals(8, meta.getOffsetField("TEXDAT").getField().getLength());
                assertEquals(DbfFieldTypeEnum.Date,
                        meta.getOffsetField("TEXDAT").getField().getType());

                assertEquals(1, meta.getOffsetField("TEXSTA").getField().getLength());
                assertEquals(DbfFieldTypeEnum.Character,
                        meta.getOffsetField("TEXSTA").getField().getType());

                assertEquals(254, meta.getOffsetField("TEXCAM").getField().getLength());
                assertEquals(DbfFieldTypeEnum.Character,
                        meta.getOffsetField("TEXCAM").getField().getType());

                DbfRecord rec;
                while ((rec = reader.read()) != null) {
                    rec.setStringCharset(stringCharset);

                    System.out.println("Record is DELETED: " + rec.isDeleted());
                    System.out.println("TEXVER: " + rec.getString("TEXVER"));
                    System.out.println("TEXTEX: " + rec.getMemoAsString("TEXTEX"));
                    System.out.println("TEXDAT: " + rec.getDate("TEXDAT"));
                    System.out.println("TEXSTA: " + rec.getString("TEXSTA"));
                    System.out.println("TEXCAM: " + rec.getString("TEXCAM"));
                    System.out.println("++++++++++++++++++++++++++++++++++");
                }

            } finally {
                reader.close();
            }
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private File getResourceFile(String name) {
        return new File(getClass().getClassLoader().getResource(name).getFile());
    }
}
