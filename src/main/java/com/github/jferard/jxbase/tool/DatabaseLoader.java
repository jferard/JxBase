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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A loader for a data base.
 *
 * Example:
 *
 * ...$ java -cp "$HOME/.m2/repository/org/xerial/sqlite-jdbc/3.32.3.2/sqlite-jdbc-3.32.3.2.jar:$HOME/.m2/repository/com/github/jferard/jxbase/0.0.1-SNAPSHOT/jxbase-0.0.1-SNAPSHOT.jar" com.github.jferard.jxbase.tool.DatabaseLoader $HOME/prog/java/jxbase/src/test/resources/data1 "jdbc:sqlite:./test.db"
 *
 */
public class DatabaseLoader {
    /**
     * @param args the args
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException
     * @throws ParseException
     */
    public static void main(final String[] args)
            throws ClassNotFoundException, SQLException, IOException, ParseException {
        final LoaderArgs loarderArgs = new LoaderArgsParser().parse(args);
        if (loarderArgs == null) {
            return;
        }
        if (loarderArgs.classForName != null) {
            Class.forName(loarderArgs.classForName);
        }
        final Connection connection = DriverManager.getConnection(loarderArgs.url);
        try {
            final DatabaseLoader loader =
                    new DatabaseLoader(Logger.getAnonymousLogger(), connection,
                            SQLQueryBuilderProvider.create(connection), loarderArgs.dropTable,
                            loarderArgs.chunkSize);
            if (loarderArgs.source.isFile()) {
                loader.buildAndFillTable(loarderArgs.source);
            } else if (loarderArgs.source.isDirectory()) {
                loader.buildAndFillTables(loarderArgs.source);
            } else {
                System.err.println("Unknown source " + loarderArgs.source);
            }
        } finally {
            connection.close();
        }
    }

    /**
     * Create a new database loader.
     * @param logger  the logger
     * @param connection the SQL connection
     * @return a loader
     * @throws SQLException
     */
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

    /**
     * Load all the tables in a directory
     * @param baseDirectory  the directory
     * @throws SQLException
     * @throws IOException
     * @throws ParseException
     */
    public void buildAndFillTables(final File baseDirectory)
            throws SQLException, IOException, ParseException {
        this.logger.info("Build tables");
        for (final File filename : baseDirectory.listFiles(new DbfFileFilter())) {
            this.buildAndFillTable(new File(baseDirectory, filename.getName()));
        }
    }

    /**
     * Load one table from the filename
     * @param filename  the file name
     * @throws SQLException
     * @throws IOException
     * @throws ParseException
     */
    public void buildAndFillTable(final File filename)
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

    private void fillTable(final SQLQueryBuilder builder,
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
        for (int i = 0; i != this.chunkSize; i++) {
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
