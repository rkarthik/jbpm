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
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.jbpm.formbuilder.server.task.AssetDTO;
import org.jbpm.formbuilder.server.task.MetaDataDTO;
import org.jbpm.formbuilder.server.task.PackageDTO;
import org.jbpm.formbuilder.server.task.PackageListDTO;

public class GuvnorHelper {
    
    private final String baseUrl;
    private final String user;
    private final String password;
    
    public GuvnorHelper(String baseUrl, String user, String password) {
        this.baseUrl = baseUrl;
        this.user = user;
        this.password = password;
    }

    public String getPackageNameByContentUUID(String uuid) throws JAXBException, IOException {
        HttpClient client = new HttpClient();
        GetMethod call = new GetMethod(getRestBaseUrl());
        try {
            String auth = getAuth();
            call.addRequestHeader("Accept", "application/xml");
            call.addRequestHeader("Authorization", auth);
            client.executeMethod(call);
            PackageListDTO dto = jaxbTransformation(PackageListDTO.class, call.getResponseBodyAsStream(), 
                    PackageListDTO.class, PackageDTO.class, MetaDataDTO.class);
            for (PackageDTO pkg : dto.getPackage()) {
                if (uuid.equals(pkg.getMetadata().getUuid())) {
                    return pkg.getTitle();
                } 
            }
            for (PackageDTO pkg : dto.getPackage()) {
                for (String url : pkg.getAssets()) {
                    GetMethod subCall = new GetMethod(url);
                    try {
                        subCall.setRequestHeader("Authorization", auth);
                        subCall.setRequestHeader("Accept", "application/xml");
                        client.executeMethod(subCall);
                        AssetDTO subDto = jaxbTransformation(AssetDTO.class, subCall.getResponseBodyAsStream(), 
                                AssetDTO.class, MetaDataDTO.class);
                        if (subDto.getMetadata().getUuid().equals(uuid)) {
                            return pkg.getTitle();
                        }
                    } finally {
                        subCall.releaseConnection();
                    }
                }
            }
        } finally {
            call.releaseConnection();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T jaxbTransformation(Class<T> retType, InputStream input, Class<?>... boundTypes) throws JAXBException, IOException {
        String content = IOUtils.toString(input);
        StringReader reader = new StringReader(content);
        JAXBContext ctx = JAXBContext.newInstance(boundTypes);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(reader);
        return (T) obj;
    }
    
    public String getAuth() {
        String basic = this.user + ":" + this.password;
        basic = "BASIC " + Base64.encodeBase64(basic.getBytes());
        return basic;
    }
    
    public String getApiUrl(String pkgName) {
        return new StringBuilder(this.baseUrl).
            append("/org.drools.guvnor.Guvnor/api/package/").
            append(pkgName).append("/").toString();
    }

    public String getApiSearchUrl(String pkgName) {
        return new StringBuilder(this.baseUrl).
            append("/org.drools.guvnor.Guvnor/api/packages/").
            append(pkgName).append("/").toString();
    }

    public String getRestBaseUrl() {
        return new StringBuilder(this.baseUrl).append("/rest/packages/").toString();
    }

    public String getUser() {
        return this.user;
    }
    
}