package org.jbpm.formbuilder.shared.rep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.formbuilder.shared.rep.trans.LanguageException;
import org.jbpm.formbuilder.shared.rep.trans.LanguageFactory;

public class FormRepresentation {

    private String name;
    private String taskId;
    private String action;
    private String method;
    private String enctype;
    private List<FBValidation> formValidations = new ArrayList<FBValidation>();
    private List<FormItemRepresentation> formItems = new ArrayList<FormItemRepresentation>();
    private Map<String, OutputData> outputs = new HashMap<String, OutputData>();
    private Map<String, InputData> inputs = new HashMap<String, InputData>();
    private List<FBScript> onLoadScripts;
    private List<FBScript> onSubmitScripts;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<FBValidation> getFormValidations() {
        return formValidations;
    }

    public void setFormValidations(List<FBValidation> formValidations) {
        this.formValidations = formValidations;
    }
    
    public void addFormValidation(FBValidation formValidation) {
        this.formValidations.add(formValidation);
    }

    public Map<String, InputData> getInputs() {
        return inputs;
    }

    public void setInputs(Map<String, InputData> inputs) {
        this.inputs = inputs;
    }
    
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        if (action == null) {
            action = "complete";
        }
        this.action = action;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        if (method == null) {
            method = "POST";
        }
        this.method = method;
    }

    public String getEnctype() {
        return enctype;
    }

    public void setEnctype(String enctype) {
        if (enctype == null) {
            enctype = "multipart/form-data";
        }
        this.enctype = enctype;
    }

    public Map<String, OutputData> getOutputs() {
        return outputs;
    }

    public void setOutputs(Map<String, OutputData> outputs) {
        this.outputs = outputs;
    }
    
    public List<FormItemRepresentation> getFormItems() {
        return formItems;
    }
    
    public void setFormItems(List<FormItemRepresentation> formItems) {
        this.formItems = formItems;
    }
    
    public void addFormItem(FormItemRepresentation formItem) {
        this.formItems.add(formItem);
    }
    
    public List<FBScript> getOnLoadScripts() {
        return onLoadScripts;
    }
    
    public void setOnLoadScripts(List<FBScript> onLoadScripts) {
        this.onLoadScripts = onLoadScripts;
    }
    
    public List<FBScript> getOnSubmitScripts() {
        return onSubmitScripts;
    }
    
    public void setOnSubmitScripts(List<FBScript> onSubmitScripts) {
        this.onSubmitScripts = onSubmitScripts;
    }
    
    public String translate(String language) throws LanguageException {
        return LanguageFactory.getInstance().getLanguage(language).translateForm(this);
    }
}