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

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.SystemEventListenerFactory;
import org.jbpm.task.BaseTestNoUserGroupSetup;
import org.jbpm.task.OrganizationalEntity;
import org.jbpm.task.Task;
import org.jbpm.task.User;
import org.jbpm.task.service.MockEscalatedDeadlineHandler.Item;
import org.jbpm.task.service.responsehandlers.BlockingAddTaskResponseHandler;

public abstract class TaskServiceEscalationBaseUserGroupCallbackTest extends BaseTestNoUserGroupSetup {

    protected TaskServer server;
    protected TaskClient client;

    public void testDummy() {
        assertTrue( true );
    }
    
    public void testUnescalatedDeadlines() throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put( "users", users );
        vars.put( "groups", groups );

        MockEscalatedDeadlineHandler handler = new MockEscalatedDeadlineHandler();
        taskService.setEscalatedDeadlineHandler( handler );  
        
        //Reader reader;
        Reader reader = new InputStreamReader( TaskServiceEscalationBaseUserGroupCallbackTest.class.getResourceAsStream( MvelFilePath.UnescalatedDeadlines ) );
        List<Task> tasks = (List<Task>) eval( reader,
                                              vars );
        long now = ((Date)vars.get( "now" )).getTime();
        
        for ( Task task : tasks ) {  
            BlockingAddTaskResponseHandler addTaskResponseHandler = new BlockingAddTaskResponseHandler();  
            if(task.getPeopleAssignments() != null && task.getPeopleAssignments().getBusinessAdministrators() != null) {
                List<OrganizationalEntity> businessAdmins = new ArrayList<OrganizationalEntity>();
                businessAdmins.add(new User("Administrator"));
                businessAdmins.addAll(task.getPeopleAssignments().getBusinessAdministrators());
                task.getPeopleAssignments().setBusinessAdministrators(businessAdmins);
            }
            client.addTask( task, null, addTaskResponseHandler ); 
            addTaskResponseHandler.waitTillDone( 1000 );
        }

        handler.wait( 3, 8000 );
        
        assertEquals( 3, handler.getList().size() );
        
        boolean firstDeadlineMet = false;
        boolean secondDeadlineMet = false;
        boolean thirdDeadlineMet = false;
        for( Item item : handler.getList() ) { 
            long deadlineTime = item.getDeadline().getDate().getTime();
            if( deadlineTime == now + 2000 ) { 
                firstDeadlineMet = true;
            }
            else if( deadlineTime == now + 4000 ) { 
                secondDeadlineMet = true;
            }
            else if( deadlineTime == now + 6000 ) { 
                thirdDeadlineMet = true;
            }
            else { 
                fail( deadlineTime + " is not an expected deadline time." );
            }
        }
        
        assertTrue( "First deadline was not met." , firstDeadlineMet );
        assertTrue( "Second deadline was not met." , secondDeadlineMet );
        assertTrue( "Third deadline was not met." , thirdDeadlineMet );
        
        // Wait for deadlines to finish
        Thread.sleep(1000);
    }
    
    public void testUnescalatedDeadlinesOnStartup() throws Exception {
        Map vars = new HashMap();
        vars.put( "users", users );
        vars.put( "groups", groups );

        //Reader reader;
        Reader reader = new InputStreamReader( TaskServiceEscalationBaseUserGroupCallbackTest.class.getResourceAsStream( MvelFilePath.UnescalatedDeadlines ) );
        List<Task> tasks = (List<Task>) eval( reader,
                                              vars );
        long now = ((Date)vars.get( "now" )).getTime();
        
        for ( Task task : tasks ) {
            BlockingAddTaskResponseHandler addTaskResponseHandler = new BlockingAddTaskResponseHandler();  
            if(task.getPeopleAssignments() != null && task.getPeopleAssignments().getBusinessAdministrators() != null) {
                List<OrganizationalEntity> businessAdmins = new ArrayList<OrganizationalEntity>();
                businessAdmins.add(new User("Administrator"));
                businessAdmins.addAll(task.getPeopleAssignments().getBusinessAdministrators());
                task.getPeopleAssignments().setBusinessAdministrators(businessAdmins);
            }
            client.addTask( task, null, addTaskResponseHandler ); 
            addTaskResponseHandler.waitTillDone( 3000 );
        }
        
        // now create a new service, to see if it initiates from the DB correctly
        MockEscalatedDeadlineHandler handler = new MockEscalatedDeadlineHandler();
        new TaskService(emf, SystemEventListenerFactory.getSystemEventListener(), handler);      
        
        handler.wait( 3, 8000 );
        
        boolean firstDeadlineMet = false;
        boolean secondDeadlineMet = false;
        boolean thirdDeadlineMet = false;
        for( Item item : handler.getList() ) { 
            long deadlineTime = item.getDeadline().getDate().getTime();
            if( deadlineTime == now + 2000 ) { 
                firstDeadlineMet = true;
            }
            else if( deadlineTime == now + 4000 ) { 
                secondDeadlineMet = true;
            }
            else if( deadlineTime == now + 6000 ) { 
                thirdDeadlineMet = true;
            }
            else { 
                fail( deadlineTime + " is not an expected deadline time." );
            }
        }
        
        assertTrue( "First deadline was not met." , firstDeadlineMet );
        assertTrue( "Second deadline was not met." , secondDeadlineMet );
        assertTrue( "Third deadline was not met." , thirdDeadlineMet );    
        
        // Wait for deadlines to finish
        Thread.sleep(1000);
    }
    
}
