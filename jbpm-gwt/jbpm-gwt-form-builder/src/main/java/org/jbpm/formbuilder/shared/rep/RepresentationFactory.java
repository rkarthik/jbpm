package org.jbpm.formbuilder.shared.rep;

import java.util.HashMap; 
import java.util.Map;

public class RepresentationFactory {

    private static final Map<String, String> MAPPING = new HashMap<String, String>();
    
    //TODO make this cleaner and configurable
    static {
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.AbsolutePanelRepresentation", 
                "org.jbpm.formbuilder.client.form.items.AbsoluteLayoutFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.CheckBoxRepresentation",
                "org.jbpm.formbuilder.client.form.items.CheckBoxFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.ComboBoxRepresentation", 
                "org.jbpm.formbuilder.client.form.items.ComboBoxFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.CompleteButtonRepresentation",
                "org.jbpm.formbuilder.client.form.items.CompleteButtonFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.ConditionalBlockRepresentation",
                "org.jbpm.formbuilder.client.form.items.ConditionalBlockFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.FileInputRepresentation",
                "org.jbpm.formbuilder.client.form.items.FileInputFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.HeaderRepresentation",
                "org.jbpm.formbuilder.client.form.items.HeaderFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.HiddenRepresentation",
                "org.jbpm.formbuilder.client.form.items.HiddenFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.HorizontalPanelRepresentation",
                "org.jbpm.formbuilder.client.form.items.HorizontalLayoutFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.HTMLRepresentation",
                "org.jbpm.formbuilder.client.form.items.HTMLFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.ImageRepresentation",
                "org.jbpm.formbuilder.client.form.items.ImageFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.LabelRepresentation",
                "org.jbpm.formbuilder.client.form.items.LabelFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.LoopBlockRepresentation",
                "org.jbpm.formbuilder.client.form.items.LoopBlockFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.PasswordFieldRepresentation",
                "org.jbpm.formbuilder.client.form.items.PasswordFieldFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.RadioButtonRepresentation",
                "org.jbpm.formbuilder.client.form.items.RadioButtonFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.ServerTransformationRepresentation",
                "org.jbpm.formbuilder.client.form.items.ServerTransformationFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.TableRepresentation",
                "org.jbpm.formbuilder.client.form.items.TableLayoutFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.TextAreaRepresentation",
                "org.jbpm.formbuilder.client.form.items.TextAreaFormItem");
        registerItemClassName("org.jbpm.formbuilder.shared.rep.items.TextFieldRepresentation",
                "org.jbpm.formbuilder.client.form.items.TextFieldFormItem");
    }

    public static void registerItemClassName(String repClassName, String itemClassName) {
        MAPPING.put(repClassName, itemClassName);
    }
    
    public static String getItemClassName(String repClassName) {
        return MAPPING.get(repClassName);
    }
    
}