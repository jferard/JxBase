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
 * A container for optional bytes after the field descriptor array (including 0x0D) and the
 * beginning of the records.
 *
 * For Visual FoxPro: "A 263-byte range that contains the backlink information (the relative path
 * of an associated database (.dbc)). If the first byte is 0x00 then the file is not associated
 * with a database. Hence, database files themselves always contain 0x00."
 * (http://fox.wikis.com/wc.dll?Wiki~TableFileStructure~Wiki).
 *
 */
public interface XBaseOptional {
    /**
     * @return the length of the optional chunk
     */
    int getLength();

    /**
     * @return the content of the chunk.
     */
    byte[] getBytes();
}
