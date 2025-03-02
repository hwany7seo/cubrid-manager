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
package com.cubrid.cubridmanager.core.cubrid.trigger.model;

import com.cubrid.common.core.common.model.Trigger;
import junit.framework.TestCase;

/**
 * test trigger model
 *
 * @author wuyingshi
 * @version 1.0 - 2009-12-30 created by wuyingshi
 */
public class TriggerTest extends TestCase {
    /** execute test contents */
    public void testTrigger() {
        String name = "name";
        String conditionTime = "conditionTime";
        String eventType = "eventType";
        String target_class = "target_class";
        String target_attribute = "target_attribute";
        String condition = "condition";
        String actionTime = "actionTime";
        String actionType = "actionType";
        String action = "action";
        String status = "status";
        String priority = "2";

        Trigger trigger = new Trigger();
        // test 	getters and setters
        trigger.setName(name);
        trigger.setConditionTime(conditionTime);
        trigger.setEventType(eventType);
        trigger.setTarget_class(target_class);
        trigger.setTarget_att(target_attribute);
        trigger.setCondition(condition);
        trigger.setActionTime(actionTime);
        trigger.setActionType(actionType);
        trigger.setAction(action);
        trigger.setStatus(status);
        trigger.setPriority(priority);
        // test 	public int compareTo(Trigger obj)
        assertNotNull(trigger.compareTo(trigger));
        // test 	public boolean equals(Object obj)
        assertTrue(trigger.equals(trigger));
        assertFalse(trigger.equals(null));
        assertFalse(trigger.equals("other object"));
        // test public int hashCode()
        trigger.hashCode();
        trigger.setAction("REJECT1");
        trigger.setAction("INVALIDATE TRANSACTION1");
        trigger.setAction("PRINT12");
        trigger.setCondition(null);
        trigger.setCondition("if you");

        assertEquals(trigger.getName(), name);
        assertEquals(trigger.getConditionTime(), conditionTime);
        assertEquals(trigger.getEventType(), eventType);
        assertEquals(trigger.getTarget_class(), target_class);
        assertEquals(trigger.getTarget_att(), target_attribute);
        assertEquals(trigger.getCondition(), "you");
        assertEquals(trigger.getActionTime(), actionTime);
        assertEquals(trigger.getActionType(), "PRINT");
        assertEquals(trigger.getAction(), "");
        assertEquals(trigger.getStatus(), status);
        assertEquals(trigger.getPriority(), "02.00");

        // assertNotNull(Trigger.formatPriority("0"));

    }
}
