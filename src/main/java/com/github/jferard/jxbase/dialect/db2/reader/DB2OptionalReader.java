/*
* JxBase - Copyright (c) 2019-2021 Julien Férard
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

package com.github.jferard.jxbase.dialect.db2.reader;

import com.github.jferard.jxbase.core.GenericOptional;
import com.github.jferard.jxbase.core.XBaseAccess;
import com.github.jferard.jxbase.core.XBaseDialect;
import com.github.jferard.jxbase.core.XBaseFieldDescriptorArray;
import com.github.jferard.jxbase.core.XBaseMetadata;
import com.github.jferard.jxbase.core.XBaseOptional;
import com.github.jferard.jxbase.reader.XBaseOptionalReader;

import java.io.InputStream;

/**
 * There is no optional chunk in DB2.
 * @param <D> the dialect
 * @param <A> the access
 */
public class DB2OptionalReader<A extends XBaseAccess, D extends XBaseDialect<A, D>> implements XBaseOptionalReader {
    public DB2OptionalReader(final D dialect, final InputStream inputStream,
                             final XBaseMetadata metadata,
                             final XBaseFieldDescriptorArray<A> array) {
    }

    @Override
    public XBaseOptional read() {
        return GenericOptional.EMPTY;
    }
}
