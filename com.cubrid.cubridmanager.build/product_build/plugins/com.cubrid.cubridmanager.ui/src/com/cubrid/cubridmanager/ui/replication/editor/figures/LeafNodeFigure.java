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
package com.cubrid.cubridmanager.ui.replication.editor.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import com.cubrid.common.ui.spi.ResourceManager;

/**
 * 
 * This figure is a leaf node figure,it can not comprise the other figures
 * 
 * @author pangqiren
 * @version 1.0 - 2009-8-26 created by pangqiren
 */
public class LeafNodeFigure extends
		Figure {

	private final Label label;

	public LeafNodeFigure() {
		this.label = new Label();
		this.add(label);
		setOpaque(true);
		setBorder(new LineBorder(1));
		setBackgroundColor(ResourceManager.getColor(230, 230, 230));
	}

	public String getText() {
		return this.label.getText();
	}

	/**
	 * set the icon
	 * 
	 * @param icon Image
	 */
	public void setIcon(Image icon) {
		label.setIcon(icon);
	}

	/**
	 * get the bound of text.
	 * 
	 * @return this.label.getTextBounds()
	 */
	public Rectangle getTextBounds() {
		return this.label.getTextBounds();
	}

	/**
	 * set the text.
	 * 
	 * @param name String
	 */
	public void setText(String name) {
		this.label.setText(name);
		this.repaint();
	}

	/**
	 * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
	 * @param rect Rectangle
	 */
	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
		this.label.setBounds(rect);
	}
}
