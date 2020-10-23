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

import java.io.IOException;

/**
 * A query builder for a given table.
 */
public interface SQLQueryBuilder {
    /**
     * @return the DROP TABLE query.
     */
    String dropTable();

    /**
     * @return the CREATE TABLE query
     * @throws IOException
     */
    String createTable() throws IOException;

    /**
     * @return the INSERT VALUES query
     */
    String insertValues();

    /**
     * @return the number of columns of the table
     */
    int getColumnsSize();
}
