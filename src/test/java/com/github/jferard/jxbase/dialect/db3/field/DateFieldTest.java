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

package com.github.jferard.jxbase.dialect.db3.field;

import com.github.jferard.jxbase.util.JxBaseUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

public class DateFieldTest {
    private DateAccess access;
    private DateField df;

    @Before
    public void setUp() {
        this.access = DB3DateAccess.create(JxBaseUtils.ASCII_CHARSET, JxBaseUtils.UTC_TIME_ZONE);
        this.df = new DateField("date");
    }

    @Test
    public void getDateName() {
        Assert.assertEquals("date", this.df.getName());
    }

    @Test
    public void getDateByteLength() {
        Assert.assertEquals(8, this.df.getValueLength(this.access));
    }

    @Test
    public void getDateValue() throws IOException {
        final byte[] bytes = "19700101".getBytes(JxBaseUtils.ASCII_CHARSET);
        final Date date = new Date(0);
        Assert.assertEquals(date, this.df.extractValue(this.access, bytes, 0, 8));
    }

    @Test
    public void getDateValueNull() throws IOException {
        final byte[] bytes = "        ".getBytes(JxBaseUtils.ASCII_CHARSET);
        Assert.assertNull(this.df.extractValue(this.access, bytes, 0, 8));
    }

    @Test
    public void getDateValueException() {
        final byte[] bytes = "abcdefgh".getBytes(JxBaseUtils.ASCII_CHARSET);
        final DateField thisDf = this.df;
        final DateAccess thisAccess = this.access;
        Assert.assertThrows(RuntimeException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                thisDf.extractValue(thisAccess, bytes, 0, 8);
            }
        });
    }

    @Test
    public void writeDateValue() throws IOException {
        final Date value = new Date(0);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.df.writeValue(this.access, out, value);
        Assert.assertEquals("19700101", out.toString(String.valueOf(JxBaseUtils.ASCII_CHARSET)));
    }

    @Test
    public void writeNullValue() throws IOException {
        final Date value = null;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        this.df.writeValue(this.access, out, value);
        Assert.assertEquals("        ", out.toString(String.valueOf(JxBaseUtils.ASCII_CHARSET)));
    }

    @Test
    public void toDateStringRepresentation() {
        Assert.assertEquals("date,D,8,0", this.df.toStringRepresentation(this.access));
    }

    @Test
    public void testDateToString() {
        Assert.assertEquals("DateField[name=date]", this.df.toString());
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(3076014, this.df.hashCode());
        Assert.assertEquals(this.df, this.df);
        Assert.assertNotEquals(this.df, new Object());
        final DateField f2 = new DateField("d");
        Assert.assertNotEquals(this.df, f2);
        final DateField f3 = new DateField("date");
        Assert.assertEquals(this.df, f3);
    }
}
