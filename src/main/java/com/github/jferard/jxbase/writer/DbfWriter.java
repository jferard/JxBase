/*
 * JxBase - Copyright (c) 2019 Julien Férard
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

package com.github.jferard.jxbase.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.github.jferard.jxbase.core.DbfMetadata;
import com.github.jferard.jxbase.core.OffsetDbfField;
import com.github.jferard.jxbase.util.BitUtils;
import com.github.jferard.jxbase.util.DbfMetadataUtils;
import com.github.jferard.jxbase.util.JdbfUtils;

public class DbfWriter {
	private OutputStream out;
	private DbfMetadata metadata;
	private Charset stringCharset = Charset.defaultCharset();
	private byte[] recordBuffer;

	public DbfWriter(DbfMetadata metadata,OutputStream out) throws IOException {
		this.out = out;
		this.metadata = metadata;
		recordBuffer = new byte[metadata.getOneRecordLength()];
		writeHeaderAndFields();
	}

	private void writeHeaderAndFields() throws IOException {
		writeHeader();
		writeFields();
	}

	private void writeHeader() throws IOException {
		byte[] bytes = DbfMetadataUtils.toByteArrayHeader(metadata);
		// assert(bytes.length == 16);
		out.write(bytes);
		BitUtils.memset(bytes, 0);
		out.write(bytes);
	}

	private void writeFields() throws IOException {
		byte[] bytes = new byte[JdbfUtils.FIELD_RECORD_LENGTH];
		for (OffsetDbfField of : metadata.getOffsetFields()) {
			DbfMetadataUtils.writeDbfField(of, bytes);
			out.write(bytes);
		}
		out.write(JdbfUtils.HEADER_TERMINATOR);
	}

	public void write(Map<String, Object> map) throws IOException {
		BitUtils.memset(recordBuffer, JdbfUtils.EMPTY);
		for (OffsetDbfField of : metadata.getOffsetFields()) {
			Object o = map.get(of.getName());
			writeIntoRecordBuffer(of, o);
		}
		out.write(recordBuffer);
	}

	private void writeIntoRecordBuffer(OffsetDbfField of, Object o) {
		if (o == null) {
			return;
		}
		// TODO: for all methods add length checkings
		// TODO: for all methods add type checkings
		switch (of.getType()) {
		case Character:
			writeString(of, (String)o);
			break;
		case Date:
			writeDate(of, (Date)o);
			break;
		case Logical:
			writeBoolean(of, (Boolean)o);
			break;
		case Numeric:
			writeBigDecimal(of, (BigDecimal)o);
			break;
		case Float:
			if (o instanceof Double)
				writeDouble(of, (Double)o);
			else
				writeFloat(of, (Float)o);
			break;

		// FOXPRO
		case DateTime:
			writeDateTime(of, (Date)o);
			break;
		case Double: // Behaves like dBASE 7 double but uses different column type identifier
			writeDouble7(of, (Double)o);
			break;
		/*case Integer: // exactly like dBASE 7
			writeInteger(f, (Integer)o);
			break;*/

		// dBASE 7
		case Timestamp:
			writeTimestamp(of, (Date)o);
			break;
		case Double7:
			writeDouble7(of, (Double)o);
			break;
		case Integer:
			writeInteger(of, (Integer)o);
			break;

		default:
			throw new UnsupportedOperationException("Unknown or unsupported field type " + of.getType().name() + " for " + of.getName());
		}
	}

	private void writeBigDecimal(OffsetDbfField of, BigDecimal value) {
		if (value != null) {
			String s = value.toPlainString();
			byte[] bytes = s.getBytes();
			if (bytes.length > of.getLength()) {
				byte[] newBytes = new byte[of.getLength()];
				System.arraycopy(bytes, 0, newBytes, 0, of.getLength());
				bytes = newBytes;
			}
			System.arraycopy(bytes, 0, recordBuffer, of.getOffset(), bytes.length);
		} else {
			blankify(of);
		}
	}

	private void writeBoolean(OffsetDbfField f, Boolean value) {
		if (value != null) {
			String s = value.booleanValue() ? "T" : "F";
			byte[] bytes = s.getBytes();
			System.arraycopy(bytes, 0, recordBuffer, f.getOffset(), bytes.length);
		} else {
			// dBASE 7 explicitly requires ? for uninitialized, some systems may use ' ' as well
			byte[] bytes = "?".getBytes();
			System.arraycopy(bytes, 0, recordBuffer, f.getOffset(), bytes.length);
		}
	}

	private void writeDate(OffsetDbfField f, Date value) {
		if (value != null) {
			byte[] bytes = JdbfUtils.writeDate(value);
			// TODO: check that bytes.length = f.getLength();
			System.arraycopy(bytes, 0, recordBuffer, f.getOffset(), bytes.length);
		} else {
			blankify(f);
		}
	}

	private void writeString(OffsetDbfField of, String value) {
		if (value != null) {
			byte[] bytes = value.getBytes(stringCharset);
			if (bytes.length > of.getLength()) {
				byte[] newBytes = new byte[of.getLength()];
				System.arraycopy(bytes, 0, newBytes, 0, of.getLength());
				bytes = newBytes;
			}
			System.arraycopy(bytes, 0, recordBuffer, of.getOffset(), bytes.length);
		} else {
			blankify(of);
		}
	}

	private void writeFloat(OffsetDbfField f, Float value) {
		writeDouble(f, value.doubleValue());
	}

	private void writeDouble(OffsetDbfField of, Double value) {
		if (value != null) {
			String str = String.format("% 20.18f", value); // Whitespace pad; 20 min length; 18 max precision
			if (str.length() > 20) { // Trim to 20 places, if longer
				str = str.substring(0, 20);
			}
			writeString(of, str);
		} else {
			blankify(of);
		}
	}

	private void writeTimestamp(OffsetDbfField f, Date d) {
		if (d != null) {
			byte[] bytes = JdbfUtils.writeJulianDate(d);
			System.arraycopy(bytes, 0, recordBuffer, f.getOffset(), bytes.length);
		} else {
			blankify(f);
		}
	}

	// TODO: Appears to be 64 bit epoch timestamp, but there was no reliable source for that
	private void writeDateTime(OffsetDbfField f, Date d) {
		if (d != null) {
			ByteBuffer bb = ByteBuffer.allocate(8);
			bb.putLong(d.getTime());
			System.arraycopy(bb.array(), 0, recordBuffer, f.getOffset(), bb.capacity());
		} else {
			blankify(f);
		}
	}

	private void writeDouble7(OffsetDbfField of, Double d) {
		if (d != null) {
			ByteBuffer bb = ByteBuffer.allocate(8);
			bb.putDouble(d);
			System.arraycopy(bb.array(), 0, recordBuffer, of.getOffset(), bb.capacity());
		} else {
			blankify(of);
		}
	}

	private void writeInteger(OffsetDbfField f, Integer i) {
		if (i != null) {
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.putInt(i);
			System.arraycopy(bb.array(), 0, recordBuffer, f.getOffset(), bb.capacity());
		} else {
			blankify(f);
		}
	}

	private void blankify(OffsetDbfField of) {
		byte[] bytes = new byte[of.getLength()];
		Arrays.fill(bytes, (byte)' ');
		System.arraycopy(bytes, 0, recordBuffer, of.getOffset(), bytes.length);
	}

	public void close() throws IOException {
		this.out.flush();
		this.out.close();
	}

	public void setStringCharset(String charsetName) {
		setStringCharset(Charset.forName(charsetName));
	}

	public void setStringCharset(Charset stringCharset) {
		this.stringCharset = stringCharset;		
	}
}
