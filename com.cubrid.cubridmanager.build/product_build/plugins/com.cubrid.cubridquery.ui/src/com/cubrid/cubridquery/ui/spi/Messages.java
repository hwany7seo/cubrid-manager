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
package com.cubrid.cubridquery.ui.spi;

import org.eclipse.osgi.util.NLS;

import com.cubrid.cubridquery.ui.CubridQueryUIPlugin;

/**
 * 
 * This is message bundle classes and provide convenience methods for
 * manipulating messages.
 * 
 * @author pangqiren
 * @version 1.0 - 2009-6-4 created by pangqiren
 */
public class Messages extends
		NLS {

	static {
		NLS.initializeMessages(CubridQueryUIPlugin.PLUGIN_ID + ".spi.Messages",
				Messages.class);
	}
	//loader
	public static String msgTablesFolderName;
	public static String msgViewsFolderName;
	public static String msgSpFolderName;
	public static String msgTriggerFolderName;
	public static String msgSerialFolderName;

	// property page related
	public static String titlePropertiesDialog;

	// action related message
	// common action
	public static String refreshActionName;
	public static String propertyActionName;
	//connection 
	public static String createConnActionName;
	public static String createConnByURLActionName;
	public static String dropConnActionName;
	public static String openConnActionName;
	public static String editConnActionName;
	public static String closeConnActionName;
	public static String copyConnActionName;
	public static String pasteConnActionName;
	public static String renameConnActionName;
	public static String viewDatabaseVersionActionName;

	public static String importConnsAction;
	public static String importServerAction;
	public static String exportServerAction;

	public static String queryNewActionNameBig;
	public static String createConnActionNameBig;
	
	//brokerLogTopMerge utility
	public static String brokerLogTopMergeAction;
	public static String brokerLogParseAction;
	
	// db user action
	public static String deleteUserActionName;
	public static String editUserActionName;
	public static String addUserActionName;
	
	//cubrid broker conf edit utility
	public static String cubridBrokerConfOpenFileActionName;
}