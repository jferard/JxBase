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

package com.github.jferard.jxbase.dialect.foxpro;

import java.util.Calendar;
import java.util.Date;

public class FoxProUtils {
    public static Date createHeaderUpdateDate(final byte yearByte, final byte monthByte,
                                              final byte dayByte) {
        final int year = yearByte + 2000;
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthByte - 1, dayByte);
        return calendar.getTime();
    }
}
