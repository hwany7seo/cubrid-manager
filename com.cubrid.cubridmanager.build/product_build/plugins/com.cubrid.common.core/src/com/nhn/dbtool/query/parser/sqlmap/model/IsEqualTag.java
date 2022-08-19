/*
 * Copyright (C) 2015 Search Solution Corporation. All rights reserved by Search
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
package com.nhn.dbtool.query.parser.sqlmap.model;

import java.util.List;

/**
 * A definition of isEqual tag.
 * <isEqual property="" prepend="" open="" close="" removeFirstPrepend="true" compareProperty="" compareValue=""></isEqual>
 *
 * @author Bumsik, Jang
 */
public class IsEqualTag extends SqlMapCondition {
	private static final long serialVersionUID = -868828264629472299L;

	public IsEqualTag() {
		this.setType("isEqual");
	}

	@Override
	public String getExpectedCompareValue() {
		return compareValue != null ? compareValue
				: (compareProperty != null ? "#" + compareProperty + "#" : null);
	}

	@Override
	public boolean isMatchCondition(List<String> parameterList) {
		for (String parameter : parameterList) {
			if (parameter.startsWith(getProperty())) {
				String[] value = parameter.split(":");
				if (value.length == 1 || (value.length == 2 && value[1].equals(this.getExpectedCompareValue()))) {
					return true;
				}
			}
		}

		return false;
	}
}