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

import com.github.jferard.jxbase.dialect.db3memo.DB3MemoDialect;
import com.github.jferard.jxbase.core.GenericFieldDescriptorArray;
import com.github.jferard.jxbase.XBaseFileTypeEnum;
import com.github.jferard.jxbase.field.CharacterField;
import com.github.jferard.jxbase.field.XBaseField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

public class GenericFieldDescriptorArrayWriterTest {
    private XBaseFieldDescriptorArrayWriter gfdaw;
    private ByteArrayOutputStream out;

    @Before
    public void setUp() {
        this.out = new ByteArrayOutputStream();
        this.gfdaw =
                new GenericFieldDescriptorArrayWriter(new DB3MemoDialect(XBaseFileTypeEnum.dBASEIV1),
                        this.out);
    }

    @Test
    public void write() throws IOException {
        this.gfdaw.write(new GenericFieldDescriptorArray(
                Collections.<XBaseField>singleton(new CharacterField("c", 25)), 10, 12));
        Assert.assertArrayEquals(
                new byte[]{99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 67, 0, 0, 0, 0, 25, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 13}, this.out.toByteArray());
    }

    /*
    @Test
    public void writeCharacterField() throws IOException {
        this.gfdaw.writeCharacterField("char", 10, 5);
        Assert.assertArrayEquals("char\0\0\0\0\0\0\0C\5\0\0\0\12\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
                .getBytes(JxBaseUtils.ASCII_CHARSET), this.out.toByteArray());
    }

    @Test
    public void writeDateField() throws IOException {
        this.gfdaw.writeDateField("date", 5);
        Assert.assertArrayEquals("date\0\0\0\0\0\0\0D\5\0\0\0\10\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
                .getBytes(JxBaseUtils.ASCII_CHARSET), this.out.toByteArray());
    }

    @Test
    public void writeDatetimeField() {
    }

    @Test
    public void writeIntegerField() throws IOException {
        this.gfdaw.writeIntegerField("int", 5);
        Assert.assertArrayEquals("int\0\0\0\0\0\0\0\0I\5\0\0\0\4\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
                .getBytes(JxBaseUtils.ASCII_CHARSET), this.out.toByteArray());
    }

    @Test
    public void writeLogicalField() throws IOException {
        this.gfdaw.writeLogicalField("bool", 5);
        Assert.assertArrayEquals("bool\0\0\0\0\0\0\0L\5\0\0\0\1\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
                .getBytes(JxBaseUtils.ASCII_CHARSET), this.out.toByteArray());
    }

    @Test
    public void writeMemoField() throws IOException {
        this.gfdaw.writeMemoField("memo", 5);
        Assert.assertArrayEquals("memo\0\0\0\0\0\0\0M\5\0\0\0\12\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
                .getBytes(JxBaseUtils.ASCII_CHARSET), this.out.toByteArray());
    }

    @Test
    public void writeNumericField() throws IOException {
        this.gfdaw.writeNumericField("numeric", 10, 4, 5);
        Assert.assertArrayEquals("numeric\0\0\0\0N\5\0\0\0\12\4\0\0\0\0\0\0\0\0\0\0\0\0\0\0"
                .getBytes(JxBaseUtils.ASCII_CHARSET), this.out.toByteArray());
    }
    */
}