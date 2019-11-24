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

import com.github.jferard.jxbase.dialect.db3.field.DB3DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.DateAccess;
import com.github.jferard.jxbase.dialect.db3.field.DateField;
import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.TimeZone;

public class DateFieldTest {
    private DateField f;
    private DateAccess access;

    @Before
    public void setUp() throws Exception {
        this.access = new DB3DateAccess(new RawRecordReader(JxBaseUtils.ASCII_CHARSET),
                new RawRecordWriter(JxBaseUtils.ASCII_CHARSET), TimeZone.getTimeZone("UTC"));
        this.f = new DateField("date");
    }

    @Test
    public void getName() {
        Assert.assertEquals("date", this.f.getName());
    }

    @Test
    public void getByteLength() {
        Assert.assertEquals(8, this.f.getValueByteLength(this.access));
    }

    @Test
    public void getValue() throws IOException {
        final byte[] bytes = "19700101".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Date date = new Date(0);
        Assert.assertEquals(date, this.f.getValue(this.access, bytes, 0, 8));
    }

    @Test
    public void writeValue() throws IOException {
        final Date value = new Date(0);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.f.writeValue(this.access, out, value);
        Assert.assertEquals("19700101",
                out.toString(String.valueOf(JxBaseUtils.ASCII_CHARSET)));
    }

    @Test
    public void toStringRepresentation() {
        Assert.assertEquals("date,D,8,0", this.f.toStringRepresentation(this.access));
    }

    @Test
    public void testToString() {
        Assert.assertEquals("DateField[name=date]", this.f.toString());
    }
}