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
package com.cubrid.common.ui.spi.util.paramSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.cubrid.common.core.util.StringUtil;
import com.cubrid.common.ui.cubrid.table.dialog.PstmtParameter;

/**
 * DoubleParamSetter
 * 
 * @author Kevin.Wang
 * @version 1.0 - 2013-7-17 created by Kevin.Wang
 */
public class DoubleParamSetter extends DefaultParamSetter {
	public void handle(PreparedStatement stmt, PstmtParameter parameter)
			throws SQLException, ParamSetException {
		Object value = parameter.getParamValue();
		if (value == null || StringUtil.isEmpty(value.toString())) {
			stmt.setNull(parameter.getParamIndex(), Types.NULL);
			return;
		}
		double dValue = 0;
		try {
			dValue = Double.parseDouble(value.toString());
		} catch (NumberFormatException ex) {
			throw new ParamSetException(ex, parameter);
		}
		stmt.setDouble(parameter.getParamIndex(), dValue);
	}
}
