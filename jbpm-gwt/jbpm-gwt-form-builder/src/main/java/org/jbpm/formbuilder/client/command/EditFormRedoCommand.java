package org.jbpm.formbuilder.client.command;

import org.jbpm.formbuilder.client.bus.UndoRedoEvent;
import org.jbpm.formbuilder.client.bus.UndoRedoEventHandler;
import org.jbpm.formbuilder.client.bus.UndoableEvent;
import org.jbpm.formbuilder.client.bus.UndoableEventHandler;
import org.jbpm.formbuilder.client.options.UndoRedoManager;
import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.MenuItem;

public class EditFormRedoCommand implements BaseCommand {

    private final UndoRedoManager mgr = UndoRedoManager.getInstance();
    private MenuItem item = null;
    
    public EditFormRedoCommand() {
        EventBus bus = FormBuilderGlobals.getInstance().getEventBus();
        bus.addHandler(UndoableEvent.TYPE, new UndoableEventHandler() {
            public void onEvent(UndoableEvent event) {  
                checkEnabled();
            }
            public void doAction(UndoableEvent event) {  }
            public void undoAction(UndoableEvent event) {  }
        });
        bus.addHandler(UndoRedoEvent.TYPE, new UndoRedoEventHandler() {
            public void onEvent(UndoRedoEvent event) {
                checkEnabled();
            }
         });
    }
    
    public void execute() {
        if (mgr.canRedo()) {
            mgr.redo();
        }
        checkEnabled();
    }

    public void setItem(MenuItem item) {
        this.item = item;
        checkEnabled();
    }

    private void checkEnabled() {
        if (this.item != null) {
            this.item.setEnabled(mgr.canRedo());
        }
    }
}