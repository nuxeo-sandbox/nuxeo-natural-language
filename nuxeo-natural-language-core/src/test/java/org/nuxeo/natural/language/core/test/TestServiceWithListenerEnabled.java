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

import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.platform.mimetype.service.MimetypeRegistryService;
import org.nuxeo.natural.language.core.test.mock.MockNaturalLanguageResponse;
import org.nuxeo.natural.language.service.api.NaturalLanguage;
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
@LocalDeploy({ "nuxeo-natural-language:OSGI-INF/mock-provider-withAutoAnalyze-contrib.xml" })
public class TestServiceWithListenerEnabled {

	protected String TEXT_FILE = "status_quo.txt";

	@Inject
	CoreSession coreSession;

	@Inject
	MimetypeRegistryService mimetypeRegistryService;

	@Inject
	protected EventService eventService;

	@Inject
	protected NaturalLanguage naturalLanguage;

	protected DocumentModel createTestDocAndWaitForAsyncCompletion(String title) {

		File file = FileUtils.getResourceFileFromContext(TEXT_FILE);
		FileBlob fileBlob = new FileBlob(file);
		fileBlob.setFilename(file.getName());
		fileBlob.setMimeType("text/plain");

		DocumentModel doc = coreSession.createDocumentModel("/", title, "File");
		doc.setPropertyValue("dc:title", title);
		doc.setPropertyValue("file:content", fileBlob);
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
	public void testConfig() {
		// Check against the mock-provider-withAutoAnalyze-contrib.xml file

		// In this deployed config, the defaultAnalyze is set to true
		assertTrue(naturalLanguage.isDocumentListenerEnabled());

		// We don't have doc types in the mock xml config. service
		List<String> excludedDocTypes = naturalLanguage.getAnalyzeExcludedDocTypes();
		assertTrue(excludedDocTypes == null || excludedDocTypes.size() == 0);

		List<String> excludedFacets = naturalLanguage.getAnalyzeExcludedFacets();
		assertTrue(excludedFacets != null && excludedFacets.size() == 3);
		assertTrue(excludedFacets.indexOf("Picture") > -1);
		assertTrue(excludedFacets.indexOf("Video") > -1);
		assertTrue(excludedFacets.indexOf("Audio") > -1);

	}

	@Test
	public void testDocumentIsAnalyzed() throws JSONException {

		DocumentModel doc = createTestDocAndWaitForAsyncCompletion("test-doc");

		assertTrue(doc.hasFacet(NaturalLanguage.FACET_NAME));
		assertTrue(doc.hasSchema(NaturalLanguage.SCHEMA_NAME));
		String json = (String) doc.getPropertyValue(NaturalLanguage.XPATH_JSON);
		assertNotNull(json);

		JSONObject obj = new JSONObject(json);
		assertNotNull(obj);
		assertEquals(MockNaturalLanguageResponse.LANGUAGE, obj.get("language"));
		assertEquals(MockNaturalLanguageResponse.SCORE, (double) obj.get("score"), 0.0);
		assertEquals(MockNaturalLanguageResponse.MAGNITUDE, (double) obj.get("magnitude"), 0.0);

		// This document should not need to be analyzed again...
		boolean doAnalyze = naturalLanguage.canProcessDocument(doc);
		assertFalse(doAnalyze);
		// ... because the digests are the same
		Blob blob = (Blob) doc.getPropertyValue("file:content");
		assertEquals(blob.getDigest(), doc.getPropertyValue(NaturalLanguage.XPATH_SOURCE_DIGEST));
		// (but this has been tested in canProcessDocument()

	}

	@Test
	public void testEnableDisableListener() {

		assertTrue(naturalLanguage.isDocumentListenerEnabled());

		// Create a doc and check it has the facet/schema
		DocumentModel doc1 = createTestDocAndWaitForAsyncCompletion("test-doc-1");
		assertTrue(doc1.hasFacet(NaturalLanguage.FACET_NAME));
		assertTrue(doc1.hasSchema(NaturalLanguage.SCHEMA_NAME));

		// Disable the service then create another doc and test it does not have
		// the facet/schema
		naturalLanguage.setDocumentListenerEnabled(false);
		DocumentModel doc2 = createTestDocAndWaitForAsyncCompletion("test-doc-2");
		assertFalse(doc2.hasFacet(NaturalLanguage.FACET_NAME));
		assertFalse(doc2.hasSchema(NaturalLanguage.SCHEMA_NAME));

	}

}
