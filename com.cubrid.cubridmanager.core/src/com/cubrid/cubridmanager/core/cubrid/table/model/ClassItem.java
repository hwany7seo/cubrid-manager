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
package com.cubrid.cubridmanager.core.cubrid.table.model;

import com.cubrid.cubridmanager.core.utils.ModelUtil.ClassType;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is to store basic class information:
 * <li>class(table) name
 * <li>owner or creator name
 * <li>class type: a normal class(table) or a virtual class(view)
 *
 * @author moulinwang 2009-3-26
 */
public class ClassItem {
    private String classname = null;
    /* this class's owner DBA,PUBLIC... */
    private String owner = null;
    /*
     * a normal class(table) or a virtual class(view),it's value is view or
     * normal
     */
    private String virtual = null;

    private List<String> superclassList;

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getVirtual() {
        return virtual;
    }

    public void setVirtual(String classType) {
        this.virtual = classType;
    }

    public boolean isVirtual() {
        return virtual != null && virtual.equals(ClassType.VIEW.getText());
    }

    /**
     * Get supper class list
     *
     * @return List<String> The supercalssList
     */
    public List<String> getSuperclassList() {
        if (superclassList == null) {
            superclassList = new ArrayList<String>();
        }
        return superclassList;
    }

    /**
     * Add a supper class to superclassList
     *
     * @param superClass String A string that includes the info super class
     */
    public void addSuperclass(String superClass) {
        if (superclassList == null) {
            superclassList = new ArrayList<String>();
        }
        this.superclassList.add(superClass);
    }
}
