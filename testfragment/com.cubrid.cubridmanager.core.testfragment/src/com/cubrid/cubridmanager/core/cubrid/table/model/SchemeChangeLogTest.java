/*
 * Copyright (C) 2008 Search Solution Corporation. All rights reserved by Search Solution. 
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met: 
 *
 * - Redistributions of source code must retain the above copyright notice, 
 *   this list of conditions and the following disclaimer. 
 *
 * - Redistributions in binary form must reproduce the above copyright notice, 
 *   this list of conditions and the following disclaimer in the documentation 
 *   and/or other materials provided with the distribution. 
 *
 * - Neither the name of the <ORGANIZATION> nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software without 
 *   specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
 * OF SUCH DAMAGE. 
 *
 */
package com.cubrid.cubridmanager.core.cubrid.table.model;

import junit.framework.TestCase;

import com.cubrid.cubridmanager.core.cubrid.table.model.SchemaChangeLog.SchemeInnerType;

/**
 * test SchemeChangeLog model
 * 
 * @author wuyingshi
 * @version 1.0 - 2010-1-5 created by wuyingshi
 */
public class SchemeChangeLogTest extends
		TestCase {
	String oldValue = "oldValue";
	String newValue = "newValue";
	SchemeInnerType type = SchemeInnerType.TYPE_ATTRIBUTE;

	/**
	 * Test SchemeChangeLog
	 */
	public void testSchemeChangeLog() {
		SchemaChangeLog schemeChangeLog = new SchemaChangeLog(oldValue,
				newValue, type);

		SchemeInnerType type = SchemeInnerType.TYPE_SCHEMA;
		assertEquals(type.getText(), "schema");
		type = SchemeInnerType.TYPE_ATTRIBUTE;
		assertEquals(type.getText(), "attribute");
		type = SchemeInnerType.TYPE_CLASSATTRIBUTE;
		assertEquals(type.getText(), "classattribute");
		type = SchemeInnerType.TYPE_FK;
		assertEquals(type.getText(), "fk");
		type = SchemeInnerType.TYPE_INDEX;
		assertEquals(type.getText(), "index");
		type = SchemeInnerType.TYPE_TABLE_NAME;
		assertEquals(type.getText(), "tablename");
		type = SchemeInnerType.TYPE_OWNER;
		assertEquals(type.getText(), "owner");
		type = SchemeInnerType.TYPE_SUPER_TABLE;
		assertEquals(type.getText(), "supertablename");
		type = SchemeInnerType.TYPE_PARTITION;
		assertEquals(type.getText(), "partition");

		schemeChangeLog.setOldValue(oldValue);
		schemeChangeLog.setNewValue(newValue);
		schemeChangeLog.setType(type);
		assertEquals(schemeChangeLog.getOldValue(), oldValue);
		assertEquals(schemeChangeLog.getNewValue(), newValue);
		assertEquals(schemeChangeLog.getType(), type);
		SchemeInnerType.eval("test");
	}

}
