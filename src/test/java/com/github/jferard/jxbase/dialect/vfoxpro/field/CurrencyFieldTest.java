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

package com.github.jferard.jxbase.dialect.vfoxpro.field;

import com.github.jferard.jxbase.dialect.db2.field.CharacterField;
import com.github.jferard.jxbase.field.FieldRepresentation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CurrencyFieldTest {
    private CurrencyField field;
    private FoxProCurrencyAccess access;

    @Before
    public void setUp() {
        this.field = new CurrencyField("currency");
        this.access = new FoxProCurrencyAccess();
    }

    @Test
    public void getName() {
        Assert.assertEquals("currency", this.field.getName());
    }

    @Test
    public void getValueLength() {
        Assert.assertEquals(8, this.field.getValueLength(this.access));
    }

    @Test
    public void extractValue() {
        Assert.assertEquals(10L,
                (long) this.field
                        .extractValue(this.access, new byte[]{0, 0, 0, 0, 10, 0, 0, 0}, 0, 8));
    }

    @Test
    public void writeValue() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.field.writeValue(this.access, out, 1000L);
        Assert.assertArrayEquals(
                new byte[]{0, 0, 0, 0, -24, 3, 0, 0}, out.toByteArray());
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("currency,Y,8,0",
                this.field.toStringRepresentation(this.access));
    }

    @Test
    public void toRepresentation() {
        Assert.assertEquals(new FieldRepresentation("currency", 'Y', 8, 0),
                this.field.toRepresentation(this.access));
    }

    @Test
    public void testToStringRepresentation() {
        Assert.assertEquals("currency,Y,8,0",
                this.field.toStringRepresentation(this.access));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("CurrencyField[name=currency]",
                this.field.toString());
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(this.field, this.field);
        Assert.assertEquals(new CurrencyField("currency"), this.field);
        Assert.assertNotEquals(new CurrencyField("currency2"), this.field);
        Assert.assertNotEquals(new CharacterField("currency", 10),
                this.field);
        Assert.assertNotEquals(this.field,
                new CharacterField("currency", 10));
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(575402001,
                this.field.hashCode());
    }
}