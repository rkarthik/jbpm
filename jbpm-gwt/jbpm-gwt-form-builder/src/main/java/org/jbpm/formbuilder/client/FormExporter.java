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
package org.jbpm.formbuilder.client;

import org.jbpm.formbuilder.client.bus.GetFormRepresentationEvent;
import org.jbpm.formbuilder.client.bus.GetFormRepresentationResponseEvent;
import org.jbpm.formbuilder.client.bus.GetFormRepresentationResponseHandler;
import org.jbpm.formbuilder.client.bus.UndoableEvent;
import org.jbpm.formbuilder.client.bus.UndoableHandler;
import org.jbpm.formbuilder.client.bus.ui.NotificationEvent;
import org.jbpm.formbuilder.client.bus.ui.NotificationEvent.Level;
import org.jbpm.formbuilder.client.messages.Constants;
import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;
import org.jbpm.formbuilder.shared.form.FormEncodingException;
import org.jbpm.formbuilder.shared.form.FormEncodingFactory;
import org.jbpm.formbuilder.shared.form.FormRepresentationEncoder;
import org.jbpm.formbuilder.shared.rep.FormRepresentation;

import com.google.gwt.event.shared.EventBus;

public class FormExporter {

    private static final String EXPORT_TYPE = FormExporter.class.getName();

    private final Constants i18n = FormBuilderGlobals.getInstance().getI18n();
    private final EventBus bus = FormBuilderGlobals.getInstance().getEventBus();
    
    public FormExporter() {
        bus.addHandler(UndoableEvent.TYPE, new UndoableHandler() {
            @Override
            public void onEvent(UndoableEvent event) {
                bus.fireEvent(new GetFormRepresentationEvent(EXPORT_TYPE));
            }
            @Override
            public void doAction(UndoableEvent event) { }
            @Override
            public void undoAction(UndoableEvent event) { }
        });
        
        bus.addHandler(GetFormRepresentationResponseEvent.TYPE, new GetFormRepresentationResponseHandler() {
            @Override
            public void onEvent(GetFormRepresentationResponseEvent event) {
                if (EXPORT_TYPE.equals(event.getSaveType())) {
                    exportForm(event.getRepresentation());
                }
            }
        });
    }
    
    protected void exportForm(FormRepresentation form) {
        FormRepresentationEncoder encoder = FormEncodingFactory.getEncoder();
        try {
            String formAsJson = encoder.encode(form);
            setClientExportForm(formAsJson);
        } catch (FormEncodingException e) {
            bus.fireEvent(new NotificationEvent(Level.ERROR, i18n.CouldntExportAsJson(), e));
        }
    }

    protected final native void start() /*-{
        if (typeof($doc.clientExportForm) == 'undefined') {
            $doc.clientExportForm = "";
        } else if ($doc.clientExportForm == null) {
            $doc.clientExportForm = "";
        }
    }-*/;
    
    private final native void setClientExportForm(String formAsJson) /*-{
        $doc.clientExportForm = formAsJson;
    }-*/;
}