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

package com.github.jferard.jxbase.dialect.db2.field;

import com.github.jferard.jxbase.field.FieldRepresentation;
import com.github.jferard.jxbase.field.RawRecordWriteHelper;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

public class DB2NumericAccessTest {
    private NumericAccess access;

    @Before
    public void setUp() {
        this.access =
                new DB2NumericAccess(new RawRecordWriteHelper(JxBaseUtils.ASCII_CHARSET));
    }

    @Test
    public void getNumericValueLength() {
        Assert.assertEquals(10, this.access.getNumericValueLength(10));
    }

    @Test
    public void extractNumericValue() {
        Assert.assertEquals(BigDecimal.valueOf(10.24),
                this.access.extractNumericValue("     10.24".getBytes(JxBaseUtils.ASCII_CHARSET), 0,
                        10, 2));
        Assert.assertNull(this.access
                .extractNumericValue("          ".getBytes(JxBaseUtils.ASCII_CHARSET), 0, 10, 2));
    }

    @Test
    public void writeNumericValue() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.access.writeNumericValue(bos, BigDecimal.valueOf(10.236), 10, 2);
        Assert.assertArrayEquals("     10.24".getBytes(JxBaseUtils.ASCII_CHARSET),
                bos.toByteArray());
    }

    @Test
    public void writeNumericValueNull() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        this.access.writeNumericValue(bos, null, 10, 2);
        Assert.assertArrayEquals("          ".getBytes(JxBaseUtils.ASCII_CHARSET),
                bos.toByteArray());
    }

    @Test
    public void getNumericFieldRepresentation() {
        Assert.assertEquals(new FieldRepresentation("foo", 'N', 10, 2),
                this.access.getNumericFieldRepresentation("foo", 10, 2));
    }
}