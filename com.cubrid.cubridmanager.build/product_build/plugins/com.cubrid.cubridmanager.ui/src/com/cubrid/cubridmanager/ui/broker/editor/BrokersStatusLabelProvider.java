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
package com.cubrid.cubridmanager.ui.broker.editor;

import static com.cubrid.common.core.util.NoOp.noOp;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.cubrid.common.core.util.CompatibleUtil;
import com.cubrid.cubridmanager.core.broker.model.BrokerInfo;
import com.cubrid.cubridmanager.core.common.model.ServerInfo;

/**
 * A label provider which is responsible for the table in the type of
 * BlocksStatusEditor
 *
 *
 * @author lizhiqiang
 * @version 1.0 - 2009-5-18 created by lizhiqiang
 */
public class BrokersStatusLabelProvider implements
		ITableLabelProvider {
	private ServerInfo serverInfo;

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
	 *      int)
	 * @param element the object representing the entire row, or
	 *        <code>null</code> indicating that no input object is set in the
	 *        viewer
	 * @param columnIndex the zero-based index of the column in which the label
	 *        appears
	 * @return Image or <code>null</code> if there is no image for the given
	 *         object at columnIndex
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
	 *      int)
	 *
	 * @param element the object representing the entire row, or
	 *        <code>null</code> indicating that no input object is set in the
	 *        viewer
	 * @param columnIndex the zero-based index of the column in which the label
	 *        appears
	 * @return String or or <code>null</code> if there is no text for the given
	 *         object at columnIndex
	 */
	public String getColumnText(Object element, int columnIndex) {
		BrokerInfo brokerInfo = (BrokerInfo) element;
		boolean isQueryOrTransTimeUseMs = false;
		if (serverInfo != null) {
			isQueryOrTransTimeUseMs = CompatibleUtil.isQueryOrTransTimeUseMs(serverInfo);
		}
		switch (columnIndex) {
		case 0:
			return brokerInfo.getName();
		case 1:
			return brokerInfo.getState();
		case 2:
			return brokerInfo.getPid();
		case 3:
			return brokerInfo.getPort();
		case 4:
			return brokerInfo.getAs();
		case 5:
			return brokerInfo.getJq();
		case 6:
			return brokerInfo.getReq();
		case 7:
			return brokerInfo.getTran();
		case 8:
			return brokerInfo.getQuery();
		case 9:
			String longTran = brokerInfo.getLong_tran();
			String longTranTime = brokerInfo.getLong_query_time();
			if (isQueryOrTransTimeUseMs) {
				double dVal = Double.parseDouble(longTranTime) * 1000;
				longTranTime = String.valueOf((int) (dVal + 0.5));
			}
			return longTran + "/" + longTranTime;
		case 10:
			String longQuery = brokerInfo.getLong_query();
			String longQueryTime = brokerInfo.getLong_query_time();
			if (isQueryOrTransTimeUseMs) {
				double dVal = Double.parseDouble(longQueryTime) * 1000;
				longQueryTime = String.valueOf((int) (dVal + 0.5));
			}
			return longQuery + "/" + longQueryTime;
		case 11:
			return brokerInfo.getError_query();
		default:
			return "";
		}
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 * @param listener a label provider listener
	 */
	public void addListener(ILabelProviderListener listener) {
		noOp();
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
		noOp();
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
	 *      java.lang.String)
	 *
	 * @param element the element
	 * @param property the property
	 * @return <code>true</code> if the label would be affected, and
	 *         <code>false</code> if it would be unaffected
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 * @param listener a label provider listener
	 */
	public void removeListener(ILabelProviderListener listener) {
		noOp();
	}

	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}

}
