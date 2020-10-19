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

package com.github.jferard.jxbase.tool;

import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.logging.Logger;

public class DatabaseLoaderTest {
    @Test
    public void test() throws SQLException, IOException, ParseException {
        /*
        final Connection connection = PowerMock.createMock(Connection.class);
        final SQLQueryBuilderProvider provider =
                PowerMock.createMock(SQLQueryBuilderProvider.class);
        final File file = PowerMock.createMock(File.class);
        Statement statement = PowerMock.createMock(Statement.class);
        SQLQueryBuilder builder = PowerMock.createMock(SQLQueryBuilder.class);
        PreparedStatement pStatement = PowerMock.createMock(PreparedStatement.class);
        PowerMock.resetAll();

        EasyMock.expect(connection.createStatement()).andReturn(statement);
        EasyMock.expect(file.isFile()).andReturn(true);
        EasyMock.expect(file.getAbsolutePath()).andReturn("foo.dbf");
        EasyMock.expect(provider.get("foo")).andReturn(builder);
        EasyMock.expect(builder.createTable()).andReturn("CREATE TABLE");
        EasyMock.expect(statement.execute("CREATE TABLE")).andReturn(true);
        connection.setAutoCommit(false);
        EasyMock.expect(builder.insertValues()).andReturn("INSERT INTO ?");
        EasyMock.expect(connection.prepareStatement("INSERT INTO ?")).andReturn(pStatement);
        PowerMock.replayAll();

        final DatabaseLoader loader =
                new DatabaseLoader(Logger.getAnonymousLogger(), connection, provider, false, 5);
        loader.buildAndFillTable(file);
        PowerMock.verifyAll();
        */
    }
}