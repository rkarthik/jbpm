package org.jbpm.formbuilder.client.command;

import org.jbpm.formbuilder.client.bus.NotificationEvent;
import org.jbpm.formbuilder.client.bus.NotificationEvent.Level;
import org.jbpm.formbuilder.shared.rep.FormRepresentation;
import org.jbpm.formbuilder.shared.rep.trans.LanguageException;

import com.google.gwt.user.client.Window;

public class SaveFormAsXslCommand extends SaveFormCommand {

    public SaveFormAsXslCommand() {
        super("xsl");
    }

    @Override
    public void saveForm(FormRepresentation form) {
        try {
            String xslContent = form.translate("xsl");
            String fileName = form.getTaskId() + ".xslt";
            Window.alert("FILE: "+ fileName + "\n" + xslContent);
            //TODO super.bus.fireEvent(new CreateFileEvent(xslContent, fileName));
        } catch (LanguageException e) {
            bus.fireEvent(new NotificationEvent(Level.ERROR, 
                    "Unexpected error while saving xsl form", e));
        }
    }
}