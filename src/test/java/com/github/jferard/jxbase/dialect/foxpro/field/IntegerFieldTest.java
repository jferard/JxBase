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

package com.github.jferard.jxbase.dialect.foxpro.field;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class IntegerFieldTest {
    private IntegerField f;
    private IntegerAccess access;

    @Before
    public void setUp() throws Exception {
        this.access = new FoxProIntegerAccess();
        this.f = new IntegerField("i");
    }

    @Test
    public void getName() {
        Assert.assertEquals("i", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(4, this.f.getValueByteLength(this.access));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = {1, 2, 3, 4};
        Assert.assertEquals(Long.valueOf(67305985), this.f.getValue(this.access, bytes, 0, 4));
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("i,I,4,0", this.f.toStringRepresentation(this.access));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("IntegerField[name=i]", this.f.toString());
    }
}