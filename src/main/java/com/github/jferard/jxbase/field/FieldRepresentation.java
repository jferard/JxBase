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

package com.github.jferard.jxbase.field;

import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.JxBaseUtils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A field representation is a dbf typical representation: name, type, length, number of decimals.
 * It can be written to a file, printed to screen, ...
 * It's dialect dependent
 */
public class FieldRepresentation {
    public String getName() {
        return this.name;
    }

    public byte getType() {
        return this.type;
    }

    public int getRepLength() {
        return this.repLength;
    }

    public int getNumberOfDecimalPlaces() {
        return this.numberOfDecimalPlaces;
    }

    private final String name;
    private final byte type;
    private final int repLength;
    private final int numberOfDecimalPlaces;

    public FieldRepresentation(final String name, final char type, final int repLength,
                               final int numberOfDecimalPlaces) {
        this.name = name;
        this.type = (byte) type;
        this.repLength = repLength;
        this.numberOfDecimalPlaces = numberOfDecimalPlaces;
    }

    /*
    public void write(final OutputStream out, final int offset) throws IOException {
        final byte[] nameBytes = this.name.getBytes(JxBaseUtils.ASCII_CHARSET);
        final int nameLength = nameBytes.length;
        if (nameLength > 11) {
            throw new IOException("Name too long");
        }
        out.write(nameBytes);
        BitUtils.writeZeroes(out, 11 - nameLength);
        out.write(this.type); // 11
        BitUtils.writeLEByte4(out, offset); // 12-15
        out.write(this.repLength & 0xFF); // 16
        out.write(this.numberOfDecimalPlaces); // 17
        BitUtils.writeZeroes(out, JxBaseUtils.FIELD_DESCRIPTOR_SIZE - 18);
    }*/

    @Override
    public String toString() {
        return this.name + "," + (char) this.type + "," + this.repLength + "," +
                this.numberOfDecimalPlaces;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldRepresentation)) {
            return false;
        }
        final FieldRepresentation other = (FieldRepresentation) o;
        return this.type == other.type && this.repLength == other.repLength &&
                this.numberOfDecimalPlaces == other.numberOfDecimalPlaces &&
                this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return ((31 * this.type + this.repLength) * 31 + this.numberOfDecimalPlaces) * 31 +
                this.name.hashCode();
    }
}
