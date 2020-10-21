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
import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.dialect.db2.field.LogicalField;
import com.github.jferard.jxbase.dialect.db2.field.NumericField;
import com.github.jferard.jxbase.dialect.db3.field.DateField;
import com.github.jferard.jxbase.dialect.db3.field.MemoField;
import com.github.jferard.jxbase.dialect.db4.field.FloatField;
import com.github.jferard.jxbase.dialect.foxpro.field.DatetimeField;
import com.github.jferard.jxbase.dialect.foxpro.field.IntegerField;
import com.github.jferard.jxbase.dialect.foxpro.field.NullFlagsField;
import com.github.jferard.jxbase.field.XBaseField;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SQLiteQueryBuilder implements SQLQueryBuilder {
    private final String tableShortName;
    private final Collection<XBaseField<?>> fields;

    public static SQLiteQueryBuilder create(final String tableName) throws IOException {
        final String tableShortName = new File(tableName).getName();
        Collection<XBaseField<?>> fields = null;
        final XBaseReader<?, ?> reader =
                XBaseReaderFactory.createReader(tableName, JxBaseUtils.LATIN1_CHARSET);
        try {
            fields = (Collection<XBaseField<?>>) reader.getFieldDescriptorArray().getFields();
        } finally {
            reader.close();
        }
        return new SQLiteQueryBuilder(tableShortName, fields);
    }

    SQLiteQueryBuilder(final String tableShortName, final Collection<XBaseField<?>> fields) {
        this.tableShortName = tableShortName;
        this.fields = fields;
    }

    @Override
    public String dropTable() {
        return "DROP TABLE IF EXISTS \"" + this.tableShortName + "\"";
    }

    @Override
    public String createTable() throws IOException {
        final StringBuilder sb = new StringBuilder(
                "CREATE TABLE \"").append(this.tableShortName).append("\" (\n");
        final List<String> definitions = new ArrayList<String>(this.fields.size());
        for (final XBaseField<?> field : this.fields) {
            definitions.add(this.fieldDefinition(field));
        }
        sb.append(JxBaseUtils.join(",\n", definitions));
        sb.append("\n)");
        return sb.toString();
    }

    private String fieldDefinition(final XBaseField<?> field) {
        return "    \"" + field.getName() + "\" " + this.SQLType(field);
    }

    private String SQLType(final XBaseField<?> field) {
        if (field instanceof CharacterField) {
            return "text";
        } else if (field instanceof DateField) {
            return "integer";
        } else if (field instanceof DatetimeField) {
            return "real";
        } else if (field instanceof FloatField) {
            return "real";
        } else if (field instanceof IntegerField) {
            return "integer";
        } else if (field instanceof LogicalField) {
            return "integer";
        } else if (field instanceof MemoField) {
            return "blob";
        } else if (field instanceof NullFlagsField) {
            return "integer";
        } else if (field instanceof NumericField) {
            return "numeric";
        } else {
            throw new IllegalArgumentException(String.format("`%s` is not a colType", field));
        }
    }

    @Override
    public String insertValues() {
        return "INSERT INTO \"" + this.tableShortName + "\" VALUES (" +
                JxBaseUtils.join(", ", Collections.nCopies(this.fields.size()
                        , "?")) + ")";
    }

    @Override
    public int getValuesSize() {
        return this.fields.size();
    }
}
