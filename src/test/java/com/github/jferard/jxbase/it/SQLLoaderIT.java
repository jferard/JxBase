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

package com.github.jferard.jxbase.it;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.tool.DatabaseLoader;
import com.github.jferard.jxbase.tool.SQLQueryBuilderProvider;
import com.github.jferard.jxbase.tool.SQLiteQueryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Logger;

public class SQLLoaderIT {
    @Test
    public void testQuery() throws IOException {
        final String tableName = TestHelper.getResourceTableName("data1/gds_im.dbf");
        final SQLiteQueryBuilder builder = SQLiteQueryBuilder.create(tableName);
        Assert.assertEquals("DROP TABLE IF EXISTS \"gds_im\"", builder.dropTable());
        Assert.assertEquals("CREATE TABLE \"gds_im\" (\n" +
                "    \"KONTR\" text,\n" +
                "    \"N_MDP\" text,\n" +
                "    \"W_LIST_NO\" numeric,\n" +
                "    \"G32\" numeric,\n" +
                "    \"N_RECEIVER\" numeric,\n" +
                "    \"G33\" text,\n" +
                "    \"G312\" text,\n" +
                "    \"G35\" numeric,\n" +
                "    \"G311\" text,\n" +
                "    \"G318\" text,\n" +
                "    \"G315\" numeric,\n" +
                "    \"G317C\" text,\n" +
                "    \"G221\" text,\n" +
                "    \"G221_BUK\" text,\n" +
                "    \"G42\" numeric,\n" +
                "    \"KODS_PT\" text,\n" +
                "    \"KODS_ABC2\" text,\n" +
                "    \"N_TTH\" text,\n" +
                "    \"G442REGNU\" text,\n" +
                "    \"DELIV_PPP\" text,\n" +
                "    \"G40T\" text,\n" +
                "    \"G40\" text,\n" +
                "    \"G405\" numeric,\n" +
                "    \"TOV_SIGN2\" text,\n" +
                "    \"CREATEDATE\" integer,\n" +
                "    \"MODIFIED_D\" integer,\n" +
                "    \"ARM_ID\" numeric,\n" +
                "    \"VERSION\" text\n" +
                ")", builder.createTable());
        Assert.assertEquals(
                "INSERT INTO \"gds_im\" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                builder.insertValues());
    }

    @Test
    public void testLoader() {
        final String url = "jdbc:sqlite:./test.db";
        try {
            final Connection connection = DriverManager.getConnection(url);
            try {
                final String databaseDir = TestHelper.getResourceTableName("data1/gds_im.dbf");
                final File baseDirectory = new File(new File(databaseDir).getParent());
                final DatabaseLoader loader =
                        new DatabaseLoader(Logger.getAnonymousLogger(), connection,
                                SQLQueryBuilderProvider.create(connection), true, 10000);
                loader.buildAndFillTables(baseDirectory);
            } finally {
                connection.close();
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        } catch (final ParseException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}