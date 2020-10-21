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

package com.github.jferard.jxbase.field;

import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class RawRecordReadHelperTest {
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void test() {
        final RawRecordReadHelper helper = new RawRecordReadHelper(JxBaseUtils.ASCII_CHARSET);
        final byte[] bytes = "*".getBytes(JxBaseUtils.ASCII_CHARSET);
        helper.extractTrimmedASCIIString(bytes, 0, 2);
    }

}