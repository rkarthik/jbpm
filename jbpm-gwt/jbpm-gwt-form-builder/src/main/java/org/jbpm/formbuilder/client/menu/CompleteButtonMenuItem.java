/**
 * Copyright 2011 JBoss Inc 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formbuilder.client.menu;

import org.jbpm.formbuilder.client.form.CompleteButtonFormItem;
import org.jbpm.formbuilder.client.form.FBFormItem;
import org.jbpm.formbuilder.client.resources.FormBuilderResources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Label;


public class CompleteButtonMenuItem extends FBMenuItem {


    @Override
    protected Label getDescription() {
        return new Label("Complete Button");
    }
    
    @Override
    protected ImageResource getIconUrl() {
        return FormBuilderResources.INSTANCE.completeButton();
    }

    @Override
    public FBMenuItem cloneWidget() {
        return new CompleteButtonMenuItem();
    }

    @Override
    public FBFormItem buildWidget() {
        return new CompleteButtonFormItem();
    }
}