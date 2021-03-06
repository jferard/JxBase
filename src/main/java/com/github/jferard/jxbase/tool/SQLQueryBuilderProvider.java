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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A provider for SQLQueryBuilder.
 */
public class SQLQueryBuilderProvider {

    /**
     * @param connection a SQL connection
     * @return the provider
     * @throws SQLException
     */
    public static SQLQueryBuilderProvider create(final Connection connection) throws SQLException {
        final String productName = connection.getMetaData().getDatabaseProductName();
        return new SQLQueryBuilderProvider(productName);
    }

    private final String productName;

    SQLQueryBuilderProvider(final String productName) {
        this.productName = productName;
    }

    /**
     * Return a builder for this table
     * @param tableName a SQL table name
     * @return the builder
     * @throws IOException
     */
    public SQLQueryBuilder get(final String tableName) throws IOException {
        if ("SQLite".equals(this.productName)) {
            return SQLiteQueryBuilder.create(tableName);
        } else {
            throw new RuntimeException("");
        }
    }
}
