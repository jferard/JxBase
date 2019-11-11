/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

import com.github.jferard.jxbase.core.field.CharacterField;
import com.github.jferard.jxbase.core.field.DateField;
import com.github.jferard.jxbase.core.field.FieldRepresentation;
import com.github.jferard.jxbase.core.field.IntegerField;
import com.github.jferard.jxbase.core.field.LogicalField;
import com.github.jferard.jxbase.core.field.MemoField;
import com.github.jferard.jxbase.core.field.NullFlagsField;
import com.github.jferard.jxbase.core.field.NumericField;
import com.github.jferard.jxbase.core.field.SmallMemoField;
import com.github.jferard.jxbase.core.field.XBaseField;
import com.github.jferard.jxbase.core.memo.XBaseMemoRecord;
import com.github.jferard.jxbase.reader.internal.XBaseRecordReader;
import com.github.jferard.jxbase.util.JxBaseUtils;

public class GenericDialect implements XBaseDialect {
    private final XBaseFileTypeEnum type;

    public GenericDialect(final XBaseFileTypeEnum type) {
        this.type = type;
    }

    @Override
    public int getCharacterValueLength(final int length) {
        return length;
    }

    @Override
    public FieldRepresentation getCharacterFieldRepresentation(final String name, final int dataSize) {
        if (dataSize > 254) {
            throw new IllegalArgumentException("Use FoxPro for long character fields");
        }
        return new FieldRepresentation(name, 'C', dataSize,0);
    }

    @Override
    public int getDateValueLength() {
        return 8;
    }

    @Override
    public FieldRepresentation getDateFieldRepresentation(final String name) {
        return new FieldRepresentation(name, 'D', 8,0);
    }

    @Override
    public int getIntegerValueLength() {
        return 4;
    }

    @Override
    public FieldRepresentation getIntegerFieldRepresentation(final String name) {
        return new FieldRepresentation(name, 'I', 4,0);
    }

    @Override
    public int getLogicalValueLength() {
        return 1;
    }

    @Override
    public FieldRepresentation getLogicalFieldRepresentation(final String name) {
        return new FieldRepresentation(name, 'L', 1,0);
    }

    @Override
    public int getMemoValueLength() {
        return 10;
    }

    @Override
    public FieldRepresentation getMemoFieldRepresentation(final String name) {
        return new FieldRepresentation(name, 'M', 10,0);
    }

    @Override
    public int getNumericValueLength(final int length) {
        return length;
    }

    @Override
    public FieldRepresentation getNumericFieldRepresentation(final String name, final int dataSize,
                                                final int numberOfDecimalPlaces) {
        return new FieldRepresentation(name, 'N', dataSize,numberOfDecimalPlaces);
    }

    @Override
    public XBaseField fromStringRepresentation(final String s) {
        final String[] a = s.split(",");

        final String name = a[0];
        final byte typeByte = (byte) a[1].charAt(0);
        final int length = Integer.parseInt(a[2]);
        final int numberOfDecimalPlaces = Integer.parseInt(a[3]);
        return this.getXBaseField(name, typeByte, length, numberOfDecimalPlaces);
    }

    @Override
    public <T extends XBaseMemoRecord<?>> XBaseField getXBaseField(final String name,
                                                                   final byte typeByte,
                                                                   final int length,
                                                                   final int numberOfDecimalPlaces) {
        switch (typeByte) {
            case 'D':
                if (length != 8) {
                    throw new IllegalArgumentException("A date has 8 chars");
                }
                return new DateField(name);
            case 'C':
                return new CharacterField(name, length);
            case 'N':
            case 'B':
                return new NumericField(name, length, numberOfDecimalPlaces);
            case 'L':
                if (length != 1) {
                    throw new IllegalArgumentException("A boolean has one char");
                }
                return new LogicalField(name);
            case 'I':
                return new IntegerField(name);
            case 'M':
                if (length == 4) {
                    return new SmallMemoField<T>(name);
                } else if (length == 10) {
                    return new MemoField<T>(name);
                } else {
                    throw new IllegalArgumentException();
                }
            case '0':
                return new NullFlagsField(name, length);
            default:
                throw new IllegalArgumentException(
                        String.format("'%c' (%d) is not a dbf field type", typeByte, typeByte));
        }
    }

    /**
     * https://www.clicketyclick.dk/databases/xbase/format/data_types.html:
     * > Pointer to ASCII text field in memo file 10 digits representing a pointer to a DBT block
     * (default is blanks).
     *
     * @param recordReader
     * @param recordBuffer
     * @param offset
     * @param length
     * @return
     */
    public long getOffsetInBlocks(final XBaseRecordReader recordReader, final byte[] recordBuffer,
                                  final int offset, final int length) {
        final String s = recordReader.getTrimmedASCIIString(recordBuffer, offset, length);
        return Long.valueOf(s);
    }

    @Override
    public XBaseFileTypeEnum getType() {
        return this.type;
    }

    @Override
    public int getMetaDataLength() {
        return JxBaseUtils.METADATA_LENGTH;
    }

    @Override
    public int getFieldDescriptorLength() {
        return JxBaseUtils.FIELD_RECORD_LENGTH;
    }

    @Override
    public int getOptionalLength() {
        return JxBaseUtils.OPTIONAL_LENGTH;
    }

    @Override
    public XBaseMemoFileType memoFileType() {
        return this.type.memoFileType();
    }
}
