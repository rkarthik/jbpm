package org.jbpm.formbuilder.server.render.xsl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.jbpm.formbuilder.server.render.RendererException;

public class Renderer implements org.jbpm.formbuilder.server.render.Renderer {

    private final TransformerFactory factory = TransformerFactory.newInstance();
    
    public Object render(URL url, Map<String, Object> inputData) throws RendererException {
        try {
            StreamSource template = new StreamSource(url.openStream());
            Transformer transformer = factory.newTransformer(template);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            StreamSource inputSource = new StreamSource(toInputString(inputData));
            transformer.transform(inputSource, result);
            return writer.toString();
        } catch (IOException e) {
            throw new RendererException("I/O problem rendering " + url, e);
        } catch (TransformerConfigurationException e) {
            throw new RendererException("transformer configuration problem rendering " + url, e);
        } catch (TransformerException e) {
            throw new RendererException("transformer problem rendering " + url, e);
        } finally {
            new File(url.getFile()).delete();
        }
    }
    
    protected String toInputString(Map<String, Object> inputData) {
        StringBuilder builder = new StringBuilder("");
        if (inputData != null) {
            for (Map.Entry<String, Object> entry : inputData.entrySet()) {
                builder.append("<").append(entry.getKey()).append(">").
                    append(toString(entry.getValue())).
                append("</").append(entry.getKey()).append(">\n");
            }
        }
        return builder.toString();
    }
    
    @SuppressWarnings("unchecked")
    protected String toString(Object value) {
        StringBuilder builder = new StringBuilder();
        if (value instanceof String || value instanceof Number) {
            builder.append(value);
        } else if (value instanceof Map) {
            builder.append(toInputString((Map<String, Object>) value));
        } else if (value instanceof Collection) {
            for (Object obj : (Collection<?>) value) {
                builder.append(toString(obj));
            }
        } else {
            Map<String, Object> metaMap = new HashMap<String, Object>();
            for (Field field : value.getClass().getFields()) {
                try {
                    metaMap.put(field.getName(), PropertyUtils.getProperty(value, field.getName()));
                } catch (Exception e) {
                    metaMap.put(field.getName(), "");
                }
            }
            builder.append(toInputString(metaMap));
        }
        return builder.toString();
    }

}