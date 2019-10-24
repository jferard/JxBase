/*
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

import java.util.Arrays;
import java.util.Date;

public class DbfMetadataTest {
    @Test
    public void testToString() {
        final DbfMetadata metadata = new DbfMetadata();
        metadata.setUpdateDate(new Date(0));
        Assert.assertEquals("DbfMetadata [\n" + "  type=null, \n" + "  updateDate=1970-01-01, \n" +
                "  recordsQty=0, \n" + "  fullHeaderLength=0, \n" + "  oneRecordLength=0, \n" +
                "  uncompletedTxFlag=0, \n" + "  ecnryptionFlag=0, \n" + "  fields=null\n" + "]", metadata.toString());
    }

    @Test
    public void testToStringWithFields() {
        DbfField f = DbfField.fromStringRepresentation("x,C,1,0");

        final DbfMetadata metadata = new DbfMetadata();
        metadata.setUpdateDate(new Date(0));
        metadata.setFields(Arrays.asList(f));
        Assert.assertEquals("DbfMetadata [\n" + "  type=null, \n" + "  updateDate=1970-01-01, \n" +
                "  recordsQty=0, \n" + "  fullHeaderLength=0, \n" + "  oneRecordLength=0, \n" +
                "  uncompletedTxFlag=0, \n" + "  ecnryptionFlag=0, \n" + "  fields=x,C,1,0\n" + "]", metadata.toString());
    }
}