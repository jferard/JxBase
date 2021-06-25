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

package com.github.jferard.jxbase.memo;

/**
 * The type of memo data. By convention, non FoxPro memo record have the NO_TYPE type.
 */
public enum MemoRecordType {
    IMAGE(0x0), TEXT(0x1), OBJECT(0x2), NO_TYPE(0xff);

    /**
     * @param type the int type (FoxPro: the first 4 bytes of the memo block.)
     * @return the memo record type
     */
    public static MemoRecordType fromInt(final int type) {
        for (final MemoRecordType e : MemoRecordType.values()) {
            if (e.type == type) {
                return e;
            }
        }
        return NO_TYPE;
    }

    /**
     * @return the int type
     */
    public int getType() {
        return this.type;
    }

    final int type;

    MemoRecordType(final int type) {
        this.type = type;
    }
}
