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

/**
 * Contains the field classes. Field classes are dialect independent.
 * <p>
 * Note: a field has three length: the repLength (third element of: name,type,repLength,
 * numberOfDecimalPlaces),
 * the byteLength of the value (usually the same as the previous), and the dataSize.
 */
package com.github.jferard.jxbase.field;
