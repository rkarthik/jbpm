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
package org.jbpm.formbuilder.client;

import org.jbpm.formbuilder.client.resources.FormBuilderGlobals;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;

public class FormBuilderEntryPoint implements EntryPoint {

    public void onModuleLoad() {
        FormBuilderModel model = new FormBuilderModel();
        
        AbsolutePanel panel = new AbsolutePanel();
        PickupDragController dragController = new PickupDragController(panel, false);
        FormBuilderGlobals.getInstance().registerDragController(dragController);
        dragController.registerDropController(new DisposeDropController(panel));
        
        Grid mainGrid = new Grid(1, 2);
        Grid editGrid = new Grid(2, 1);
        
        MenuController menuController = createMenu(model);
        
        editGrid.setWidget(0, 0, menuController.getView());
        editGrid.setWidget(1, 0, createEdition(model).asWidget());
        
        mainGrid.setWidget(0, 0, editGrid);
        mainGrid.setWidget(0, 1, createLayout(model).asWidget());
        
        panel.add(mainGrid);
        
        RootPanel.get("formBuilder").add(panel);
    }

    private EditionView createEdition(FormBuilderModel model) {
        EditionView editionView = new EditionView();
        new EditionController(model, editionView);
        return editionView;
    }

    private MenuController createMenu(FormBuilderModel model) {
        FormBuilderModel menuModel = new FormBuilderModel();
        MenuView menuView = new MenuView();
        return new MenuController(model, menuView);
    }

    private LayoutView createLayout(FormBuilderModel model) {
        LayoutView layoutView = new LayoutView();
        new LayoutController(model, layoutView);
        return layoutView;
    }
}