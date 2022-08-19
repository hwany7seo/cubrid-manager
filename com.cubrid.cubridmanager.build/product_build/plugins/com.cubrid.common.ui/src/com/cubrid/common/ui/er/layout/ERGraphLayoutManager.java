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
package com.cubrid.common.ui.er.layout;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

import com.cubrid.common.ui.er.part.SchemaDiagramPart;
import com.cubrid.common.ui.er.part.TablePart;

/**
 * Layout figures on the schema diagram automatically.
 * 
 * @author Yu Guojia
 * @version 1.0 - 2013-7-10 created by Yu Guojia
 */
public class ERGraphLayoutManager extends AbstractLayout {
	private SchemaDiagramPart schemaDiagram;

	public ERGraphLayoutManager(SchemaDiagramPart schemaDiagram) {
		this.schemaDiagram = schemaDiagram;
	}

	protected Dimension calculatePreferredSize(IFigure figure, int wHint,
			int hHint) {
		return figure.getPreferredSize();
	}

	public void layout(IFigure figure) {
		new ERGraphLayoutVisitor().layout(schemaDiagram);
		schemaDiagram.setTableModelBounds();
	}

	public void tmpAutoLayout() {
		new ERGraphLayoutVisitor().layout(schemaDiagram);
		for (int i = 0; i < schemaDiagram.getChildren().size(); i++) {
			TablePart tp = (TablePart) schemaDiagram.getChildren().get(i);
			tp.refreshVisuals();
		}
		schemaDiagram.setTableModelBounds();
	}
}