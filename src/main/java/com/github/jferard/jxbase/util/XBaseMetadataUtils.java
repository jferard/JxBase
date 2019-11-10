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

package com.github.jferard.jxbase.util;

public class XBaseMetadataUtils {
    public static final int BUFFER_SIZE = 8192;

    /*
    public static GenericMetadata fromFieldsString(final XBaseFileTypeEnum fileType, final Date
    updateDate,
                                                   final int recordQty, final XBaseLengths
                                                   dialect) {
        return fromFields(fileType, updateDate, recordQty, dialect);
    }

    public static GenericMetadata fromFields(final XBaseFileTypeEnum fileType, final Date
    updateDate,
                                             final int recordQty, final XBaseLengths dialect) {
        final List<XBaseField> fields = null;
        final int fullHeaderLength = calculateFullHeaderLength(fields);
        final int oneRecordLength = calculateOneRecordLength(fields, dialect);

        return GenericMetadata
                .create(fileType, updateDate, 0, recordQty, fullHeaderLength, oneRecordLength,
                        JdbfUtils.NULL_BYTE, JdbfUtils.NULL_BYTE);
    }

    private static int calculateFullHeaderLength(final List<XBaseField> fields) {
        int result = JdbfUtils.METADATA_SIZE;
        result += JdbfUtils.FIELD_RECORD_LENGTH * fields.size();
        return result + 1;
    }
     */
}
