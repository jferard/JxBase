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

package com.github.jferard.jxbase.memo;

public enum MemoRecordTypeEnum {
    IMAGE(0x0), TEXT(0x1), OBJECT(0x2), NO_TYPE(0xff);

    public static MemoRecordTypeEnum fromInt(final int type) {
        for (final MemoRecordTypeEnum e : MemoRecordTypeEnum.values()) {
            if (e.type == type) {
                return e;
            }
        }
        return NO_TYPE;
    }

    public int getType() {
        return this.type;
    }

    final int type;

    MemoRecordTypeEnum(final int type) {
        this.type = type;
    }
}
