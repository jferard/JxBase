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

public enum XBaseFileTypeEnum {
    FoxBASE1(0x02, "FoxBASE"), FoxBASEPlus1(0x03, "FoxBASE+/Dbase III plus, no memo"),
    VisualFoxPro1(0x30, "Visual FoxPro"),
    VisualFoxPro2(0x31, "Visual FoxPro, autoincrement enabled"),
    dBASEIV1(0x43, "dBASE IV SQL table files, no memo"),
    dBASEIV2(0x63, "dBASE IV SQL system files, no memo"),
    FoxBASEPlus2(0x83, "FoxBASE+/dBASE III PLUS, with memo"), dBASEIV3(0x8B, "dBASE IV with memo"),
    dBASEIV4(0xCB, "dBASE IV SQL table files, with memo"),
    FoxPro2x(0xF5, "FoxPro 2.x (or earlier) with memo"), FoxBASE2(0xFB, "FoxBASE"),
    dBASEVII1(0x44, "dBASE VII SQL table files, no memo"),
    dBASEVII2(0x64, "dBASE VII SQL system files, no memo"), dBASEIVII3(0x8D, "dBASE VII with memo"),
    dBASEIVII4(0xCD, "dBASE VII SQL table files, with memo"),
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
                dialect = new GenericDialect(type);
                break;

        }
        return dialect;
    }

    final int type;
    final String description;

    XBaseFileTypeEnum(final int type, final String description) {
        this.type = type;
        this.description = description;
    }

    public byte toByte() {
        return (byte) this.type;
    }
}
