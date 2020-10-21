/*
 * JxBase - Copyright (c) 2019-2020 Julien Férard
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

import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.XBaseMetadata;

import java.util.Map;
import java.util.Set;


/**
 * Represents the file meta data, that is the first part of the header
 */
public class GenericMetadata implements XBaseMetadata {
    private final byte typeByte;
    private final int fullHeaderLength;
    private final int oneRecordLength;
    private final Map<String, Object> meta;

    public GenericMetadata(final byte typeByte, final int fullHeaderLength,
                           final int oneRecordLength, final Map<String, Object> meta) {
        this.typeByte = typeByte;
        this.fullHeaderLength = fullHeaderLength;
        this.oneRecordLength = oneRecordLength;
        this.meta = meta;
    }

    @Override
    public int getFileTypeByte() {
        return this.typeByte;
    }

    @Override
    public int getFullHeaderLength() {
        return this.fullHeaderLength;
    }

    @Override
    public int getOneRecordLength() {
        return this.oneRecordLength;
    }

    @Override
    public Object get(final String key) {
        return this.meta.get(key);
    }

    @Override
    public Set<String> keySet() {
        return this.meta.keySet();
    }

    @Override
    public String toString() {
        return "GenericMetadata[type=" + this.typeByte + " (" +
                XBaseFileTypeEnum.fromInt(this.typeByte) + "), fullHeaderLength=" + this.
                fullHeaderLength + ", oneRecordLength=" + this.oneRecordLength + ", meta=" +
                this.meta + "]";
    }
}
