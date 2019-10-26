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

import com.github.jferard.jxbase.core.DbfField;
import com.github.jferard.jxbase.core.DbfFieldTypeEnum;
import com.github.jferard.jxbase.core.DbfFileTypeEnum;
import com.github.jferard.jxbase.core.DbfMetadata;
import com.github.jferard.jxbase.util.DbfMetadataUtils;
import com.github.jferard.jxbase.writer.DbfWriter;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbfWriterIT {
    private final Map<String, Object> valueMap = new HashMap<String, Object>();
    private String filePath;
    private List<DbfField> fields = new ArrayList<DbfField>();

    public DbfField addCharDBFField(String name, int length) {
        final DbfField fld = new DbfField(name, DbfFieldTypeEnum.Character, length, 0);
        fields.add(fld);
        return fld;
    }

  /*if(encoding.equals("UTF-8"))
  lenght *= 2;*/

    public DbfField addNumDBFField(String name, int length, int decimal) {
        final DbfField fld = new DbfField(name, DbfFieldTypeEnum.Numeric, length, decimal);
        fields.add(fld);
        return fld;
    }

    public DbfField addDateDBFField(String name) {
        final DbfField fld = new DbfField(name, DbfFieldTypeEnum.Date, 0, 0);
        fields.add(fld);
        return fld;
    }

    @Before
    public void prepareData() {
        valueMap.put("FIOISP", "Виноградова Ольга Евгеньевна");
        valueMap.put("NAME", "Вячеслав");
        valueMap.put("SURNAME", "Егоров");
        valueMap.put("DATER", "30.06.1971");
        valueMap.put("SECONDNAME", "Иванович");
        valueMap.put("UNICODE", new BigDecimal(1001731864));
        valueMap.put("NUMID", "6/14/19/69");
        filePath = "G:\\test\\" + new Date().getTime() + ".dbf";
        fields.add(addCharDBFField("FIOISP", 100));
        fields.add(addCharDBFField("NAME", 250));
        fields.add(addCharDBFField("SURNAME", 250));
        fields.add(addCharDBFField("DATER", 10));
        fields.add(addCharDBFField("SECONDNAME", 250));
        fields.add(addNumDBFField("UNICODE", 10, 10));
        fields.add(addCharDBFField("NUMID", 100));
    }

    @Test
    public void test() throws IOException {
        DbfMetadata dbfMetadata = new DbfMetadata();
        dbfMetadata.setFields(fields);
        dbfMetadata.setOneRecordLength(DbfMetadataUtils.calculateOneRecordLength(fields));
        dbfMetadata.setType(DbfFileTypeEnum.FoxBASE2);
        FileOutputStream fos = null;
        DbfWriter dbfWriter = null;
        try {
            fos = new FileOutputStream("111.dbf");
            dbfWriter = new DbfWriter(dbfMetadata, fos);
            final String encoding = "CP866";
            dbfWriter.setStringCharset(encoding);
            dbfWriter.write(valueMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dbfWriter != null) {
                dbfWriter.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
}
