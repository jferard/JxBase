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

package com.github.jferard.jxbase.dialect.db3.reader;

import com.github.jferard.jxbase.memo.MemoFileHeader;
import com.github.jferard.jxbase.memo.RawMemoReader;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DB3MemoReaderTest {
    private DB3MemoReader reader;
    private MemoFileHeader header;
    private RawMemoReader rawMemoReader;

    @Before
    public void setUp() {
        this.header = Mockito.mock(MemoFileHeader.class);
        this.rawMemoReader = Mockito.mock(RawMemoReader.class);
        this.reader = new DB3MemoReader(this.header, this.rawMemoReader);
    }

    @Test
    public void testHeader() {
        Assert.assertEquals(this.header, this.reader.getHeader());
    }

    @Test
    public void testRead() {
        Mockito.when(this.rawMemoReader.read(8, 0, 512))
                .thenReturn(new byte[]{1, 2, 3, JxBaseUtils.RECORDS_TERMINATOR});

        Assert.assertEquals(3, this.reader.read(8).getLength());
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, this.reader.read(8).getBytes());
    }
}