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

package com.github.jferard.jxbase.core;

public interface XBaseRepresentations {
    String characterFieldToStringRepresentation(String name, int length);

    String dateFieldToStringRepresentation(String name);

    String datetimeFieldToStringRepresentation(String name);

    String integerFieldToStringRepresentation(String name, int length);

    String logicalFieldToStringRepresentation(String name);

    String memoFieldToStringRepresentation(String name);

    String numericFieldToStringRepresentation(String name, int length, int numberOfDecimalPlaces);
}