package org.jbpm.formbuilder.client.form.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.formbuilder.client.effect.FBFormEffect;
import org.jbpm.formbuilder.client.form.FBFormItem;
import org.jbpm.formbuilder.shared.rep.FormItemRepresentation;
import org.jbpm.formbuilder.shared.rep.items.AbsolutePanelRepresentation;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class AbsoluteLayoutFormItem extends LayoutFormItem {

    private AbsolutePanel panel = new AbsolutePanel();
    
    private String id;
    
    public AbsoluteLayoutFormItem(List<FBFormEffect> formEffects) {
        super(formEffects);
        add(panel);
        setSize("90px", "90px");
        panel.setSize(getWidth(), getHeight());
    }

    @Override
    public Map<String, Object> getFormItemPropertiesMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", this.id);
        map.put("height", getHeight());
        map.put("width", getWidth());
        return map;
    }
    
    @Override
    public void saveValues(Map<String, Object> asPropertiesMap) {
        this.id = extractString(asPropertiesMap.get("id"));
        this.setHeight(extractString(asPropertiesMap.get("height")));
        this.setWidth(extractString(asPropertiesMap.get("width")));
        populate(this.panel);
    }

    private void populate(AbsolutePanel panel) {
        if (this.getHeight() != null) {
            panel.setHeight(this.getHeight());
        } 
        if (this.getWidth() != null) {
            panel.setWidth(this.getWidth());
        }
    }

    @Override
    public FormItemRepresentation getRepresentation() {
        AbsolutePanelRepresentation rep = new AbsolutePanelRepresentation();
        rep.setWidth(this.getWidth());
        rep.setHeight(this.getHeight());
        rep.setId(this.id);
        for (FBFormItem item : getItems()) {
            rep.addItem(item.getRepresentation(), 
                    item.getDesiredX() - panel.getAbsoluteLeft(), 
                    item.getDesiredY() - panel.getAbsoluteTop());
        }
        return rep;
    }

    @Override
    public FBFormItem cloneItem() {
        AbsoluteLayoutFormItem clone = new AbsoluteLayoutFormItem(getFormEffects());
        clone.setHeight(this.getHeight());
        clone.id = this.id;
        clone.setWidth(this.getWidth());
        clone.populate(clone.panel);
        for (FBFormItem item : getItems()) {
            clone.add(item.cloneItem());
        }
        return clone;
    }

    @Override
    public boolean add(FBFormItem item) {
        int left = item.getDesiredX();
        int top = item.getDesiredY();
        panel.add(item, left - panel.getAbsoluteLeft(), top - panel.getAbsoluteTop());
        return super.add(item);
    }
    
    @Override
    public Panel getPanel() {
        return this.panel;
    }

    @Override
    public Widget cloneDisplay() {
        AbsolutePanel ap = new AbsolutePanel();
        populate(ap);
        for (FBFormItem item : getItems()) {
            ap.add(item.cloneDisplay(), this.getAbsoluteLeft() - item.getDesiredX(), this.getAbsoluteTop() - item.getDesiredY());
        }
        return ap;
    }
}