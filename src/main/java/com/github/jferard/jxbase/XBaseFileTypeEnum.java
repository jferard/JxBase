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

package com.github.jferard.jxbase;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * dBASE IV bit flags:
 * 0-2  Version no. i.e. 0-7
 * 3    Presence of memo file
 * 4-6  Presence of SQL table
 * 7    DBT flag
 */
public enum XBaseFileTypeEnum {
    /**
     * '00000010'
     */
    FoxBASE1(0x02, XBaseMemoFileType.NO_MEMO_FILE, "FoxBASE/Dbase II"),

    /**
     * '00000010'
     */
    dBASE2(0x02, XBaseMemoFileType.NO_MEMO_FILE, "FoxBASE/Dbase II"),

    /**
     * '00000011'
     */
    FoxBASEPlus1(0x03, XBaseMemoFileType.NO_MEMO_FILE, "FoxBASE+/Dbase III plus, no memo"),

    /**
     * '00000011'
     */
    dBASE3plus(0x03, XBaseMemoFileType.NO_MEMO_FILE, "FoxBASE+/Dbase III plus, no memo"),

    /**
     * '00000100'
     */
    dBASE4(0x04, XBaseMemoFileType.NO_MEMO_FILE, "Dbase IV, no memo"),

    /**
     * '00000101'
     */
    dBASE5(0x05, XBaseMemoFileType.NO_MEMO_FILE, "Dbase V, no memo"),

    /**
     * '00000111'
     */
    VisualObjectsDBASE3(0x07, XBaseMemoFileType.NO_MEMO_FILE,
            "VISUAL OBJECTS (first 1.0 versions) for the Dbase III files w/o memo file"),

    /**
     * '00110000'
     */
    VisualFoxPro(0x30, XBaseMemoFileType.FOXPRO_OBJECT_AND_MEMO_FILE, "Visual FoxPro"),

    /**
     * '00110001'
     */
    VisualFoxProAutoIncrement(0x31, XBaseMemoFileType.FOXPRO_OBJECT_AND_MEMO_FILE,
            "Visual FoxPro, autoincrement enabled"),

    /**
     * 01000011 - 0x43
     */
    dBASE4SQLTable(0x43, XBaseMemoFileType.NO_MEMO_FILE, "dBASE IV SQL table files, no memo"),

    /**
     * 01000100 - 0x44
     */
    dBASE7SQLTable(0x44, XBaseMemoFileType.NO_MEMO_FILE, "dBASE VII SQL table files, no memo"),

    /**
     * 01100011 - 0x63
     */
    dBASE4SQLSystem(0x63, XBaseMemoFileType.NO_MEMO_FILE, "dBASE IV SQL system files, no memo"),

    /**
     * 01100100 - 0x64
     */
    dBASE7SQLSystem(0x64, XBaseMemoFileType.NO_MEMO_FILE, "dBASE VII SQL system files, no memo"),

    /**
     * 10000011 - 0x83
     */
    dBASE3plusMemo(0x83, XBaseMemoFileType.REGULAR_MEMO_FILE, "FoxBASE+/dBASE III PLUS, with memo"),

    /**
     * 10001011 - 0x8b
     */
    dBASE4Memo(0x8B, XBaseMemoFileType.REGULAR_MEMO_FILE, "dBASE IV with memo"),

    /**
     * 10001101 - 0x8d
     */
    dBASE7Memo(0x8D, XBaseMemoFileType.REGULAR_MEMO_FILE, "dBASE VII with memo"),

    /**
     * 11001011 - 0xcb
     */
    dBASE4SQLTableMemo(0xCB, XBaseMemoFileType.REGULAR_MEMO_FILE,
            "dBASE IV SQL table files, with memo"),

    /**
     * 11001101 - 0xcd
     */
    dBASE7SQLTableMemo(0xCD, XBaseMemoFileType.REGULAR_MEMO_FILE,
            "dBASE VII SQL table files, with memo"),

    /**
     * 11110101 - 0xf5
     */
    FoxPro2xMemo(0xF5, XBaseMemoFileType.FOXPRO_OBJECT_AND_MEMO_FILE, "FoxPro 2.x (or earlier) with memo"),

    /**
     * 11111011 - 0xfb
     */
    FoxBASEMemo(0xFB, XBaseMemoFileType.REGULAR_MEMO_FILE, "FoxBASE");

    public static XBaseFileTypeEnum fromInt(final byte bType) {
        final int iType = 0xFF & bType;
        for (final XBaseFileTypeEnum e : XBaseFileTypeEnum.values()) {
            if (e.type == iType) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown file type: " + bType);
    }

    /**
     * @param type
     * @param tableName
     * @param charset
     * @param memoHeaderMeta null if reader only
     * @return
     * @throws IOException
     */
    public static XBaseDialect<?, ?> getDialect(final XBaseFileTypeEnum type,
                                                final String tableName, final Charset charset,
                                                final Map<String, Object> memoHeaderMeta)
            throws IOException {
        final XBaseDialect<?, ?> dialect;
        if (type.memoFileType() == XBaseMemoFileType.NO_MEMO_FILE) {
            dialect = DialectFactory.getNoMemoDialect(type, charset);
        } else if (memoHeaderMeta == null) {
            dialect = DialectFactory.getMemoReaderDialect(type, tableName, charset);
        } else {
            dialect = DialectFactory.getMemoWriterDialect(type, tableName, charset, memoHeaderMeta);
        }
        return dialect;
    }

    final int type;
    final String description;
    private final XBaseMemoFileType memoFileType;

    XBaseFileTypeEnum(final int type, final XBaseMemoFileType memoFileType,
                      final String description) {
        this.type = type;
        this.memoFileType = memoFileType;
        this.description = description;
    }

    public byte toByte() {
        return (byte) this.type;
    }

    public XBaseMemoFileType memoFileType() {
        return this.memoFileType;
    }
}
