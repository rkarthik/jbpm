package org.jbpm.formbuilder.shared.rep.items;

import java.util.Map;

import org.jbpm.formbuilder.client.form.FormEncodingClientFactory;
import org.jbpm.formbuilder.shared.form.FormEncodingException;
import org.jbpm.formbuilder.shared.form.FormRepresentationDecoder;
import org.jbpm.formbuilder.shared.rep.FormItemRepresentation;

public class ConditionalBlockRepresentation extends FormItemRepresentation {

    private FormItemRepresentation ifBlock;
    private FormItemRepresentation elseBlock;
    private String condition;
    
    public ConditionalBlockRepresentation() {
        super("conditionalBlock");
    }

    public FormItemRepresentation getIfBlock() {
        return ifBlock;
    }

    public void setIfBlock(FormItemRepresentation ifBlock) {
        this.ifBlock = ifBlock;
    }

    public FormItemRepresentation getElseBlock() {
        return elseBlock;
    }

    public void setElseBlock(FormItemRepresentation elseBlock) {
        this.elseBlock = elseBlock;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    @Override
    public Map<String, Object> getDataMap() {
        Map<String, Object> data = super.getDataMap();
        data.put("condition", this.condition);
        data.put("ifBlock", this.ifBlock == null ? null : this.ifBlock.getDataMap());
        data.put("elseBlock", this.elseBlock == null ? null : this.elseBlock.getDataMap());
        return data;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void setDataMap(Map<String, Object> data) {
        super.setDataMap(data);
        this.condition = (String) data.get("condition");
        FormRepresentationDecoder decoder = FormEncodingClientFactory.getDecoder();
        try {
            this.ifBlock = (FormItemRepresentation) decoder.decode((Map<String, Object>) data.get("ifBlock"));
        } catch (FormEncodingException e) {
            //TODO see how to manage this error
        }
        try {
            this.elseBlock = (FormItemRepresentation) decoder.decode((Map<String, Object>) data.get("elseBlock"));
        } catch (FormEncodingException e) {
            //TODO see how to manage this error
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof ConditionalBlockRepresentation)) return false;
        ConditionalBlockRepresentation other = (ConditionalBlockRepresentation) obj;
        boolean equals = (this.condition == null && other.condition == null) || 
            (this.condition != null && this.condition.equals(other.condition));
        if (!equals) return equals;
        equals = (this.ifBlock == null && other.ifBlock == null) || 
            (this.ifBlock != null && this.ifBlock.equals(other.ifBlock));
        if (!equals) return equals;
        equals = (this.elseBlock == null && other.elseBlock == null) || 
            (this.elseBlock != null && this.elseBlock.equals(other.elseBlock));
        return equals;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        int aux = this.condition == null ? 0 : this.condition.hashCode();
        result = 37 * result + aux;
        aux = this.ifBlock == null ? 0 : this.ifBlock.hashCode();
        result = 37 * result + aux;
        aux = this.elseBlock == null ? 0 : this.elseBlock.hashCode();
        result = 37 * result + aux;
        return result;
    }
}