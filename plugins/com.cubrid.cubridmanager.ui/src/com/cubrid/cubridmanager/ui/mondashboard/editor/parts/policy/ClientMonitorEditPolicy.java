/*
 * Copyright (C) 2009 Search Solution Corporation. All rights reserved by Search Solution. 
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
package com.cubrid.cubridmanager.ui.mondashboard.editor.parts.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.cubrid.cubridmanager.ui.mondashboard.editor.command.DeleteClientMonitorCommand;
import com.cubrid.cubridmanager.ui.mondashboard.editor.model.ClientNode;
import com.cubrid.cubridmanager.ui.mondashboard.editor.model.Dashboard;

/**
 * Edit policy used by client of broker edit part.
 * 
 * @author cyl
 * @version 1.0 - 2010-8-19 created by cyl
 */
public class ClientMonitorEditPolicy extends
		ComponentEditPolicy {

	/**
	 * Create a command of delete figure
	 * 
	 * @param deleteRequest to delete figure
	 * @return command of delete figure
	 */
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteClientMonitorCommand deleteCommand = new DeleteClientMonitorCommand();
		deleteCommand.setNodeToDelete((ClientNode) getHost().getModel());
		deleteCommand.setDashboard((Dashboard) getHost().getParent().getModel());
		return deleteCommand;
	}
}
