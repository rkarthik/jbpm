package org.jbpm.task.service.local;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.drools.SystemEventListenerFactory;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.jbpm.process.workitem.wsht.SyncWSHumanTaskHandler;
import org.jbpm.task.service.TaskService;

public class LocalHumanTaskService {
	
	private static TaskService INSTANCE;
	
	public static TaskService getService(Environment environment) {
		if (INSTANCE == null) {
			EntityManagerFactory emf = (EntityManagerFactory) environment.get(EnvironmentName.ENTITY_MANAGER_FACTORY);
			if (emf == null) {
				emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
			}
	        TaskService taskService = new TaskService(emf, SystemEventListenerFactory.getSystemEventListener());
	        INSTANCE = taskService;
		}
		return INSTANCE;
	}
	
	public static org.jbpm.task.TaskService getTaskService(StatefulKnowledgeSession ksession) {
	    TaskService taskService = getService(ksession.getEnvironment());
		
		SyncWSHumanTaskHandler humanTaskHandler = new SyncWSHumanTaskHandler(
			new LocalTaskService(taskService), ksession);
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanTaskHandler);
		
		return new LocalTaskService(taskService);
	}

}
