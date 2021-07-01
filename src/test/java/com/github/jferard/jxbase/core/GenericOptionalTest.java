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

package com.github.jferard.jxbase.core;

import org.junit.Assert;
import org.junit.Test;

public class GenericOptionalTest {
    @Test
    public void test() {
        final byte[] bytes = {0, 1, 2};
        final GenericOptional optional = new GenericOptional(bytes);
        Assert.assertArrayEquals(bytes, optional.getBytes());
        Assert.assertEquals(3, optional.getLength());
    }

    @Test
    public void testEmpty() {
        final XBaseOptional optional = GenericOptional.DB234_EMPTY;
        Assert.assertArrayEquals(new byte[] {}, optional.getBytes());
        Assert.assertEquals(0, optional.getLength());
    }
}