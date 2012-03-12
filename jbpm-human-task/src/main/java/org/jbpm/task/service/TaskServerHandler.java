/**
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.task.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.SystemEventListener;
import org.jbpm.eventmessaging.EventKey;
import org.jbpm.task.Attachment;
import org.jbpm.task.Comment;
import org.jbpm.task.Content;
import org.jbpm.task.OrganizationalEntity;
import org.jbpm.task.Task;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.persistence.TaskServiceSession;

public class TaskServerHandler {

	private final TaskService service;
    private final Map<String, SessionWriter> clients;

    /**
     * Listener used for logging
     */
    private final SystemEventListener systemEventListener;

    public TaskServerHandler(TaskService service, SystemEventListener systemEventListener) {
        this.service = service;
        this.clients = new HashMap<String, SessionWriter>();
        this.systemEventListener = systemEventListener;
    }

    public void exceptionCaught(SessionWriter session, Throwable cause) throws Exception {
        systemEventListener.exception("Uncaught exception on Server", cause);
    }

    public void messageReceived(SessionWriter session, Object message) throws Exception {
        Command cmd = (Command) message;
        TaskServiceSession taskSession = service.createSession();
        CommandName response = null;
        try {
            systemEventListener.debug("Message receieved on server : " + cmd.getName());
            systemEventListener.debug("Arguments : " + Arrays.toString(cmd.getArguments().toArray()));

            switch (cmd.getName()) {
                case OperationRequest: {
                    // prepare
                    response = CommandName.OperationResponse;
                    Operation operation = (Operation) cmd.getArguments().get(0);

                    systemEventListener.debug("Command receieved on server was operation of type: " + operation);

                    long taskId = (Long) cmd.getArguments().get(1);
                    String userId = (String) cmd.getArguments().get(2);
                    String targetEntityId = null;
                    ContentData data = null;
                    List<String> groupIds = null;
                    if (cmd.getArguments().size() > 3) {
                        targetEntityId = (String) cmd.getArguments().get(3);
                        if (cmd.getArguments().size() > 4) {
                            data = (ContentData) cmd.getArguments().get(4);
                            if (cmd.getArguments().size() > 5) {
                                groupIds = (List<String>) cmd.getArguments().get(5);
                            }
                        }
                    }
                    
                    // execute
                    taskSession.taskOperation(operation, taskId, userId, targetEntityId, data, groupIds);

                    // return 
                    List args = Collections.emptyList();
                    Command resultsCmnd = new Command(cmd.getId(), CommandName.OperationResponse, args);
                    session.write(resultsCmnd);
                    break;
                }
                case GetTaskRequest: {
                    // prepare
                    response = CommandName.GetTaskResponse;
                    long taskId = (Long) cmd.getArguments().get(0);

                    // execute
                    Task task = taskSession.getTask(taskId);

                    // return
                    List args = Arrays.asList((new Task[] {task}));
                    Command resultsCmnd = new Command(cmd.getId(), CommandName.GetTaskResponse, args);
                    session.write(resultsCmnd);
                    break;
                }
                case AddTaskRequest: {
                    // prepare
                    response = CommandName.AddTaskResponse;
                    Task task = (Task) cmd.getArguments().get(0);
                    ContentData content = (ContentData) cmd.getArguments().get(1);
                    
                    // execute
                    taskSession.addTask(task, content);

                    // return
                    List args = Arrays.asList((new Long[] {task.getId()}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.AddTaskResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case AddCommentRequest: {
                    // prepare
                    response = CommandName.AddCommentResponse;
                    Comment comment = (Comment) cmd.getArguments().get(1);
                    
                    // execute
                    taskSession.addComment((Long) cmd.getArguments().get(0), comment);

                    // return
                    List args = Arrays.asList((new Long[] {comment.getId()}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.AddCommentResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case DeleteCommentRequest: {
                    // prepare
                    response = CommandName.DeleteCommentResponse;
                    long taskId = (Long) cmd.getArguments().get(0);
                    long commentId = (Long) cmd.getArguments().get(1);
                    
                    // execute
                    taskSession.deleteComment(taskId, commentId);

                    // return
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.DeleteCommentResponse,
                            Collections.emptyList());
                    session.write(resultsCmnd);
                    break;
                }
                case AddAttachmentRequest: {
                    // prepare
                    response = CommandName.AddAttachmentResponse;
                    Attachment attachment = (Attachment) cmd.getArguments().get(1);
                    Content content = (Content) cmd.getArguments().get(2);
                    
                    // execute
                    taskSession.addAttachment((Long) cmd.getArguments().get(0), attachment, content);

                    // return
                    List args = Arrays.asList((new Long[] {attachment.getId(), content.getId()}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.AddAttachmentResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case DeleteAttachmentRequest: {
                    // prepare
                    response = CommandName.DeleteAttachmentResponse;
                    long taskId = (Long) cmd.getArguments().get(0);
                    long attachmentId = (Long) cmd.getArguments().get(1);
                    long contentId = (Long) cmd.getArguments().get(2);
                    
                    // execute
                    taskSession.deleteAttachment(taskId, attachmentId, contentId);
                    
                    // return
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.DeleteAttachmentResponse,
                            Collections.emptyList());
                    session.write(resultsCmnd);
                    break;
                }
                case SetDocumentContentRequest: {
                    // prepare
                    response = CommandName.SetDocumentContentResponse;
                    long taskId = (Long) cmd.getArguments().get(0);
                    Content content = (Content) cmd.getArguments().get(1);
                    
                    // execute
                    taskSession.setDocumentContent(taskId,
                            content);

                    // return
                    List args = Arrays.asList((new Long[] {content.getId()}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.SetDocumentContentResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case GetContentRequest: {
                    // prepare
                    response = CommandName.GetContentResponse;
                    long contentId = (Long) cmd.getArguments().get(0);
                    
                    // execute
                    Content content = taskSession.getContent(contentId);

                    // return
                    List args = Arrays.asList((new Content[] {content}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.GetContentResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case QueryTaskByWorkItemId: {
                    // prepare
                    response = CommandName.QueryTaskByWorkItemIdResponse;
                    
                    // execute
                    Task result = taskSession.getTaskByWorkItemId((Long) cmd.getArguments().get(0));

                    // return
                    List args = Arrays.asList((new Task[] {result}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskByWorkItemIdResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case QueryTasksOwned: {
                    // prepare
                    response = CommandName.QueryTaskSummaryResponse;
                    
                    // execute
                    List<TaskSummary> results = taskSession.getTasksOwned(
                            (String) cmd.getArguments().get(0),
                            (String) cmd.getArguments().get(1));

                    // return
                    List args = Arrays.asList((new List[] {results}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskSummaryResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case QueryTasksAssignedAsBusinessAdministrator: {
                    // prepare
                    response = CommandName.QueryTaskSummaryResponse;
                    
                    // execute
                    List<TaskSummary> results = taskSession.getTasksAssignedAsBusinessAdministrator(
                            (String) cmd.getArguments().get(0),
                            (String) cmd.getArguments().get(1));

                    // return
                    List args = Arrays.asList((new List[] {results}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskSummaryResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case QueryTasksAssignedAsPotentialOwner: {
                    // prepare
                    response = CommandName.QueryTaskSummaryResponse;
                    
                    // execute
                    List<TaskSummary> results = taskSession.getTasksAssignedAsPotentialOwner(
                            (String) cmd.getArguments().get(0),
                            (String) cmd.getArguments().get(1));


                    // return
                    List args = Arrays.asList((new List[] {results}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskSummaryResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case QueryTasksAssignedAsPotentialOwnerWithGroup: {
                    // prepare
                    response = CommandName.QueryTaskSummaryResponse;
                    
                    // execute
                    List<TaskSummary> results = taskSession.getTasksAssignedAsPotentialOwner(
                    		(String) cmd.getArguments().get(0),
                            (List<String>) cmd.getArguments().get(1),
                            (String) cmd.getArguments().get(2),
                            (Integer) cmd.getArguments().get(3),
                            (Integer) cmd.getArguments().get(4));

                    // return
                    List args = Arrays.asList((new List[] {results}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskSummaryResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case QueryTasksAssignedAsPotentialOwnerByGroup: {
                    // prepare
                    response = CommandName.QueryTaskSummaryResponse;
                    
                    // execute
                    List<TaskSummary> results = taskSession.getTasksAssignedAsPotentialOwnerByGroup(
                            (String) cmd.getArguments().get(0),
                            (String) cmd.getArguments().get(1));

                    // return
                    List args = Arrays.asList((new List[] {results}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskSummaryResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case QuerySubTasksAssignedAsPotentialOwner: {
                    // prepare
                    response = CommandName.QueryTaskSummaryResponse;
                    
                    // execute
                    List<TaskSummary> results = taskSession.getSubTasksAssignedAsPotentialOwner(
                            (Long) cmd.getArguments().get(0),
                            (String) cmd.getArguments().get(1),
                            (String) cmd.getArguments().get(2));

                    // return
                    List args = Arrays.asList((new List[] {results}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskSummaryResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case QueryGetSubTasksByParentTaskId: {
                    // prepare
                    response = CommandName.QueryTaskSummaryResponse;
                    
                    // execute
                    List<TaskSummary> results = taskSession.getSubTasksByParent(
                            (Long) cmd.getArguments().get(0),
                            (String) cmd.getArguments().get(1));

                    // return
                    List args = Arrays.asList((new List[] {results}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskSummaryResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }


                case QueryTasksAssignedAsTaskInitiator: {
                    // prepare
                    response = CommandName.QueryTaskSummaryResponse;
                    
                    // execute
                    List<TaskSummary> results = taskSession.getTasksAssignedAsTaskInitiator(
                            (String) cmd.getArguments().get(0),
                            (String) cmd.getArguments().get(1));

                    // return
                    List args = Arrays.asList((new List[] {results}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskSummaryResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case QueryTasksAssignedAsExcludedOwner: {
                    // prepare
                    response = CommandName.QueryTaskSummaryResponse;
                    
                    // execute
                    List<TaskSummary> results = taskSession.getTasksAssignedAsExcludedOwner(
                            (String) cmd.getArguments().get(0),
                            (String) cmd.getArguments().get(1));

                    // return
                    List args = Arrays.asList((new List[] {results}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskSummaryResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case QueryTasksAssignedAsRecipient: {
                    // prepare
                    response = CommandName.QueryTaskSummaryResponse;
                    
                    // execute
                    List<TaskSummary> results = taskSession.getTasksAssignedAsRecipient(
                            (String) cmd.getArguments().get(0),
                            (String) cmd.getArguments().get(1));

                    // return
                    List args = Arrays.asList((new List[] {results}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskSummaryResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case QueryTasksAssignedAsTaskStakeholder: {
                    // prepare
                    response = CommandName.QueryTaskSummaryResponse;
                    
                    // execute
                    List<TaskSummary> results = taskSession.getTasksAssignedAsTaskStakeholder(
                            (String) cmd.getArguments().get(0),
                            (String) cmd.getArguments().get(1));

                    // return
                    List args = Arrays.asList((new List[] {results}));
                    Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryTaskSummaryResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case RegisterForEventRequest: {
                    // prepare
                    response = CommandName.EventTriggerResponse;
                    EventKey key = (EventKey) cmd.getArguments().get(0);
                    boolean remove = (Boolean) cmd.getArguments().get(1);
                    String uuid = (String) cmd.getArguments().get(2);
                    
                    // execute
                    clients.put(uuid, session);
                    EventTransport transport = new EventTransport(uuid,
									                            cmd.getId(),
									                            clients,
									                            remove);
                    
                    service.getEventKeys().register(key, transport);
                    break;
                }
                case RegisterClient: {
                    // prepare
                    response = CommandName.RegisterClient;
                    String uuid = (String) cmd.getArguments().get(0);
                    
                    // execute
                    clients.put(uuid, session);
                    break;
                }
                case QueryGenericRequest: {
                    // prepare
                	String qlString = (String) cmd.getArguments().get(0);
                	Integer size = (Integer) cmd.getArguments().get(1);
                	Integer offset = (Integer) cmd.getArguments().get(2);
                	
                    // execute
                	List<?> results = taskSession.query(qlString, size, offset);

                    // return
                    List args = Arrays.asList((new List[] {results}));
                	Command resultsCmnd = new Command(cmd.getId(),
                            CommandName.QueryGenericResponse,
                            args);
                    session.write(resultsCmnd);
                    break;
                }
                case NominateTaskRequest:  {
                    // prepare
                	response = CommandName.OperationResponse;
                	long taskId = (Long) cmd.getArguments().get(0);
                	String userId = (String) cmd.getArguments().get(1);
                	List<OrganizationalEntity> potentialOwners = (List<OrganizationalEntity>) cmd.getArguments().get(2);
                	
                    // execute
                	taskSession.nominateTask(taskId, userId, potentialOwners);
                	
                    // return
                	List args = Collections.emptyList();
                	Command resultsCmnd = new Command(cmd.getId(), CommandName.OperationResponse, args);
                	session.write(resultsCmnd);
                	break;
                }
                case SetOutputRequest: {
                    // prepare
                	response = CommandName.OperationResponse;
                	long taskId = (Long) cmd.getArguments().get(0);
                	String userId = (String) cmd.getArguments().get(1);
                	ContentData outputContentData = (ContentData) cmd.getArguments().get(2);
                	
                    // execute
                	taskSession.setOutput(taskId, userId, outputContentData);
                	
                    // return
                	List args = Collections.emptyList();
                	Command resultsCmnd = new Command(cmd.getId(), CommandName.OperationResponse, args);
                	session.write(resultsCmnd);
                	break;
                }
                case DeleteOutputRequest: {
                    // prepare
                	response = CommandName.OperationResponse;
                	long taskId = (Long) cmd.getArguments().get(0);
                	String userId = (String) cmd.getArguments().get(1);
                	
                    // execute
                	taskSession.deleteOutput(taskId, userId);
                	
                    // return
                	List args = Collections.emptyList();
                	Command resultsCmnd = new Command(cmd.getId(), CommandName.OperationResponse, args);
                	session.write(resultsCmnd);
                	
                	break;
                }
                case SetFaultRequest: {
                    // prepare
                	response = CommandName.OperationResponse;
                	long taskId = (Long) cmd.getArguments().get(0);
                	String userId = (String) cmd.getArguments().get(1);
                	FaultData data = (FaultData) cmd.getArguments().get(2);
                	
                    // execute
                	taskSession.setFault(taskId, userId, data);
                	
                    // return
                	List args = Collections.emptyList();
                	Command resultsCmnd = new Command(cmd.getId(), CommandName.OperationResponse, args);
                	session.write(resultsCmnd);
                	break;
                }
                case DeleteFaultRequest: {
                    // prepare
                	response = CommandName.OperationResponse;
                	long taskId = (Long) cmd.getArguments().get(0);
                	String userId = (String) cmd.getArguments().get(1);
                	
                    // execute
                	taskSession.deleteFault(taskId, userId);
                	
                    // return
                	List args = Collections.emptyList();
                	Command resultsCmnd = new Command(cmd.getId(), CommandName.OperationResponse, args);
                	session.write(resultsCmnd);
                	break;
                }
                case SetPriorityRequest: {
                    // prepare
                	response = CommandName.OperationResponse;
                	long taskId = (Long) cmd.getArguments().get(0);
                	String userId = (String) cmd.getArguments().get(1);
                	int priority = (Integer) cmd.getArguments().get(2);
                	
                    // execute
                	taskSession.setPriority(taskId, userId, priority);
                	
                    // return
                	List args = Collections.emptyList();
                	Command resultsCmnd = new Command(cmd.getId(), CommandName.OperationResponse, args);
                	session.write(resultsCmnd);
                	break;
                }
                default: {
                    systemEventListener.debug("Unknown command recieved on server");
                }
            }
        } catch (RuntimeException e) {
            systemEventListener.exception(e.getMessage(),e);
            e.printStackTrace(System.err);
            List<Object> list = new ArrayList<Object>(1);
            list.add(e);
            Command resultsCmnd = new Command(cmd.getId(), response, list);
            session.write(resultsCmnd);
        } finally {
            taskSession.dispose();
        }
    }

}