package org.jbpm.formbuilder.client.form.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.formbuilder.client.effect.FBFormEffect;
import org.jbpm.formbuilder.client.form.FBFormItem;
import org.jbpm.formbuilder.client.form.editors.ServerScriptEditor;
import org.jbpm.formbuilder.shared.rep.FormItemRepresentation;
import org.jbpm.formbuilder.shared.rep.items.ServerTransformationRepresentation;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class ServerTransformationFormItem extends FBFormItem {

    private Label scriptMarker = new Label("{ script }");
    
    private TextArea script = new TextArea();
    private String language;
    
    public ServerTransformationFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        scriptMarker.setStyleName("transformationBlockBorder");
        add(scriptMarker);
    }

    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("language", this.language);
        return map;
    }

    @Override
    public void saveValues(Map<String, Object> asPropertiesMap) {
        this.language = extractString(asPropertiesMap.get("language"));
    }

    @Override
    public FormItemRepresentation getRepresentation() {
        ServerTransformationRepresentation rep = getRepresentation(new ServerTransformationRepresentation());
        rep.setScript(this.script.getValue());
        rep.setLanguage(this.language);
        return rep;
    }

    @Override
    public FBFormItem cloneItem() {
        ServerTransformationFormItem clone = cloneItem(new ServerTransformationFormItem(getFormEffects()));
        clone.setScriptContent(this.getScriptContent());
        clone.language = this.language;
        return clone;
    }

    @Override
    public Widget cloneDisplay() {
        Label display = new Label(scriptMarker.getText());
        display.setHeight(getHeight());
        display.setWidth(getWidth());
        display.setStyleName("transformationBlockBorder");
        return display;
    }

    @Override
    public Widget createInplaceEditor() {
        return new ServerScriptEditor(this);
    }

    public void setScriptContent(String value) {
        script.setValue(value);
    }
    
    public String getScriptContent() {
        return script.getValue();
    }
}