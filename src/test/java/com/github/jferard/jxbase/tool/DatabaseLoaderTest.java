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

package com.github.jferard.jxbase.tool;

import com.github.jferard.jxbase.XBaseReader;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseRecord;
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db3.DB3Access;
import com.github.jferard.jxbase.field.XBaseField;
import org.easymock.EasyMock;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Logger;

public class DatabaseLoaderTest {
    @Test
    public void testBuildTable() throws SQLException, IOException, ParseException {
        final Connection connection = PowerMock.createMock(Connection.class);
        final SQLQueryBuilderProvider provider =
                PowerMock.createMock(SQLQueryBuilderProvider.class);
        final Statement statement = PowerMock.createMock(Statement.class);
        final SQLQueryBuilder builder = PowerMock.createMock(SQLQueryBuilder.class);
        PowerMock.resetAll();

        EasyMock.expect(connection.createStatement()).andReturn(statement);
        EasyMock.expect(builder.createTable()).andReturn("CREATE TABLE");
        EasyMock.expect(statement.execute("CREATE TABLE")).andReturn(true);
        PowerMock.replayAll();

        final DatabaseLoader loader =
                new DatabaseLoader(Logger.getAnonymousLogger(), connection, provider, false, 5);
        loader.buildTable(builder);
        PowerMock.verifyAll();
    }

    @Test
    public void testFillTable() throws SQLException, IOException, ParseException {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("foo", "bar");

        final Connection connection = PowerMock.createMock(Connection.class);
        final SQLQueryBuilderProvider provider =
                PowerMock.createMock(SQLQueryBuilderProvider.class);
        final Statement statement = PowerMock.createMock(Statement.class);
        final SQLQueryBuilder builder = PowerMock.createMock(SQLQueryBuilder.class);
        final PreparedStatement pStatement = PowerMock.createMock(PreparedStatement.class);
        final XBaseReader<DB3Access, ?> reader = PowerMock.createMock(XBaseReader.class);
        final XBaseRecord record = PowerMock.createMock(XBaseRecord.class);
        final XBaseFieldDescriptorArray<DB3Access> array =
                PowerMock.createMock(XBaseFieldDescriptorArray.class);
        PowerMock.resetAll();

        EasyMock.expect(connection.createStatement()).andReturn(statement);
        connection.setAutoCommit(false);
        EasyMock.expect(builder.insertValues()).andReturn("INSERT INTO ?");
        EasyMock.expect(connection.prepareStatement("INSERT INTO ?")).andReturn(pStatement);

        EasyMock.expect(reader.getFieldDescriptorArray()).andReturn(array);
        EasyMock.expect(array.getFields())
                .andReturn(Collections
                        .<XBaseField<? super DB3Access>>singleton(new CharacterField("foo", 10)));

        EasyMock.expect(reader.read()).andReturn(record).times(5);
        EasyMock.expect(reader.read()).andReturn(null).once();

        EasyMock.expect(record.getMap()).andReturn(map).times(5);
        pStatement.setObject(1, "bar");
        EasyMock.expectLastCall().times(5);
        pStatement.addBatch();
        EasyMock.expectLastCall().times(5);
        EasyMock.expect(pStatement.executeBatch()).andReturn(new int[10]);
        connection.commit();
        connection.setAutoCommit(true);
        reader.close();
        PowerMock.replayAll();

        final DatabaseLoader loader =
                new DatabaseLoader(Logger.getAnonymousLogger(), connection, provider, false, 10);
        loader.fillTable(builder, reader);
        PowerMock.verifyAll();
    }
}