package org.jbpm.formbuilder.client;

import org.jbpm.formapi.client.CommonGlobals;
import org.jbpm.formbuilder.client.bus.ui.HistoryStoreEvent;
import org.jbpm.formbuilder.client.bus.ui.HistoryStoreHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.History;

/**
 * TODO Use the HistoryStoreEvent to actually create history management. Ideas:
 * 		on saving a form: #formId=something
 * 		on loading a form: #formId=something
 * 		on selecting a userTask: #process=processName
 * 								 #task=taskName
 */
public class HistoryPresenter {

	private EventBus bus = CommonGlobals.getInstance().getEventBus();
	
	public HistoryPresenter() {
		bus.addHandler(HistoryStoreEvent.TYPE, new HistoryStoreHandler() {
			public void onEvent(HistoryStoreEvent event) {
				if (event.getTokens() != null && !event.getTokens().isEmpty()) {
					for (String token : event.getTokens()) {
						History.newItem(token);
					}
				}
				if (event.getValueChangeHandler() != null) {
					History.addValueChangeHandler(event.getValueChangeHandler());
				}
			}
		});
	}
}
