/*
 * (C) Copyright 2017 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     Thibaud Arguillere
 */
package org.nuxeo.natural.language.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.PostCommitEventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.core.event.impl.EventContextImpl;
import org.nuxeo.natural.language.service.api.NaturalLanguage;
import org.nuxeo.runtime.api.Framework;

/**
 *
 * @since 9.2
 */
public class NaturalLanguageListener implements PostCommitEventListener {

	private static final Log log = LogFactory.getLog(NaturalLanguageListener.class);

	@Override
	public void handleEvent(EventBundle events) {
		for (Event event : events) {
			handleEvent(event);
		}
	}

	protected void handleEvent(Event event) {

		NaturalLanguage naturalLanguageService = Framework.getService(NaturalLanguage.class);
		if (naturalLanguageService == null || !naturalLanguageService.isAutoAnalyze()) {
			return;
		}

		EventContext ectx = event.getContext();
		if (!(ectx instanceof DocumentEventContext)) {
			return;
		}
		DocumentModel doc = ((DocumentEventContext) ectx).getSourceDocument();

		if (naturalLanguageService.canProcessDocument(doc)) {
			String mapperChainName = naturalLanguageService.getDefaultDocumentProcessingChainName();

			CoreSession session = doc.getCoreSession();
			AutomationService as = Framework.getService(AutomationService.class);
			OperationContext octx = new OperationContext();
			octx.setInput(doc);
			octx.setCoreSession(session);
			OperationChain chain = new OperationChain("NaturalLanguageListenerChain");
			chain.add(mapperChainName);

			try {
				as.run(octx, chain);

				EventContextImpl evctx = new DocumentEventContext(session, session.getPrincipal(), doc);

				Event eventToSend = evctx.newEvent(NaturalLanguage.EVENT_DOCUMENT_HANDLED);
				EventService eventService = Framework.getLocalService(EventService.class);
				eventService.fireEvent(eventToSend);

			} catch (OperationException e) {
				log.warn("Error running the chain <" + mapperChainName + ">", e);
			}
		}

	}
}
