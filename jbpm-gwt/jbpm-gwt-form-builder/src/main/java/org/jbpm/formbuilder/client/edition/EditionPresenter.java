/**
 * Copyright 2011 JBoss Inc 
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
package org.jbpm.formbuilder.client.edition;

import org.jbpm.formbuilder.client.bus.FormItemSelectionEvent;
import org.jbpm.formbuilder.client.bus.FormItemSelectionEventHandler;
import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;

import com.google.gwt.event.shared.EventBus;

public class EditionPresenter {

    private final EditionView editView;
    private final EventBus bus;
    
    public EditionPresenter(EditionView view) {
        super();
        this.editView = view;
        this.bus = FormBuilderGlobals.getInstance().getEventBus();
        
        this.bus.addHandler(FormItemSelectionEvent.TYPE, new FormItemSelectionEventHandler() {
            public void onEvent(FormItemSelectionEvent event) {
                if (event.isSelected()) {
                    editView.populate(event.getFormItemSelected());
                } else {
                    editView.clear();
                }
            }
        });
    }
}