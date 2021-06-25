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

package com.github.jferard.jxbase.field;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FieldRepresentationTest {
    private FieldRepresentation fr;

    @Before
    public void setUp() {
        this.fr = new FieldRepresentation("foo", 'Z', 9, 2);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals("foo", this.fr.getName());
        Assert.assertEquals('Z', this.fr.getType());
        Assert.assertEquals(9, this.fr.getFieldLength());
        Assert.assertEquals(2, this.fr.getNumberOfDecimalPlaces());
    }

    @Test
    public void testToString() {
        Assert.assertEquals("foo,Z,9,2", this.fr.toString());
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(this.fr, this.fr);
        Assert.assertEquals(2791475, this.fr.hashCode());
        Assert.assertNotEquals(this.fr, new Object());
        final FieldRepresentation f2 = new FieldRepresentation("char", 'Z', 9, 2);
        Assert.assertNotEquals(this.fr, f2);
        final FieldRepresentation f3 = new FieldRepresentation("foo", 'Z', 9, 2);
        Assert.assertEquals(this.fr, f3);
    }
}