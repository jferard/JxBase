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

package com.github.jferard.jxbase.writer.internal;

import com.github.jferard.jxbase.core.FoxProDialect;
import com.github.jferard.jxbase.core.GenericDialect;
import com.github.jferard.jxbase.core.XBaseFileTypeEnum;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FoxProFieldDescriptorArrayWriterTest {
    private GenericFieldDescriptorArrayWriter fpfdaw;
    private ByteArrayOutputStream out;

    @Before
    public void setUp() {
        this.out = new ByteArrayOutputStream();
        this.fpfdaw =
                new GenericFieldDescriptorArrayWriter(new FoxProDialect(XBaseFileTypeEnum.dBASEIV1),
                        this.out);
    }

    /*
    @Test
    public void writeDatetimeField() {
    }

    @Test
    public void writeSmallMemoField() throws IOException {
        this.fpfdaw.writeSmallMemoField("memo", 5);
        Assert.assertArrayEquals("memo\0\0\0\0\0\0\0M\5\0\0\0\4\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
                .getBytes(JxBaseUtils.ASCII_CHARSET), this.out.toByteArray());
    }

    @Test
    public void writeNullFlagsField() throws IOException {
        this.fpfdaw.writeNullFlagsField("nf", 14, 5);
        Assert.assertArrayEquals("nf\0\0\0\0\0\0\0\0\0\60\5\0\0\0\16\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
                .getBytes(JxBaseUtils.ASCII_CHARSET), this.out.toByteArray());
    }
    */
}