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
 *     Thibaud Arguillere
 */
package org.nuxeo.natural.language.core.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.natural.language.core.test.mock.MockNaturalLanguageProvider;
import org.nuxeo.natural.language.core.test.mock.MockNaturalLanguageResponse;
import org.nuxeo.natural.language.service.api.NaturalLanguage;
import org.nuxeo.natural.language.service.api.NaturalLanguageFeature;
import org.nuxeo.natural.language.service.api.NaturalLanguageProvider;
import org.nuxeo.natural.language.service.api.NaturalLanguageResponse;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;
import org.nuxeo.runtime.transaction.TransactionHelper;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features(AutomationFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy("nuxeo-natural-language-core")
@LocalDeploy({ "nuxeo-natural-language:OSGI-INF/mock-provider-contrib.xml" })
public class TestNaturalLanguageService {

	@Inject
	CoreSession coreSession;

	@Inject
	protected EventService eventService;

	@Inject
	protected NaturalLanguage naturalLanguage;

	@Test
	public void testConfiguration() throws JSONException {
		// Checking mock-provider-contrib.xml values

		assertFalse(naturalLanguage.isDocumentListenerEnabled());

		assertEquals(NaturalLanguage.DEFAULT_PROCESSING_CHAIN, naturalLanguage.getDefaultDocumentProcessingChainName());

		// We don't have doc types in the mock xml config. service
		List<String> excludedDocTypes = naturalLanguage.getAnalyzeExcludedDocTypes();
		assertTrue(excludedDocTypes == null || excludedDocTypes.size() == 0);

		List<String> excludedFacets = naturalLanguage.getAnalyzeExcludedFacets();
		assertTrue(excludedFacets != null && excludedFacets.size() == 3);
		assertTrue(excludedFacets.indexOf("Picture") > -1);
		assertTrue(excludedFacets.indexOf("Video") > -1);
		assertTrue(excludedFacets.indexOf("Audio") > -1);

		// Checking the JSON config
		JSONObject obj = naturalLanguage.getServiceConfiguration(null);
		assertNotNull(obj);
		assertFalse(obj.getBoolean("listenerEnabled"));
		assertEquals(NaturalLanguage.DEFAULT_PROCESSING_CHAIN, obj.getString("defaultProcessingChainName"));
		assertFalse(obj.getBoolean("canProcessDocument"));

	}

	@Test
	public void testNamedProvider() throws IllegalStateException, IOException, GeneralSecurityException {

		NaturalLanguageProvider provider = naturalLanguage.getProvider(MockNaturalLanguageProvider.NAME);
		assertNotNull(provider);
		assertTrue(provider instanceof MockNaturalLanguageProvider);

		NaturalLanguageResponse response = naturalLanguage.processText(MockNaturalLanguageProvider.NAME, "dummy text",
				Arrays.asList(NaturalLanguageFeature.DOCUMENT_SENTIMENT), null);
		assertNotNull(response);
		assertEquals(MockNaturalLanguageResponse.LANGUAGE, response.getLanguage());
	}

	@Test
	public void testDefaultProvider() throws IllegalStateException, IOException, GeneralSecurityException {

		String name = naturalLanguage.getDefaultProviderName();
		assertEquals(MockNaturalLanguageProvider.NAME, name);

		NaturalLanguageResponse response = naturalLanguage.processText(null, "dummy text",
				Arrays.asList(NaturalLanguageFeature.DOCUMENT_SENTIMENT), null);
		assertNotNull(response);
		assertEquals(MockNaturalLanguageResponse.LANGUAGE, response.getLanguage());

	}

	@Test
	public void tesGetProviders() {
		Map<String, NaturalLanguageProvider> providers = naturalLanguage.getProviders();
		assertNotNull(providers);
		assertEquals(1, providers.size());
		NaturalLanguageProvider provider = providers.get(MockNaturalLanguageProvider.NAME);
		assertNotNull(provider);
		assertTrue(provider instanceof MockNaturalLanguageProvider);

	}

	protected DocumentModel createTestDocAndWaitForAsyncCompletion() {

		DocumentModel doc = coreSession.createDocumentModel("/", "test-doc", "File");
		doc.setPropertyValue("dc:title", "test-doc");
		doc = coreSession.createDocument(doc);

		coreSession.save();
		TransactionHelper.commitOrRollbackTransaction();
		TransactionHelper.startTransaction();

		eventService.waitForAsyncCompletion();

		// ========================================
		// The listener is asynchronous => need to refresh our instance
		// ========================================
		doc.refresh();

		return doc;
	}

	@Test
	public void testFacetAndSchema() {

		DocumentModel doc = createTestDocAndWaitForAsyncCompletion();

		assertFalse(doc.hasFacet(NaturalLanguage.FACET_NAME));
		boolean ok = doc.addFacet(NaturalLanguage.FACET_NAME);
		assertTrue(ok);

		assertTrue(doc.hasFacet(NaturalLanguage.FACET_NAME));
		assertTrue(doc.hasSchema(NaturalLanguage.SCHEMA_NAME));

	}

}
