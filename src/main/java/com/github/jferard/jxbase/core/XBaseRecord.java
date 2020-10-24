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

package com.github.jferard.jxbase.core;

import java.util.Map;

/**
 * A record
 */
public class XBaseRecord {
    private final boolean isDeleted;
    private final int recordNumber;
    private final Map<String, Object> valueByFieldName;

    public XBaseRecord(final boolean isDeleted, final int recordNumber,
                       final Map<String, Object> valueByFieldName) {
        this.isDeleted = isDeleted;
        this.recordNumber = recordNumber;
        this.valueByFieldName = valueByFieldName;
    }

    /**
     * @return true if the record is marked as deleted
     */
    public boolean isDeleted() {
        return this.isDeleted;
    }

    /**
     * @return the number of this record
     */
    public int getRecordNumber() {
        return this.recordNumber;
    }

    /**
     * @return the record data as a map
     */
    public Map<String, Object> getMap() {
        return this.valueByFieldName;
    }
}
