package org.jbpm.formbuilder.client.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.formbuilder.shared.rep.InputData;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InputMapPanel extends PopupPanel {

    private final Button okButton = new Button("Ok");
    private final Button cancelButton = new Button("Cancel");
    private final Map<String, InputData> inputs;
    
    private final Map<String, Object> retData = new HashMap<String, Object>();
    
    public InputMapPanel(Map<String, InputData> inputs) {
        super(false, true);
        this.inputs = inputs;
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        mainPanel.add(new Label("Input Map population"));
        mainPanel.add(createInputTable());
        mainPanel.add(createButtonPanel());
        setWidget(mainPanel);
    }

    private Grid createInputTable() {
        Grid inputTable = new Grid(this.inputs.size(), 2);
        List<InputData> inputList = new ArrayList<InputData>(this.inputs.values());
        for (int index = 0; index < inputList.size(); index++) {
            final InputData input = inputList.get(index);
            populateRetData(input);
            inputTable.setWidget(index, 0, new Label(input.getName()));
            final TextBox inputText = new TextBox();
            inputText.setValue(input.getValue());
            inputText.addChangeHandler(new ChangeHandler() {
                public void onChange(ChangeEvent event) {
                    input.setValue(inputText.getValue());
                    populateRetData(input);
                }
            });
            inputTable.setWidget(index, 1, inputText);
        }
        return inputTable;
    }

    private void populateRetData(final InputData input) {
        if (input.getFormatter() == null) {
            retData.put(input.getName(), input.getValue());
        } else {
            retData.put(input.getName(), input.getFormatter().format(input.getValue()));
        }
    }

    private HorizontalPanel createButtonPanel() {
        HorizontalPanel buttons = new HorizontalPanel();
        okButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        cancelButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        buttons.add(okButton);
        buttons.add(cancelButton);
        return buttons;
    }
    
    public HandlerRegistration addOkHandler(ClickHandler handler) {
        return okButton.addClickHandler(handler);
    }
    
    public Map<String, Object> getInputs() {
        return retData; 
    }
}