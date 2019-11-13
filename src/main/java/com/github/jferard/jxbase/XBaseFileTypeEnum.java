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

package com.github.jferard.jxbase;

import com.github.jferard.jxbase.core.BasicDialect;
import com.github.jferard.jxbase.dialect.memo.WithMemoDialect;
import com.github.jferard.jxbase.dialect.foxpro.FoxProDialect;

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
     * '00000011'
     */
    FoxBASEPlus1(0x03, XBaseMemoFileType.NO_MEMO_FILE, "FoxBASE+/Dbase III plus, no memo"),
    /**
     * '00000100'
     */
    dBASEIV0(0x04, XBaseMemoFileType.NO_MEMO_FILE, "Dbase IV, no memo"),

    /**
     * '00000101'
     */
    dBASEV(0x05, XBaseMemoFileType.NO_MEMO_FILE, "Dbase V, no memo"),

    /**
     * '00000111'
     */
    VisualObjectsDbaseIII(0x07, XBaseMemoFileType.NO_MEMO_FILE,
            "VISUAL OBJECTS (first 1.0 versions) for the Dbase III files " + "w/o memo file"),
    /**
     * '00110000'
     */
    VisualFoxPro1(0x30, XBaseMemoFileType.FOXPRO_OBJECT_AND_MEMO_FILE, "Visual FoxPro"),
    /**
     * '00110001'
     */
    VisualFoxPro2(0x31, XBaseMemoFileType.FOXPRO_OBJECT_AND_MEMO_FILE,
            "Visual FoxPro, autoincrement enabled"),
    /**
     * 01000011 - 0x43
     */
    dBASEIV1(0x43, XBaseMemoFileType.NO_MEMO_FILE, "dBASE IV SQL table files, no memo"),
    /**
     * 01000100 - 0x44
     */
    dBASEVII1(0x44, XBaseMemoFileType.NO_MEMO_FILE, "dBASE VII SQL table files, no memo"),
    /**
     * 01100011 - 0x63
     */
    dBASEIV2(0x63, XBaseMemoFileType.NO_MEMO_FILE, "dBASE IV SQL system files, no memo"),
    /**
     * 01100100 - 0x64
     */
    dBASEVII2(0x64, XBaseMemoFileType.NO_MEMO_FILE, "dBASE VII SQL system files, no memo"),
    /**
     * 10000011 - 0x83
     */
    FoxBASEPlus2(0x83, XBaseMemoFileType.REGULAR_MEMO_FILE, "FoxBASE+/dBASE III PLUS, with memo"),
    /**
     * 10001011 - 0x8b
     */
    dBASEIV3(0x8B, XBaseMemoFileType.REGULAR_MEMO_FILE, "dBASE IV with memo"),
    /**
     * 10001101 - 0x8d
     */
    dBASEIVII3(0x8D, XBaseMemoFileType.REGULAR_MEMO_FILE, "dBASE VII with memo"),
    /**
     * 11001011 - 0xcb
     */
    dBASEIV4(0xCB, XBaseMemoFileType.REGULAR_MEMO_FILE, "dBASE IV SQL table files, with memo"),
    /**
     * 11001101 - 0xcd
     */
    dBASEIVII4(0xCD, XBaseMemoFileType.REGULAR_MEMO_FILE, "dBASE VII SQL table files, with memo"),
    /**
     * 11110101 - 0xf5
     */
    FoxPro2x(0xF5, XBaseMemoFileType.REGULAR_MEMO_FILE, "FoxPro 2.x (or earlier) with memo"),
    /**
     * 11111011 - 0xfb
     */
    FoxBASE2(0xFB, XBaseMemoFileType.REGULAR_MEMO_FILE, "FoxBASE"),
    ;

    public static XBaseFileTypeEnum fromInt(final byte bType) {
        final int iType = 0xFF & bType;
        for (final XBaseFileTypeEnum e : XBaseFileTypeEnum.values()) {
            if (e.type == iType) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown file type: " + bType);
    }

    public static XBaseDialect getDialect(final XBaseFileTypeEnum type) {
        final XBaseDialect dialect;
        switch (type) {
            case VisualFoxPro1:
            case VisualFoxPro2:
                dialect = new FoxProDialect(type);
                break;
            default:
                if (type.memoFileType() == XBaseMemoFileType.REGULAR_MEMO_FILE) {
                    dialect = new WithMemoDialect(type);
                } else {
                    dialect = new BasicDialect(type);
                }
                break;
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
