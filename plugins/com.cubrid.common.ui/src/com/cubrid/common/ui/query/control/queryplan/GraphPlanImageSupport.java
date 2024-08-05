/*
 * Copyright (C) 2013 Search Solution Corporation. All rights reserved by Search
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
package com.cubrid.common.ui.query.control.queryplan;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.cubrid.common.core.queryplan.model.PlanNode;
import com.cubrid.common.core.util.StringUtil;
import com.cubrid.common.ui.CommonUIPlugin;

public class GraphPlanImageSupport {
	public static Image getImage(PlanNode planNode) {
		String method = planNode.getMethod();
		if (method == null) {
			return CommonUIPlugin.getImage("icons/queryplan/default.png");
		}
		method = method.toLowerCase();
		method = method.replaceAll(" ", "_");
		method = method.replaceAll("\\-", "_");
		method = method.replaceAll("\\(", "");
		method = method.replaceAll("\\)", "");

		if (method.equals("temp") && StringUtil.isNotEmpty(planNode.getOrder())) {
			method = "temp_order";
		} else if (method.equals("tempgroup_by")) {
			method = "temp_group_by";
		} else if (method.equals("temporder_by")) {
			method = "temp_order";
		}

		String imagePath = "icons/queryplan/" + method + ".png";
		ImageDescriptor imageDescript = CommonUIPlugin.getImageDescriptor(imagePath);
		if (imageDescript == null) {
			return CommonUIPlugin.getImage("icons/queryplan/default.png");
		}

		return CommonUIPlugin.getImage(imagePath);
	}

	public static Image getDefaultImage() {
		return CommonUIPlugin.getImage("icons/queryplan/default.png");
	}
}
