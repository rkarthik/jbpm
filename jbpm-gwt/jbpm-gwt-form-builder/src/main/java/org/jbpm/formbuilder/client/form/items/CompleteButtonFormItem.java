package org.jbpm.formbuilder.client.form.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.formbuilder.client.effect.FBFormEffect;
import org.jbpm.formbuilder.client.form.FBFormItem;
import org.jbpm.formbuilder.shared.rep.FormItemRepresentation;
import org.jbpm.formbuilder.shared.rep.items.CompleteButtonRepresentation;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class CompleteButtonFormItem extends FBFormItem {

    private Button button = new Button("Complete");

    private String innerText;
    private String name;
    private String id;
    private String cssStyleName;
    
    public CompleteButtonFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        add(button);
        setHeight("20px");
        setWidth("40px");
        button.setSize(getWidth(), getHeight());
    }
    
    @Override
    public void saveValues(Map<String, Object> asPropertiesMap) {
        setHeight(extractString(asPropertiesMap.get("height")));
        setWidth(extractString(asPropertiesMap.get("width")));
        this.name = extractString(asPropertiesMap.get("name"));
        this.id = extractString(asPropertiesMap.get("id"));
        this.innerText = extractString(asPropertiesMap.get("innerText"));
        this.cssStyleName = extractString(asPropertiesMap.get("cssStyleName"));
        
        populate(this.button);
    }

    private void populate(Button button) {
        if (getHeight() != null) {
            button.setHeight(getHeight());
        }
        if (getWidth() != null) {
            button.setWidth(getWidth());
        }
        if (this.innerText != null) {
            button.setText(this.innerText);
        }
        if (this.cssStyleName != null) {
            button.setStyleName(this.cssStyleName);
        }
    }

    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("height", getHeight());
        map.put("width", getWidth());
        map.put("innerText", this.innerText);
        map.put("cssStyleName", this.cssStyleName);
        map.put("name", this.name);
        map.put("id", this.id);
        return map;
    }
    
    @Override
    public FormItemRepresentation getRepresentation() {
        CompleteButtonRepresentation rep = new CompleteButtonRepresentation();
        rep.setText(this.innerText);
        rep.setName(this.name);
        rep.setId(this.id);
        rep.setWidth(getWidth());
        rep.setHeight(getHeight());
        return rep;
    }
    
    @Override
    public FBFormItem cloneItem() {
        CompleteButtonFormItem clone = new CompleteButtonFormItem(getFormEffects());
        clone.cssStyleName = this.cssStyleName;
        clone.setHeight(this.getHeight());
        clone.setWidth(this.getWidth());
        clone.id = this.id;
        clone.innerText = this.innerText;
        clone.name = this.name;
        clone.populate(clone.button);
        return clone;
    }
    
    @Override
    public Widget cloneDisplay() {
        Button bt = new Button();
        populate(bt);
        return bt;
    }
}