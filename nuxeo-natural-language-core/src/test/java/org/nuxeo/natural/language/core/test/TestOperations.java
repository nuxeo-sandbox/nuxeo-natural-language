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
import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationChain;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.natural.language.core.test.mock.MockNaturalLanguageResponse;
import org.nuxeo.natural.language.operations.NaturalLanguageOnBlobOp;
import org.nuxeo.natural.language.operations.NaturalLanguageOnDocumentOp;
import org.nuxeo.natural.language.operations.NaturalLanguageOnStringOp;
import org.nuxeo.natural.language.operations.NaturalLanguageServiceInfoOp;
import org.nuxeo.natural.language.service.api.NaturalLanguage;
import org.nuxeo.natural.language.service.api.NaturalLanguageFeature;
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
public class TestOperations {

	protected String TEXT_FILE = "status_quo.txt";

	@Inject
	CoreSession coreSession;

	@Inject
	protected EventService eventService;

	@Inject
	AutomationService automationService;

	@Inject
	protected NaturalLanguage naturalLanguage;

	protected Blob createTestFileBlob() {

		File file = FileUtils.getResourceFileFromContext(TEXT_FILE);
		FileBlob fileBlob = new FileBlob(file);
		fileBlob.setFilename(file.getName());
		fileBlob.setMimeType("text/plain");

		return fileBlob;
	}

	protected DocumentModel createTestDocAndWaitForAsyncCompletion(String title) {

		Blob blob = createTestFileBlob();

		DocumentModel doc = coreSession.createDocumentModel("/", title, "File");
		doc.setPropertyValue("dc:title", title);
		doc.setPropertyValue("file:content", (Serializable) blob);
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
	public void testServiceInfoOperation() throws OperationException, JSONException {

		DocumentModel doc = createTestDocAndWaitForAsyncCompletion("testdoc1");

		OperationContext ctx = new OperationContext(coreSession);
		assertNotNull(ctx);

		ctx.setInput(doc);
		ctx.setCoreSession(coreSession);
		OperationChain chain = new OperationChain("testServiceInfoOperation");
		chain.add(NaturalLanguageServiceInfoOp.ID);

		String result = (String) automationService.run(ctx, chain);
		assertNotNull(result);
		// Check it's a valid JSON string
		JSONObject obj = new JSONObject(result);
		assertNotNull(obj);
		assertFalse(obj.getBoolean("listenerEnabled"));
		assertEquals(NaturalLanguage.DEFAULT_PROCESSING_CHAIN, obj.getString("defaultProcessingChainName"));
		// The input is a document that we can process
		assertTrue(obj.getBoolean("canProcessDocument"));

		// With void input, canProcessDocument is always set to false
		ctx.setInput(null);
		result = (String) automationService.run(ctx, chain);
		obj = new JSONObject(result);
		assertFalse(obj.getBoolean("canProcessDocument"));

	}

	@Test
	public void testProcessStringOperation() throws OperationException, JSONException {

		OperationContext ctx = new OperationContext(coreSession);
		assertNotNull(ctx);

		String inputStr = "whatever, we are using the mock provider";
		ctx.setInput(inputStr);
		ctx.setCoreSession(coreSession);
		OperationChain chain = new OperationChain("testProcessStringOperation");
		String featuresStr = Arrays.asList(NaturalLanguageFeature.values()).toString();
		featuresStr = StringUtils.remove(featuresStr, '[');
		featuresStr = StringUtils.remove(featuresStr, ']');
		chain.add(NaturalLanguageOnStringOp.ID).set("features", featuresStr).set("outputVariable", "result");

		// This operation returns the string unchanged
		String result = (String) automationService.run(ctx, chain);
		assertEquals(result, inputStr);

		// (Result as returned by the mock provider)
		NaturalLanguageResponse response = (NaturalLanguageResponse) ctx.get("result");
		assertNotNull(response);
		assertEquals(MockNaturalLanguageResponse.LANGUAGE, response.getLanguage());

	}

	@Test
	public void testProcessBlobOperation() throws OperationException, JSONException {

		OperationContext ctx = new OperationContext(coreSession);

		Blob blob = createTestFileBlob();
		ctx.setInput(blob);
		ctx.setCoreSession(coreSession);
		OperationChain chain = new OperationChain("testProcessBlobOperation");
		String featuresStr = Arrays.asList(NaturalLanguageFeature.values()).toString();
		featuresStr = StringUtils.remove(featuresStr, '[');
		featuresStr = StringUtils.remove(featuresStr, ']');
		chain.add(NaturalLanguageOnBlobOp.ID).set("features", featuresStr).set("outputVariable", "result");

		// This operation returns the blob unchanged
		Blob result = (Blob) automationService.run(ctx, chain);
		assertEquals(blob.getDigest(), result.getDigest());
		assertEquals(blob.getFilename(), result.getFilename());
		assertEquals(blob.getMimeType(), result.getMimeType());
		assertEquals(blob.getLength(), result.getLength());

		// (Result as returned by the mock provider)
		NaturalLanguageResponse response = (NaturalLanguageResponse) ctx.get("result");
		assertNotNull(response);
		assertEquals(MockNaturalLanguageResponse.LANGUAGE, response.getLanguage());

	}

	@Test
	public void testProcessDocumentOperation() throws OperationException, JSONException {

		OperationContext ctx = new OperationContext(coreSession);

		DocumentModel doc = createTestDocAndWaitForAsyncCompletion("testdoc1");
		ctx.setInput(doc);
		ctx.setCoreSession(coreSession);
		OperationChain chain = new OperationChain("testProcessDocumentOperation");
		String featuresStr = Arrays.asList(NaturalLanguageFeature.values()).toString();
		featuresStr = StringUtils.remove(featuresStr, '[');
		featuresStr = StringUtils.remove(featuresStr, ']');
		chain.add(NaturalLanguageOnDocumentOp.ID).set("features", featuresStr).set("outputVariable", "result");

		// This operation returns the document unchanged
		DocumentModel result = (DocumentModel) automationService.run(ctx, chain);
		assertEquals(doc.getChangeToken(), result.getChangeToken());

		// (Result as returned by the mock provider)
		NaturalLanguageResponse response = (NaturalLanguageResponse) ctx.get("result");
		assertNotNull(response);
		assertEquals(MockNaturalLanguageResponse.LANGUAGE, response.getLanguage());

	}
}
