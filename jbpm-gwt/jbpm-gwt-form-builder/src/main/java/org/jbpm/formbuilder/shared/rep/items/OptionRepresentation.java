package org.jbpm.formbuilder.shared.rep.items;

import org.jbpm.formbuilder.shared.rep.FormItemRepresentation;

public class OptionRepresentation extends FormItemRepresentation {

    private String label;
    private String value;

    public OptionRepresentation() {
        super("option");
    }
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}