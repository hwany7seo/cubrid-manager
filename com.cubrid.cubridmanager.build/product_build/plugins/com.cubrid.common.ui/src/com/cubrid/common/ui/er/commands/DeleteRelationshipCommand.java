/*
 * Copyright (C) 2013 Search Solution Corporation. All rights reserved by Search Solution. 
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
package com.cubrid.common.ui.er.commands;

import org.eclipse.gef.commands.Command;

import com.cubrid.common.ui.er.model.ERTable;
import com.cubrid.common.ui.er.model.Relationship;

/**
 * Command to delete relationship
 * 
 * @author Yu Guojia
 * @version 1.0 - 2013-7-10 created by Yu Guojia
 */
public class DeleteRelationshipCommand extends Command {
	private final ERTable foreignKeySource;
	private final ERTable primaryKeyTarget;
	private final Relationship relationship;

	public DeleteRelationshipCommand(ERTable foreignKeySource,
			ERTable primaryKeyTarget, Relationship relationship) {
		super();
		this.foreignKeySource = foreignKeySource;
		this.primaryKeyTarget = primaryKeyTarget;
		this.relationship = relationship;
	}

	@Override
	public void execute() {
		foreignKeySource.deleteForeignKeyShipAndFire(relationship);
		relationship.setForeignKeyTable(null);
		relationship.setPrimaryKeyTable(null);
	}

	@Override
	public void undo() {
		relationship.setForeignKeyTable(foreignKeySource);
		relationship.setForeignKeyTable(primaryKeyTarget);
		foreignKeySource.addForeignKeyShipAndFire(relationship);
		primaryKeyTarget.FireTargeted(relationship);
	}
}
