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

import org.jbpm.formbuilder.client.menu.FBMenuItem;
import org.jbpm.formbuilder.client.menu.FBMenuPanel;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class MenuView extends SimplePanel {

    private FBMenuPanel panel; 

    public MenuView() {
        setSize("270px", "245px");
        Grid grid = new Grid(1,1);
        grid.setWidget(0, 0, panel);
        grid.setSize("100%", "100%");
        grid.setBorderWidth(2);
        add(grid);
    }
    
    public void setPanel(FBMenuPanel panel) {
        this.panel = panel;
        ((Grid) getWidget()).setWidget(0, 0, this.panel);
    }
    
    public void addItem(FBMenuItem item, String group) {
        panel.add(item); //TODO
    }
    
    public void addItemGroup(Widget widget) {
        
    }
}