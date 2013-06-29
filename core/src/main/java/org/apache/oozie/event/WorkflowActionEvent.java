/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.oozie.event;

import java.util.Date;

import org.apache.oozie.AppType;
import org.apache.oozie.client.WorkflowAction;
import org.apache.oozie.client.event.JobEvent;
import org.apache.oozie.service.EventHandlerService;
import org.apache.oozie.util.XLog;

/**
 * Class implementing JobEvent for events generated by Workflow Actions
 */
public class WorkflowActionEvent extends JobEvent {

    private WorkflowAction.Status status;
    private String hadoopId;
    private String errorCode;
    private String errorMessage;

    public WorkflowActionEvent(String id, String parentId, WorkflowAction.Status status, String user, String appName,
            Date startTime, Date endTime) {
        super(id, parentId, user, AppType.WORKFLOW_ACTION, appName);
        setStatus(status);
        setStartTime(startTime);
        setEndTime(endTime);
        XLog.getLog(EventHandlerService.class).trace("Event generated - " + this.toString());
    }

    public WorkflowAction.Status getStatus() {
        return status;
    }

    public void setStatus(WorkflowAction.Status actionStatus) {
        status = actionStatus;
        // set high-level status for event based on low-level actual job status
        // this is to ease filtering on the consumer side
        switch (actionStatus) {
            case OK:
                setEventStatus(EventStatus.SUCCESS);
                break;
            case RUNNING:
                setEventStatus(EventStatus.STARTED);
                break;
            case ERROR:
            case KILLED:
            case FAILED:
                setEventStatus(EventStatus.FAILURE);
                break;
            case START_MANUAL:
            case END_MANUAL:
                setEventStatus(EventStatus.SUSPEND);
        }
    }

    public String getHadoopId() {
        return hadoopId;
    }

    public void setHadoopId(String id) {
        hadoopId = id;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String code) {
        errorCode = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String msg) {
        errorMessage = msg;
    }

}
