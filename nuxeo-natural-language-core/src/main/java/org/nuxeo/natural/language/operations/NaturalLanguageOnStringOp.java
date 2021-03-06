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
package org.nuxeo.natural.language.operations;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.automation.core.util.StringList;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.natural.language.service.api.NaturalLanguage;
import org.nuxeo.natural.language.service.api.NaturalLanguageEncoding;
import org.nuxeo.natural.language.service.api.NaturalLanguageFeature;
import org.nuxeo.natural.language.service.api.NaturalLanguageResponse;

/**
 * @since9.2
 */
@Operation(id = NaturalLanguageOnStringOp.ID, category = Constants.CAT_SERVICES, label = "Natural Language: Process String", description = "Calls the Computer Natural Language Service for the input string, returns the input unchanged. encoding parameter is optional and if passed, must be UTF8, UTF16 or UTF32. Put the NaturalLanguageResponse result in the output context variable")
public class NaturalLanguageOnStringOp {

	public static final String ID = "Services.NaturalLanguageOnStringOp";

	private static final Log log = LogFactory.getLog(NaturalLanguageOnStringOp.class);

	@Context
	protected OperationContext ctx;

	@Context
	protected NaturalLanguage naturalLanguageService;

	@Param(name = "provider", description = "The Natural Language provider name (if empty, will use thedefault provider", required = false)
	protected String provider;

	@Param(name = "features", description = "A StringList of features to request from the API", required = true)
	protected StringList features;

	@Param(name = "encoding", description = "The encoding of the input. UTF8, UTF16 or UTF32 (or not passed)", required = false)
	protected String encoding;

	@Param(name = "outputVariable", description = "The key of the context output variable. "
			+ "The output variable is the NaturalLanguageResponse object. ", required = true)
	protected String outputVariable;

	@OperationMethod
	public String run(String text) {
		NaturalLanguageResponse response = null;

		// Convert feature string to enum
		List<NaturalLanguageFeature> featureList = new ArrayList<>();
		for (String feature : features) {
			featureList.add(NaturalLanguageFeature.valueOf(feature));
		}

		try {

			NaturalLanguageEncoding nlEncoding;
			if (StringUtils.isEmpty(encoding)) {
				nlEncoding = null;
			} else {
				nlEncoding = NaturalLanguageEncoding.fromString(encoding);
			}
			response = naturalLanguageService.processText(provider, text, featureList, nlEncoding);
			ctx.put(outputVariable, response);

		} catch (NuxeoException e) {
			if (StringUtils.isEmpty(provider)) {
				log.error("Call to the Natural Language API failed for the default provider:\n" + e.getMessage());
			} else {
				log.error("Call to the Natural Language API failed for provider " + provider + ":\n" + e.getMessage());
			}
			throw new NuxeoException("Call to the Natural Language API failed: ", e);
		} catch (Exception e) {
			// Still catching Exception to make sure we handle even new kind or
			// unexpected error triggered by providers
			if (StringUtils.isEmpty(provider)) {
				log.error("Call to the Natural Language API failed for the default provider:\n" + e.getMessage());
			} else {
				log.error("Call to the Natural Language API failed for provider " + provider + ":\n" + e.getMessage());
			}
			throw new NuxeoException(e);
		}

		return text;
	}

}
