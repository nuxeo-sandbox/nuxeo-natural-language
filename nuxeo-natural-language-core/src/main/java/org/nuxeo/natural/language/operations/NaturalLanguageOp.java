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

import java.io.IOException;
import java.security.GeneralSecurityException;
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
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.natural.language.service.api.NaturalLanguage;
import org.nuxeo.natural.language.service.api.NaturalLanguageEncoding;
import org.nuxeo.natural.language.service.api.NaturalLanguageFeature;
import org.nuxeo.natural.language.service.api.NaturalLanguageResponse;

/**
 * WHEN CALLED WITH STRING, return the result directly
 * When called with BLOB input, return the blob(s) unchanged, result values are put in the outputVariable context variable
 */
@Operation(id = NaturalLanguageOp.ID, category = Constants.CAT_SERVICES, label = "Call the Natural Language Service", description = "Calls the Computer Natural Language Service for the input blobs or String. Returns the input unchanged. Whe using a text as input, encoding is optional and if passed, must be UTF8, UTF16 or UTF32")
public class NaturalLanguageOp {

    public static final String ID = "Services.NaturalLanguageOp";

    private static final Log log = LogFactory.getLog(NaturalLanguageOp.class);

    @Context
    protected OperationContext ctx;

    @Context
    protected NaturalLanguage naturalLanguageService;

    @Param(name = "provider", description = "The Natural Language provider name (if empty, will use thedefault provider", required = false)
    protected String provider;

    @Param(name = "features", description = "A StringList of features to request from the API", required = true)
    protected StringList features;

    @Param(name = "encoding", description = "The encoding when using a sting as input", required = false)
    protected String encoding;

    @Param(name = "outputVariable", description = "The key of the context output variable. "
            + "The output variable is the NaturalLanguageResponse object. ", required = true)
    protected String outputVariable;

    @OperationMethod
    public Blob run(Blob blob) {

        NaturalLanguageResponse response;

        // Convert feature string to enum
        List<NaturalLanguageFeature> featureList = new ArrayList<>();
        for (String feature : features) {
            featureList.add(NaturalLanguageFeature.valueOf(feature));
        }

        try {
        	response = naturalLanguageService.processBlob(provider, blob, featureList);
            ctx.put(outputVariable, response);
        } catch (IOException | GeneralSecurityException e) {
            if (StringUtils.isEmpty(provider)) {
                log.warn("Call to the Natural Language API failed for the default provider " + provider, e);
            } else {
                log.warn("Call to the Natural Language API failed for provider " + provider, e);
            }
        }
        return blob;
    }


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
        	if(StringUtils.isEmpty(encoding)) {
        		nlEncoding = null;
        	} else {
        		nlEncoding = NaturalLanguageEncoding.fromString(encoding);
        	}
        	response = naturalLanguageService.processText(provider, text, featureList, nlEncoding);
            ctx.put(outputVariable, response);
        } catch (IOException | GeneralSecurityException e) {
            if (StringUtils.isEmpty(provider)) {
                log.warn("Call to the Natural Language API failed for the default provider " + provider, e);
            } else {
                log.warn("Call to the Natural Language API failed for provider " + provider, e);
            }
        }
        return text;
    }

}
