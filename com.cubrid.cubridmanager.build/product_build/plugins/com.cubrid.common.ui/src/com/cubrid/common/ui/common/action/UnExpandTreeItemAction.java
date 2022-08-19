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
package com.cubrid.common.ui.common.action;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.cubrid.common.ui.spi.model.ICubridNode;

/**
 * 
 * Filter setting action for tree
 * 
 * @author pangqiren
 * @version 1.0 - 2010-12-1 created by Isaiah Choe
 */
public class UnExpandTreeItemAction extends
		Action {

	public static final String ID = UnExpandTreeItemAction.class.getName();
	private TreeViewer tv;

	public UnExpandTreeItemAction(String text, ImageDescriptor image,
			TreeViewer tv) {
		super(text);
		setId(ID);
		this.setToolTipText(text);
		this.setImageDescriptor(image);
		this.tv = tv;
	}

	/**
	 * 
	 * Set the navigator view
	 * 
	 * @param cubridNavigatorView CubridNavigatorView
	 */
	public void setTv(TreeViewer tv) {
		this.tv = tv;
	}

	/**
	 * Filter
	 */
	@SuppressWarnings("rawtypes")
	public void run() {
		if (tv != null) {
			ITreeSelection sel = (ITreeSelection) tv.getSelection();
			Iterator it = sel.iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				if (!(obj instanceof ICubridNode)) {
					continue;
				}
				
				tv.collapseToLevel(obj, 1);
			}
		}
	}
}
