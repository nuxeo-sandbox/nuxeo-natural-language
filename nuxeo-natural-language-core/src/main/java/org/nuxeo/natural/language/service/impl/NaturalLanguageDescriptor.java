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

import org.apache.commons.lang.StringUtils;
import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;
import org.nuxeo.natural.language.service.api.NaturalLanguage;

@XObject("configuration")
public class NaturalLanguageDescriptor {

	@XNode("defaultProviderName")
	protected String defaultProviderName;

	@XNode("defaultDocumentProcessingChainName")
	protected String defaultDocumentProcessingChainName = NaturalLanguage.DEFAULT_PROCESSING_CHAIN;

	@XNode("autoAnalyze")
	protected Boolean autoAnalyze = false;

	@XNodeList(value = "doNotAutoAnalyze/facet", type = String[].class, componentType = String.class)
    protected String[] autoAnalyzeExcludedFacets;

	@XNodeList(value = "doNotAutoAnalyze/type", type = String[].class, componentType = String.class)
    protected String[] autoAnalyzeExcludedDocTypes;

	public String getDefaultProviderName() {
		if (StringUtils.isBlank(defaultProviderName)) {
			return NaturalLanguage.DEFAULT_PROVIDER_NAME;
		}
		return defaultProviderName;
	}

	public String getDefaultDocumentProcessingChainName() {
		return defaultDocumentProcessingChainName;
	}

	public boolean isAutoAnalyze() {
		return autoAnalyze;
	}

	public String[] getAutoAnalyzeExcludedFacets() {
        return autoAnalyzeExcludedFacets;
    }

	public String[] getAutoAnalyzeExcludedDocTypes() {
        return autoAnalyzeExcludedDocTypes;
    }
}
