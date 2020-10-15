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

import com.github.jferard.jxbase.XBaseReader;
import com.github.jferard.jxbase.XBaseReaderFactory;
import com.github.jferard.jxbase.core.XBaseRecord;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.IOUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Map;
import java.util.logging.Logger;

public class DatabaseLoader {
    public static DatabaseLoader create(final Logger logger, final Connection connection)
            throws SQLException {
        final SQLQueryBuilderProvider provider = SQLQueryBuilderProvider.create(connection);
        return new DatabaseLoader(logger, connection, provider, false, 10000);
    }

    private final Logger logger;
    private final Connection connection;
    private final Statement statement;
    private final SQLQueryBuilderProvider provider;
    private final boolean dropTable;
    private final int chunkSize;

    public DatabaseLoader(final Logger logger, final Connection connection,
                          final SQLQueryBuilderProvider provider, final boolean dropTable,
                          final int chunkSize) throws
            SQLException {
        this.logger = logger;
        this.connection = connection;
        this.provider = provider;
        this.statement = connection.createStatement();
        this.dropTable = dropTable;
        this.chunkSize = chunkSize;
    }

    public void buildAndFillTables(final File baseDirectory)
            throws SQLException, IOException, ParseException {
        this.logger.info("Build tables");
        for (final File filename : baseDirectory.listFiles(new DbfFileFilter())) {
            this.buildAndFillTable(new File(baseDirectory, filename.getName()));
        }
    }

    private void buildAndFillTable(final File filename)
            throws IOException, SQLException, ParseException {
        this.logger.info(" > " + filename);
        final String filenameWithoutExt = IOUtils.removeExtension(filename);
        final SQLQueryBuilder builder = this.provider.get(filenameWithoutExt);
        this.buildTable(builder);
        this.fillTable(builder, filenameWithoutExt);
    }

    private void buildTable(final SQLQueryBuilder builder) throws IOException, SQLException {
        if (this.dropTable) {
            final String dropQuery = builder.dropTable();
            this.logger.fine(dropQuery);
            this.statement.execute(dropQuery);
        }
        final String createQuery = builder.createTable();
        this.logger.fine(createQuery);
        this.statement.execute(createQuery);
    }

    public void fillTable(final SQLQueryBuilder builder,
                          final String filenameWithoutExt)
            throws SQLException, IOException, ParseException {
        this.connection.setAutoCommit(false);
        final String sql = builder.insertValues();
        this.logger.fine(sql);
        final PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        final XBaseReader<?, ?> reader =
                XBaseReaderFactory.createReader(filenameWithoutExt, JxBaseUtils.LATIN1_CHARSET);
        try {
            while (this.addBatchRows(preparedStatement, reader)) {
                preparedStatement.executeBatch();
                this.connection.commit();
            }
        } finally {
            reader.close();
        }
        this.connection.setAutoCommit(true);
    }

    private boolean addBatchRows(final PreparedStatement preparedStatement,
                                 final XBaseReader<?, ?> reader)
            throws IOException, SQLException, ParseException {
        for (int i = 0; i < this.chunkSize; i++) {
            final XBaseRecord record = reader.read();
            if (record == null) {
                return i != 0;
            }
            final Map<String, Object> map = record.getMap();
            int j = 1;
            for (final XBaseField<?> field : reader.getFieldDescriptorArray().getFields()) {
                preparedStatement.setObject(j++, map.get(field.getName()));
            }
            preparedStatement.addBatch();
        }
        return true;
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    private static class DbfFileFilter implements FileFilter {
        @Override
        public boolean accept(final File pathname) {
            return pathname.isFile() && "dbf".equalsIgnoreCase(IOUtils.getExtension(pathname));
        }
    }
}
