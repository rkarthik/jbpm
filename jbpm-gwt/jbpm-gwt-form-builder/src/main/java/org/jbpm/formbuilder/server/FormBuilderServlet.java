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
package org.jbpm.formbuilder.server;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.drools.repository.RulesRepository;
import org.jboss.seam.Component;
import org.jboss.seam.contexts.Contexts;
import org.jbpm.formbuilder.client.menu.FBMenuItem;
import org.jbpm.formbuilder.client.options.MainMenuOption;
import org.jbpm.formbuilder.server.form.GuvnorFormDefinitionService;
import org.jbpm.formbuilder.server.menu.GuvnorMenuService;
import org.jbpm.formbuilder.server.render.Renderer;
import org.jbpm.formbuilder.server.render.RendererException;
import org.jbpm.formbuilder.server.render.RendererFactory;
import org.jbpm.formbuilder.server.task.GuvnorTaskDefinitionService;
import org.jbpm.formbuilder.server.trans.Language;
import org.jbpm.formbuilder.server.trans.LanguageException;
import org.jbpm.formbuilder.server.trans.LanguageFactory;
import org.jbpm.formbuilder.server.xml.FormEffectDTO;
import org.jbpm.formbuilder.server.xml.ListMenuItemsDTO;
import org.jbpm.formbuilder.server.xml.ListOptionsDTO;
import org.jbpm.formbuilder.server.xml.ListTasksDTO;
import org.jbpm.formbuilder.server.xml.MenuGroupDTO;
import org.jbpm.formbuilder.server.xml.MenuItemDTO;
import org.jbpm.formbuilder.server.xml.MenuOptionDTO;
import org.jbpm.formbuilder.server.xml.MetaDataDTO;
import org.jbpm.formbuilder.server.xml.PropertyDTO;
import org.jbpm.formbuilder.server.xml.TaskRefDTO;
import org.jbpm.formbuilder.shared.form.FormDefinitionService;
import org.jbpm.formbuilder.shared.menu.MenuService;
import org.jbpm.formbuilder.shared.rep.FormRepresentation;
import org.jbpm.formbuilder.shared.task.TaskDefinitionService;
import org.jbpm.formbuilder.shared.task.TaskRef;

public class FormBuilderServlet extends HttpServlet {

    private static final long serialVersionUID = -5961620265453738055L;

    private MenuService menuService;
    private TaskDefinitionService taskService;
    private FormDefinitionService formService;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.menuService = new GuvnorMenuService();
        this.taskService = new GuvnorTaskDefinitionService();
        if ( Contexts.isApplicationContextActive() ) {
            RulesRepository repo = (RulesRepository) Component.getInstance( "repository" );
            this.formService = new GuvnorFormDefinitionService(repo);
        }
    }
    
    /*
     * all URLs are supposed to be "/<contextPath>/<pkgName>/<operation>"
     */
    private String extractPackageName(String uri, String operation) {
        String[] folders = uri.split("/");
        for (int index = 0; index < folders.length; index++) {
            if (folders[index].equals(operation)) {
                return folders[index - 1];
            }
        }
        return null;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String uri = req.getRequestURI();
            StringBuilder content = new StringBuilder();
            if (uri.contains("menuItems")) {
                resp.setContentType("text/xml");
                content.append(listMenuItems());
            } else if (uri.contains("menuOptions")) {
                resp.setContentType("text/xml");
                content.append(listOptions());
            } else if (uri.contains("listTasks")) {
                resp.setContentType("text/xml");
                content.append(listTasks(extractPackageName(uri, "listTasks"), req.getParameter("q")));
            } else if (uri.contains("listValidations")) {
                resp.setContentType("text/xml");
                //TODO implement
            } else if (uri.contains("formPreview")) {
                resp.setContentType("text/xml");
                content.append(getFormPreview(req.getRequestURI()));
                //TODO implement
            }
            
            
            resp.getWriter().println(content.toString());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        } 
    }
    
    private String getUriParameter(String requestUri, String paramName) {
        paramName = "/" + paramName + "/";
        int start = requestUri.indexOf(paramName) + paramName.length();
        int end = requestUri.indexOf("/", start + 1);
        if (end == -1) {
            end = requestUri.length();
        }
        return requestUri.substring(start, end);
    }
    
    private String getFormPreview(String requestUri) throws LanguageException, RendererException {
        StringBuilder builder = new StringBuilder();
        String taskId = getUriParameter(requestUri, "formPreview");
        String language = getUriParameter(requestUri, "lang");
        String pkgName = getUriParameter(requestUri, "package");
        LanguageFactory factory = LanguageFactory.getInstance();
        Language translator = factory.getLanguage(language);
        FormRepresentation form = formService.getFormByTaskId(pkgName, taskId);
        URL url = translator.translateForm(form);
        RendererFactory factory2 = RendererFactory.getInstance();
        Renderer renderer = factory2.getRenderer(language);
        builder.append(renderer.render(url, new HashMap<String, Object>()));
        return builder.toString();
        
    }

    private String listTasks(String filter, String pkgName) throws JAXBException {
        List<TaskRef> tasks = taskService.query(pkgName, filter);
        ListTasksDTO dto = new ListTasksDTO(tasks);
        return jaxbTransformation(dto, ListTasksDTO.class, TaskRefDTO.class, PropertyDTO.class, MetaDataDTO.class);
    }
    
    private String listMenuItems() throws JAXBException {
        Map<String, List<FBMenuItem>> items = menuService.listItems();
        ListMenuItemsDTO dto = new ListMenuItemsDTO(items);
        return jaxbTransformation(dto, ListMenuItemsDTO.class, MenuGroupDTO.class, MenuItemDTO.class, FormEffectDTO.class);
    }

    private String listOptions() throws JAXBException {
        List<MainMenuOption> options = menuService.listOptions();
        ListOptionsDTO dto = new ListOptionsDTO(options);
        return jaxbTransformation(dto, ListOptionsDTO.class, MenuOptionDTO.class);
    }

    private String jaxbTransformation(Object dto, Class<?>... boundClasses) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(boundClasses);
        Marshaller marshaller = ctx.createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.marshal(dto, writer);
        return writer.toString();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        // TODO Auto-generated method stub
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        // TODO Auto-generated method stub
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        // TODO Auto-generated method stub
    }
    
    public void setFormService(FormDefinitionService formService) {
        this.formService = formService;
    }
    
    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }
    
    public void setTaskService(TaskDefinitionService taskService) {
        this.taskService = taskService;
    }
}