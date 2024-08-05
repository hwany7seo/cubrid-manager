/*
 * Copyright (C) 2012 Search Solution Corporation. All rights reserved by Search
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
package com.cubrid.common.ui.spi.util;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

import com.cubrid.common.core.util.StringUtil;

public class FormValidateUtil {
	public static boolean isEmpty(Text text) {
		if (text == null) {
			return true;
		}

		return StringUtil.isEmpty(text.getText());
	}

	public static boolean isEmpty(Combo combo) {
		if (combo == null) {
			return true;
		}

		return StringUtil.isEmpty(combo.getText());
	}

	/**
	 * return a string from Text widget.
	 *
	 * @param text
	 * @return
	 */
	public static String getString(Text text) {
		if (text == null) {
			return null;
		}

		return text.getText();
	}

	/**
	 * return a string from Combo widget.
	 *
	 * @param text
	 * @return
	 */
	public static String getString(Combo text) {
		if (text == null) {
			return null;
		}

		return text.getText();
	}
}
