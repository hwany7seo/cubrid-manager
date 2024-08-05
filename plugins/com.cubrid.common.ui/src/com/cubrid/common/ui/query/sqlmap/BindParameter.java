/*
 * Copyright (C) 2014 Search Solution Corporation. All rights reserved by Search
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
package com.cubrid.common.ui.query.sqlmap;

/**
 * <p>
 * The bind parameter class.
 * </p>
 *
 * <p>
 * In order to replace the parameter name with the user defined value,
 * this class might be used.
 * It try to wrap the value with single quotations depending on the type.
 * </p>
 *
 * @author CHOE JUNGYEON
 */
public class BindParameter {

	private String name;
	private String value;
	private BindParameterType type;

	public BindParameter(String name, String value, BindParameterType type) {
		setName(name);
		setValue(value);
		setType(type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BindParameterType getType() {
		return type;
	}

	public void setType(BindParameterType type) {
		this.type = type;
	}

	public enum BindParameterType {
		NUMBER("N") {
			public String wrap(String string) {
				return string;
			}
		},
		STRING("S") {
			public String wrap(String string) {
				return "'" + string + "'";
			}
		},
		FUNCTION("F") {
			public String wrap(String string) {
				return string;
			}
		};

		public String wrap(String string) {
			return string;
		}

		String type;

		BindParameterType(String type) {
			this.type = type;
		}
	}

}
