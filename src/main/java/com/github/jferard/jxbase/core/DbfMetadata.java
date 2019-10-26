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

package com.github.jferard.jxbase.core;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class DbfMetadata {
	private DbfFileTypeEnum type;
	private Date updateDate;
	private int recordsQty;
	private int fullHeaderLength;
	private int oneRecordLength;
	private byte uncompletedTxFlag;
	private byte encryptionFlag;
	private HashMap<String, OffsetDbfField> offsetFieldByName;
	private List<DbfField> fields;

	public DbfFileTypeEnum getFileType() {
		return type;
	}
	public void setType(DbfFileTypeEnum type) throws IOException {
		if (type == null)
			throw new IOException("The file is corrupted or is not a dbf file");
		this.type = type;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public int getRecordsQty() {
		return recordsQty;
	}
	public void setRecordsQty(int recordsQty) {
		this.recordsQty = recordsQty;
	}
	public int getFullHeaderLength() {
		return fullHeaderLength;
	}
	public void setFullHeaderLength(int fullHeaderLength) {
		this.fullHeaderLength = fullHeaderLength;
	}
	public int getOneRecordLength() {
		return oneRecordLength;
	}
	public void setOneRecordLength(int oneRecordLength) {
		this.oneRecordLength = oneRecordLength;
	}
	public byte getUncompletedTxFlag() {
		return uncompletedTxFlag;
	}
	public void setUncompletedTxFlag(byte uncompletedTxFlag) {
		this.uncompletedTxFlag = uncompletedTxFlag;
	}
	public byte getEncryptionFlag() {
		return encryptionFlag;
	}
	public void setEncryptionFlag(byte encryptionFlag) {
		this.encryptionFlag = encryptionFlag;
	}
	public OffsetDbfField getOffsetField(String name) {
		return offsetFieldByName.get(name);
	}
	public Collection<DbfField> getFields() {
		return fields;
	}
	public void setFields(List<DbfField> fields) {
		processFields(fields);
	}
	
	private void processFields(List<DbfField> fields) {
		offsetFieldByName = new LinkedHashMap<String, OffsetDbfField>(fields.size()*2);
		int offset = 1;
		for (DbfField f : fields) {
			f.setOffset(offset);
			offsetFieldByName.put(f.getName(), new OffsetDbfField(f, offset));
			offset += f.getLength();
		}
		this.fields = new ArrayList<DbfField>(fields);
	}
	
	public String getFieldsStringRepresentation() {
		if (offsetFieldByName == null) {
			return null;
		}
		int i = offsetFieldByName.size();
		// i*64 - just to allocate enough space
		StringBuilder sb = new StringBuilder(i*64);		
		for (OffsetDbfField of : offsetFieldByName.values()) {
			sb.append(of.toString());
			i--;
			if (i>0) {
				sb.append("|");			
			}			
		}
		return sb.toString();
	}

    private String formatUpdateDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(updateDate);
    }
	@Override
	public String toString() {
		return "DbfMetadata [\n  type=" + type + ", \n  updateDate="
				+ formatUpdateDate() + ", \n  recordsQty=" + recordsQty
				+ ", \n  fullHeaderLength=" + fullHeaderLength
				+ ", \n  oneRecordLength=" + oneRecordLength
				+ ", \n  uncompletedTxFlag=" + uncompletedTxFlag
				+ ", \n  encryptionFlag=" + encryptionFlag + ", \n  fields="
				//+ fields + "\n]";
				+ getFieldsStringRepresentation() + "\n]";
	}
}
