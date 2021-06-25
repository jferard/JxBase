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

package com.github.jferard.jxbase.core;

/**
 * the type of the memo file associated with the dbf file.
 */
public enum XBaseMemoFileType {
    /**
     * No memo file allowed
     */
    NO_MEMO_FILE(""),

    /**
     * A regular (dbt) memo file
     */
    REGULAR_MEMO_FILE(".dbt"),

    /**
     * A foxpro (fpt) memo file.
     */
    FOXPRO_OBJECT_AND_MEMO_FILE(".fpt");

    private final String extension;

    XBaseMemoFileType(final String extension) {
        this.extension = extension;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return this.extension;
    }
}
