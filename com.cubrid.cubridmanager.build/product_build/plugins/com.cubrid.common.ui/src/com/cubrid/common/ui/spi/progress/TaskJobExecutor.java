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
package com.cubrid.common.ui.spi.progress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

import com.cubrid.common.core.task.ITask;

/**
 * 
 * A abstract class that has <code>Task</code> array and run method in order to
 * execute a specific function. Generally,subclasses should be an inner class in
 * a dialog and implements the <code>exec<code> method
 * 
 * @author pangqiren
 * @version 1.0 - 2009-6-4 created by pangqiren
 */
public abstract class TaskJobExecutor extends
		JobChangeAdapter {

	private boolean isCanceled;
	protected List<ITask> taskList = new ArrayList<ITask>();

	/**
	 * This method is to be complete the concrete task.When the task be canceled
	 * by user or the task have completed, the return value should be
	 * <code>Status.CANCEL_STATUS</code> to indicate that the thread is
	 * stopped,or it should be default value<code>Status.OK_STATUS;</code>.
	 * Generally,the subclasses should override this method .
	 * 
	 * @param monitor the monitor object
	 * @return the status
	 */
	public abstract IStatus exec(final IProgressMonitor monitor);

	/**
	 * Calls the cancel method in <code>Task</code> class in order to stop the
	 * socket connection.
	 * 
	 * @see com.cubrid.cubridmanager.core.common.socket#cancel()
	 * 
	 */
	public void cancel() {
		if (isCanceled) {
			return;
		}
		setCanceled(true);
		for (ITask task : taskList) {
			if (null != task) {
				task.cancel();
			}
		}

	}

	/**
	 * Sets the concrete task.
	 * 
	 * @param tasks the task array
	 */
	public void setTask(ITask[] tasks) {
		if (tasks != null && tasks.length > 0) {
			taskList.addAll(Arrays.asList(tasks));
		}
	}

	/**
	 * 
	 * Add a task
	 * 
	 * @param task the task object
	 */
	public void addTask(ITask task) {
		if (!taskList.contains(task)) {
			taskList.add(task);
		}
	}

	/**
	 * Realse all Task
	 */
	public void releaseTask() {
		taskList = new ArrayList<ITask>();
	}

	/**
	 * 
	 * Return whether it is canceled
	 * 
	 * @return <code>true</code> if it is canceled; <code>false</code> otherwise
	 */
	public boolean isCanceled() {
		return isCanceled;
	}

	/**
	 * Set cancel status
	 * 
	 * @param isCanceled the isCanceled to set
	 */
	public void setCanceled(boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	/**
	 * 
	 * Start the task job
	 * 
	 * @param jobName the Job name
	 * @param jobFamily the JobFamily object
	 * @param isShowProgessDialog whether show progress dialog
	 * @param jobPriority the <code>Job</code> priority
	 */
	public final void schedule(String jobName, JobFamily jobFamily,
			boolean isShowProgessDialog, int jobPriority) {
		TaskJob job = new TaskJob(jobName, this);
		if (jobFamily != null) {
			job.setJobFamily(jobFamily);
		}
		job.setPriority(jobPriority);
		job.setUser(isShowProgessDialog);
		job.schedule();
	}
	
	public int getTaskCount() {
		if(taskList == null) {
			return 0;
		}
		
		return taskList.size();
	}
}
