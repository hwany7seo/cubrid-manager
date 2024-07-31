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
package com.cubrid.common.ui.query.editor;

import com.cubrid.common.ui.query.control.CombinedQueryEditorComposite;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

/**
 * SubQueryEditorTabItem
 *
 * @author Kevin.Wang
 *     <p>Create at 2014-4-8
 */
public class SubQueryEditorTabItem extends CTabItem {
    private CombinedQueryEditorComposite control;
    private int tabIndex;

    public SubQueryEditorTabItem(CTabFolder parent, int style) {
        super(parent, style);
    }

    public void setControl(CombinedQueryEditorComposite control) {
        this.control = control;
        super.setControl(control);
    }

    public CombinedQueryEditorComposite getControl() {
        return control;
    }

    /** @return the tabIndex */
    public int getTabIndex() {
        return tabIndex;
    }

    /** @param tabIndex the tabIndex to set */
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }
}
