/*
 * (C) Copyright 2015-2017 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Michael Vachette (via nuxeo-vision)
 *     Thibaud Arguillere
 */
package org.nuxeo.natural.language.service.impl;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject("configuration")
public class NaturalLanguageDescriptor {

    @XNode("performNaturalLanguageChainName")
    protected String naturalLanguageMapperChainName = "javascript.NaturalLanguageDefaultMapper";

    @XNode("defaultProviderName")
    protected String defaultProviderName;

    public String getMapperChainName() {
        return naturalLanguageMapperChainName;
    }

    public String getDefaultProviderName() {
        return defaultProviderName;
    }
}
