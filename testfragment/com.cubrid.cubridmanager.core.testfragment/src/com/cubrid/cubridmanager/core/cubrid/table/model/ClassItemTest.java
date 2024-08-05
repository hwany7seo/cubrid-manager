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

import java.util.ArrayList;
import java.util.List;

import com.cubrid.cubridmanager.core.utils.ModelUtil.ClassType;

import junit.framework.TestCase;

/**
 * test TClassInfo model
 * 
 * @author wuyingshi
 * @version 1.0 - 2010-1-4 created by wuyingshi
 */
public class ClassItemTest extends
		TestCase {

	/**
	 * Test ClassItem
	 */
	public final void testClassItem() {
		String classname = "classname";
		String owner = "owner";
		String virtual = "virtual";
		List<String> superclassList = null;
		//test 	getters and setters		
		ClassItem classItem = new ClassItem();
		classItem.getSuperclassList();
		ClassItem classItem2 = new ClassItem();
		classItem2.addSuperclass("superClass");
		classItem.isVirtual();
		classItem.setClassname(classname);
		classItem.setOwner(owner);
		classItem.setVirtual(virtual);

		assertEquals(classItem.getClassname(), classname);
		assertEquals(classItem.getOwner(), owner);
		assertEquals(classItem.getVirtual(), virtual);
		
		classItem.addSuperclass(null);
		superclassList = new ArrayList<String>();
		classItem.addSuperclass("superClass");
		classItem.getSuperclassList();
		superclassList.add("superClass");
		classItem.setVirtual(ClassType.VIEW.getText());
		classItem.isVirtual();
		
	}
}
