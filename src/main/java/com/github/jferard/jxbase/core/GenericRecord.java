/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

import com.github.jferard.jxbase.reader.XBaseMemoReader;

import java.util.Map;

public class GenericRecord {
    private final boolean isDeleted;
    private final int recordNumber;
    private final Map<String, Object> valueByFieldName;
    private final XBaseMemoReader memoReader;

    public GenericRecord(final boolean isDeleted, final int recordNumber,
                         final Map<String, Object> valueByFieldName,
                         final XBaseMemoReader memoReader) {
        this.isDeleted = isDeleted;
        this.recordNumber = recordNumber;
        this.valueByFieldName = valueByFieldName;
        this.memoReader = memoReader;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public int getRecordNumber() {
        return this.recordNumber;
    }

    public Map<String, Object> getMap() {
        return this.valueByFieldName;
    }
}
