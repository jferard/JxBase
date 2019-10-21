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

public class BitUtils {
	public static int makeInt(byte b1, byte b2) {
		return ((b1 <<  0) & 0x000000FF) +   
			   ((b2 <<  8) & 0x0000FF00);
	}
	
	public static int makeInt(byte b1, byte b2, byte b3, byte b4) {
		return ((b1 <<  0) & 0x000000FF) +   
		       ((b2 <<  8) & 0x0000FF00) + 
		       ((b3 << 16) & 0x00FF0000) + 
		       ((b4 << 24) & 0xFF000000);
	}
	
	public static byte[] makeByte4(int i) {
		byte[] b = {
			(byte)(i & 0x000000FF),
			(byte)((i >> 8)& 0x000000FF),
			(byte)((i >> 16)& 0x000000FF),
			(byte)((i >> 24)& 0x000000FF)
		};
		return b;
	}
	
	public static byte[] makeByte2(int i) {
		byte[] b = {
			(byte)(i & 0x000000FF),
			(byte)((i >> 8)& 0x000000FF)
		};
		return b;
	}
	
	public static void memset(byte[] bytes, int value) {
		// this approach is a bit faster than Arrays.fill,
		// because there is no rangeCheck
		byte b = (byte)value;
		for (int i=0; i<bytes.length; i++) {
			bytes[i] = b;
		}
//		Arrays.fill(bytes, (byte)value);
	}
}
