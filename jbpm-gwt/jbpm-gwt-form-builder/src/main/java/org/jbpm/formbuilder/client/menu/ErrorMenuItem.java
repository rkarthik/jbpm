package org.jbpm.formbuilder.client.menu;

import java.util.Map;

import org.jbpm.formbuilder.client.form.FBFormItem;
import org.jbpm.formbuilder.client.resources.FormBuilderResources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ErrorMenuItem extends FBMenuItem {

    private final String errMsg;
    
    public ErrorMenuItem(String errMsg) {
        this.errMsg = errMsg;
    }
    
    @Override
    public FBMenuItem cloneWidget() {
        return new ErrorMenuItem(this.errMsg);
    }

    @Override
    protected ImageResource getIconUrl() {
        return FormBuilderResources.INSTANCE.errorIcon();
    }

    @Override
    protected Label getDescription() {
        return new Label("Error: " + errMsg);
    }

    @Override
    public FBFormItem buildWidget() {
        return new FBFormItem() {
            @Override
            public void saveValues(Map<String, Object> asPropertiesMap) {
            }
            
            @Override
            public Widget createInplaceEditor() {
                return null;
            }
            
            @Override
            public String asCode(String type) {
                return null;
            }
        };
    }

}