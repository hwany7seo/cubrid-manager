/*
 * Copyright (C) 2009 Search Solution Corporation. All rights reserved by Search
 * Solution.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: -
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. - Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. - Neither the name of the <ORGANIZATION> nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package com.cubrid.cubridmanager.core.cubrid.table.model;

import java.util.Locale;

import com.cubrid.cubridmanager.core.utils.ModelUtil.ClassType;

/**
 * 
 * This class is responsible to cache class information
 * 
 * @author pangqiren
 * @version 1.0 - 2009-5-8 created by pangqiren
 */
public class ClassInfo {

	private String className;
	private String ownerName;
	private ClassType classType;
	private boolean isSystemClass;
	private boolean isPartitionedClass;
	private boolean isSupportUserSchema;
	private boolean debugInputUserSchema = false;

	public ClassInfo(String className) {
		this.className = className;
	}

	public ClassInfo(String className, String ownerName, ClassType classType,
			boolean isSystemClass, boolean isPartitionedClass, boolean isSupportUserSchema) {
		this.className = className;
		this.ownerName = ownerName;
		this.classType = classType;
		this.isSystemClass = isSystemClass;
		this.isPartitionedClass = isPartitionedClass;
		this.isSupportUserSchema = isSupportUserSchema;
		this.debugInputUserSchema = true;
	}

	public String getClassName() {
		if (!debugInputUserSchema) {
			try {
				throw new Exception(); 
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getUniqueName() {
		if (!debugInputUserSchema) {
			try {
				throw new Exception(); 
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		if (isSupportUserSchema) {
			if (isSystemClass) {
				return className;
			} else {
				return ownerName + "." + className;
			}
		} else {
			return className;
		}
	}

	public ClassType getClassType() {
		return classType;
	}

	public void setClassType(ClassType classType) {
		this.classType = classType;
	}

	public boolean isSystemClass() {
		return isSystemClass;
	}

	public void setSystemClass(boolean isSystemClass) {
		this.isSystemClass = isSystemClass;
	}

	public boolean isPartitionedClass() {
		return isPartitionedClass;
	}

	public void setPartitionedClass(boolean isPartitionedClass) {
		this.isPartitionedClass = isPartitionedClass;
	}

	public boolean isSupportUserSchema() {
		return isSupportUserSchema;
	}

	public void setSupportUserSchema(boolean isSupportUserSchema) {
		this.debugInputUserSchema = true;
		this.isSupportUserSchema = isSupportUserSchema;
	}

}
