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
package org.jbpm.formbuilder.shared.api;

import java.util.HashMap;
import java.util.Map;

public class RepresentationFactory {

    private static final Map<String, String> MAPPING = new HashMap<String, String>();
    
    public static void registerItemClassName(String repClassName, String itemClassName) {
        MAPPING.put(repClassName, itemClassName);
    }
    
    public static String getItemClassName(String repClassName) {
        return MAPPING.get(repClassName);
    }
}