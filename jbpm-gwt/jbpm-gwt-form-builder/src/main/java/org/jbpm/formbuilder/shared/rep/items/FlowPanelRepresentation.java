package org.jbpm.formbuilder.shared.rep.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jbpm.formbuilder.client.form.FormEncodingClientFactory;
import org.jbpm.formbuilder.shared.form.FormEncodingException;
import org.jbpm.formbuilder.shared.form.FormRepresentationDecoder;
import org.jbpm.formbuilder.shared.rep.FormItemRepresentation;

public class FlowPanelRepresentation extends FormItemRepresentation {

    private String cssClassName;
    private String id;
    private List<FormItemRepresentation> items = new ArrayList<FormItemRepresentation>();
    
    public FlowPanelRepresentation() {
		super("flowPanel");
	}

	public String getCssClassName() {
		return cssClassName;
	}

	public void setCssClassName(String cssClassName) {
		this.cssClassName = cssClassName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<FormItemRepresentation> getItems() {
		return items;
	}

	public void setItems(List<FormItemRepresentation> items) {
		this.items = items;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void setDataMap(Map<String, Object> data) throws FormEncodingException {
		super.setDataMap(data);
		this.cssClassName = (String) data.get("cssClassName");
		this.id = (String) data.get("id");
		this.items.clear();
		List<Object> mapItems = (List<Object>) data.get("items");
		FormRepresentationDecoder decoder = FormEncodingClientFactory.getDecoder();
		if (mapItems != null) {
			for (Object obj : mapItems) {
				Map<String, Object> itemMap = (Map<String, Object>) obj;
				FormItemRepresentation item = (FormItemRepresentation) decoder.decode(itemMap);
				this.items.add(item);
			}
		}
	}
	
	@Override
	public Map<String, Object> getDataMap() {
		Map<String, Object> data = super.getDataMap();
		data.put("cssClassName", this.cssClassName);
		data.put("id", this.id);
		List<Object> mapItems = new ArrayList<Object>();
		for (FormItemRepresentation item : this.items) {
			mapItems.add(item.getDataMap());
		}
		data.put("items", mapItems);
		return data;
	}
}