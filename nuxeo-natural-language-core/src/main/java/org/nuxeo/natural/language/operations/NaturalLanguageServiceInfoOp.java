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

import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.natural.language.service.api.NaturalLanguage;

/**
 * @since9.2
 */
@Operation(id = NaturalLanguageServiceInfoOp.ID, category = Constants.CAT_SERVICES, label = "Natural Language: Service Info", description = "Returns a JSON string containing the service configuration. Also rerurns the dynamic value canProcessDocument (checks agains excludedFacets, checks if has a blob, ...). Always false when the input is void.")
public class NaturalLanguageServiceInfoOp {

	public static final String ID = "Services.NaturalLanguageServiceInfoOp";

	@Context
	protected OperationContext ctx;

	@Context
	protected NaturalLanguage naturalLanguageService;

	@OperationMethod
	public String run() throws JSONException {

		JSONObject obj = naturalLanguageService.getServiceConfiguration(null);
		return obj.toString();
	}

	@OperationMethod
	public String run(DocumentModel doc) throws JSONException {

		JSONObject obj = naturalLanguageService.getServiceConfiguration(doc);
		return obj.toString();
	}

}
