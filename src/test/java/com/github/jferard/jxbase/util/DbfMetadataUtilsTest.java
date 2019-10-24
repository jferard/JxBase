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

package com.github.jferard.jxbase.util;

import com.github.jferard.jxbase.core.DbfField;
import com.github.jferard.jxbase.core.DbfFileTypeEnum;
import com.github.jferard.jxbase.core.DbfMetadata;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

public class DbfMetadataUtilsTest {
    @Test
    public void testFromFields() throws IOException {
        DbfField f = DbfField.fromStringRepresentation("x,C,1,0");

        final DbfMetadata metadata = DbfMetadataUtils.fromFields(Arrays.asList(f), DbfFileTypeEnum.FoxBASEPlus1);
        Assert.assertEquals("DbfMetadata [\n" + "  type=FoxBASEPlus1, \n" +
                "  updateDate=2019-10-24, \n" + "  recordsQty=0, \n" + "  fullHeaderLength=65, \n" +
                "  oneRecordLength=2, \n" + "  uncompletedTxFlag=0, \n" + "  ecnryptionFlag=0, \n" +
                "  fields=x,C,1,0\n]", metadata.toString());
    }

    @Test
    public void testFromFieldsString() throws IOException {
        final DbfMetadata metadata = DbfMetadataUtils.fromFieldsString("x,C,1,0");
        Assert.assertEquals("DbfMetadata [\n" + "  type=FoxBASEPlus1, \n" +
                "  updateDate=2019-10-24, \n" + "  recordsQty=0, \n" + "  fullHeaderLength=65, \n" +
                "  oneRecordLength=2, \n" + "  uncompletedTxFlag=0, \n" + "  ecnryptionFlag=0, \n" +
                "  fields=x,C,1,0\n]", metadata.toString());
    }
}