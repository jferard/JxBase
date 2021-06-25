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

import java.util.Set;

/**
 * The meta data of a file. Contains info about the file.
 */
public interface XBaseMetadata {
    /**
     * @return the file type
     */
    int getFileTypeByte();

    /**
     * @return the length of the header
     */
    int getFullHeaderLength();

    /**
     * @return the length of a record
     */
    int getOneRecordLength();

    /**
     * @param key the key
     * @return the value mapped to the key
     */
    Object get(String key);

    /**
     * @return the set of keys
     */
    Set<String> keySet();
}
