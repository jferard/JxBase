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

package com.github.jferard.jxbase.it;

import com.github.jferard.jxbase.TestHelper;
import com.github.jferard.jxbase.tool.DatabaseLoader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.ParseException;

public class DatabaseLoaderIT {
    @Test
    public void testMainHelper()
            throws ClassNotFoundException, IOException, ParseException, SQLException {
        this.testHelpMessage(new String[]{});
        this.testHelpMessage(new String[]{"-h"});
        this.testHelpMessage(new String[]{"--help"});
    }

    private void testHelpMessage(final String[] args)
            throws ClassNotFoundException, IOException, ParseException, SQLException {
        final PrintStream outBkp = System.out;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
        DatabaseLoader.main(args);
        System.setOut(outBkp);

        Assert.assertTrue(bos.toString("UTF-8").startsWith("Usage: java -cp"));
    }

    @Test
    public void testMain() throws ClassNotFoundException, ParseException, SQLException,
            IOException {
        DatabaseLoader.main(new String[]{"-c", "java.lang.Boolean", "-d", "-s", "100", TestHelper.getResourcePath("data1/tir_im.dbf"),
                "jdbc:sqlite::memory:"});
    }
}
