/*
 * Copyright (C) 2008 Search Solution Corporation. All rights reserved by Search
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
package com.cubrid.common.ui.cubrid.serial;

import org.eclipse.osgi.util.NLS;

import com.cubrid.common.ui.CommonUIPlugin;

/**
 * Message bundle classes. Provides convenience methods for manipulating
 * messages.
 * 
 * @author pangqiren
 * @version 1.0 - 2009-6-4 created by pangqiren
 */
public class Messages extends
		NLS {

	static {
		NLS.initializeMessages(CommonUIPlugin.PLUGIN_ID
				+ ".cubrid.serial.Messages", Messages.class);
	}
	public static String msgConfirmDelSerial;
	public static String titleCreateSerialDialog;
	public static String msgCreateSerialDialog;
	public static String lblSerialName;
	public static String lblSerialDescription;
	public static String lblStartValue;
	public static String lblCurrentValue;
	public static String lblIncrementValue;
	public static String lblMinValue;
	public static String lblMaxValue;
	public static String btnNoMinValue;
	public static String btnNoMaxValue;
	public static String lblCacheCount;
	public static String btnNoCache;
	public static String btnCycle;
	public static String titleEditSerialDialog;
	public static String msgEditSerialDialog;
	public static String errSerialName;
	public static String errSerialNameLength;
	public static String errStartValue;
	public static String errStartValueAfter1020;
	public static String msgStartValue;
	public static String msgCurrentValue;
	public static String errIncrementValue;
	public static String errIncrementValueAfter1020;
	public static String errMinValue;
	public static String errMinValueAfter1020;
	public static String errMaxValue;
	public static String errMaxValueAfter1020;
	public static String errSerialExist;
	public static String errValue;
	public static String errDiffValue;
	public static String errCacheCount;
	public static String grpGeneral;
	public static String grpSqlScript;

	public static String delSerialTaskName;
	public static String loadSerialTaskName;
	public static String createSerialTaskName;
	public static String editSerialTaskName;

	public static String errNameNotExist;
	
	//serial dashboard
	public static String serialsDetailInfoPartTitle;
	public static String serialsDetailInfoPartTableOwnerCol;
	public static String serialsDetailInfoPartTableNameCol;
	public static String serialsDetailInfoPartTableCurValCol;
	public static String serialsDetailInfoPartTableIncreValCol;
	public static String serialsDetailInfoPartTableMinValCol;
	public static String serialsDetailInfoPartTableMaxValCol;
	public static String serialsDetailInfoPartTableCacheNumCol;
	public static String serialsDetailInfoPartTableCycleCol;
	public static String serialsDetailInfoPartCreateSerialBtn;
	public static String serialsDetailInfoPartEditSerialBtn;
	public static String serialsDetailInfoPartDropSerialBtn;

	public static String errSerialNoSelection;
}