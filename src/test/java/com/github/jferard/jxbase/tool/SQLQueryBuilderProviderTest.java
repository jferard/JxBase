/*
 * JxBase - Copyright (c) 2019-2020 Julien Férard
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

package com.github.jferard.jxbase.tool;

import com.github.jferard.jxbase.TestHelper;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class SQLQueryBuilderProviderTest {
    @Test
    public void test() throws SQLException, IOException {
        final Connection connection = PowerMock.createMock(Connection.class);
        final DatabaseMetaData metaData = PowerMock.createMock(DatabaseMetaData.class);
        PowerMock.resetAll();

        EasyMock.expect(connection.getMetaData()).andReturn(metaData);
        EasyMock.expect(metaData.getDatabaseProductName()).andReturn("SQLite");
        PowerMock.replayAll();

        final SQLQueryBuilderProvider provider = SQLQueryBuilderProvider.create(connection);
        final SQLQueryBuilder foo = provider.get(TestHelper.getResourceTableName("data1/gds_im.dbf"));
        PowerMock.verifyAll();

        Assert.assertEquals(SQLiteQueryBuilder.class, foo.getClass());
    }

}