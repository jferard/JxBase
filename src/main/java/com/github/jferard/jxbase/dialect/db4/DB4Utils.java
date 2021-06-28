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

package com.github.jferard.jxbase.dialect.db4;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An utility class
 */
public class DB4Utils {
    // see https://www.clicketyclick.dk/databases/xbase/format/dbt.html#DBT_STRUCT
    public static final byte[] MEMO_FIELD_RESERVED_BYTES = {(byte) 0xff, (byte) 0xff, 0x08, 0x00};

    public static final String META_UPDATE_DATE = "updateDate";
    public static final String META_RECORDS_QTY = "recordsQty";
    public static final String META_UNCOMPLETED_TX_FLAG = "uncompletedTxFlag";
    public static final String META_ENCRYPTION_FLAG = "encryptionFlag";
    public static final String META_MDX_FLAG = "mdxFlag";
    public static final String META_LANGUAGE_DRIVER_ID = "languageDriverId";

    public static void writeFlag1(final OutputStream out, final Object f) throws IOException {
        if (f == null) {
            out.write(0);
        } else if (f instanceof Number) {
            out.write(((Number) f).byteValue());
        } else {
            throw new IllegalArgumentException();
        }
    }

    private DB4Utils() {}
}
