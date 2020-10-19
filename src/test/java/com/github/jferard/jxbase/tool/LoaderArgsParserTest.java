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

package com.github.jferard.jxbase.tool;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class LoaderArgsParserTest {
    @Test
    public void test() {
        final LoaderArgs loaderArgs = new LoaderArgsParser()
                .parse(new String[]{"-c", "java.lang.Boolean", "-d", "-s", "100",
                        "data1/tir_im.dbf",
                        "jdbc:sqlite::memory:"});
        Assert.assertEquals(new File("data1/tir_im.dbf"), loaderArgs.source);
        Assert.assertEquals("jdbc:sqlite::memory:", loaderArgs.url);
        Assert.assertEquals(true, loaderArgs.dropTable);
        Assert.assertEquals(100, loaderArgs.chunkSize);
        Assert.assertEquals("java.lang.Boolean", loaderArgs.classForName);
    }

}