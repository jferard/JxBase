/*
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

package com.github.jferard.jxbase.core;

/**
 * https://www.dbase.com/KnowledgeBase/int/db7_file_fmt.htm, Storage of dBASE Data Types
 */
// TODO: depends on the concrete format + search for other formats
public enum DbfFieldTypeEnum {
    /**
     * Binary, a string 	10 digits representing a .DBT block number. The number is stored as a
     * string, right justified and padded with blanks.
     */
    // Binary('B'),

    /**
     * Character 	All OEM code page characters - padded with blanks to the width of the field.
     */
    Character('C'),

    /**
     * Date 	8 bytes - date stored as a string in the format YYYYMMDD.
     */
    Date('D'),

    /**
     * Numeric 	Number stored as a string, right justified, and padded with blanks to the width of
     * the field.
     */
    Numeric('N'),

    /**
     * Logical 	1 byte - initialized to 0x20 (space) otherwise T or F.
     */
    Logical('L'),

    /**
     * Memo, a string 	10 digits (bytes) representing a .DBT block number. The number is stored
     * as a string, right justified and padded with blanks.
     */
    Memo('M'),

    /**
     * dbASE 7 Timestamp 	8 bytes - two longs, first for date, second for time.  The date is the
     * number
     * of days since  01/01/4713 BC. Time is hours * 3600000L + minutes * 60000L + Seconds * 1000L
     */
    Timestamp('@'),

    /**
     * Long 	4 bytes. Leftmost bit used to indicate sign, 0 negative.
     */
    Integer('I'),

    /**
     * Autoincrement 	Same as a Long
     */
    Autoincrement('+'),

    /**
     * Float 	Number stored as a string, right justified, and padded with blanks to the width of
     * the field.
     */
    Float('F'),

    /**
     * Double 	8 bytes - no conversions, stored as a double.
     */
    Double7('O'), // dBASE 7 binary double (standardized in contrast to 'B'

    /**
     * OLE 	10 digits (bytes) representing a .DBT block number. The number is stored as a string,
     * right justified and padded with blanks.
     */
    General('G'),

    /**
     * not dBase7
     */
    Currency('Y'),

    /**
     * @deprecated FoxPro-specific extension. Use Timestamp/@ with dBASE 7 or later
     */
    @Deprecated DateTime('T'),
    /**
     * @deprecated Binary doubles are FoxPro specific dBASE V uses B for binary MEMOs. Use
     * Double7, Float or Numeric instead
     */
    @Deprecated Double('B'),

    /**
     * not dBase7
     */
    Picture('P'),

    /**
     * not dBase7
     */
    NullFlags('0');

    public static DbfFieldTypeEnum fromChar(char type) {
        for (DbfFieldTypeEnum e : DbfFieldTypeEnum.values()) {
            if (e.type == type) {
                return e;
            }
        }
        return null;
    }

    final char type;

    DbfFieldTypeEnum(char type) {
        this.type = type;
    }

    public byte toByte() {
        return (byte) type;
    }

    public char getType() {
        return type;
    }
}
