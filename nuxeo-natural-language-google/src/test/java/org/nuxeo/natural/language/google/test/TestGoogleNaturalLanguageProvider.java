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
package org.nuxeo.natural.language.google.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.Blobs;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.platform.mimetype.service.MimetypeRegistryService;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.natural.language.google.GoogleNaturalLanguageProvider;
import org.nuxeo.natural.language.service.api.NaturalLanguage;
import org.nuxeo.natural.language.service.api.NaturalLanguageEncoding;
import org.nuxeo.natural.language.service.api.NaturalLanguageEntity;
import org.nuxeo.natural.language.service.api.NaturalLanguageFeature;
import org.nuxeo.natural.language.service.api.NaturalLanguageProvider;
import org.nuxeo.natural.language.service.api.NaturalLanguageResponse;
import org.nuxeo.natural.language.service.api.NaturalLanguageSentence;
import org.nuxeo.natural.language.service.api.NaturalLanguageToken;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.transaction.TransactionHelper;

import com.google.cloud.language.v1.LanguageServiceClient;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class, SimpleFeatureCustom.class })
@Deploy({ "nuxeo-natural-language-core", "nuxeo-natural-language-google" })
public class TestGoogleNaturalLanguageProvider {

	protected String PDF_EN = "files/DocumentManagement-EN-2017.pdf";

	protected String PDF_ES = "files/DocumentManagement-ES-2017.pdf";

	protected String PDF_JP = "files/DocumentManagement-JP-2017.pdf";

	protected String PDF_FR = "files/DocumentManagement-FR-2017.pdf";

	protected String WORD_EN = "files/GPU.docx";

	protected DocumentModel pdfEN;

	protected DocumentModel pdfES;

	protected DocumentModel pdfJP;

	protected DocumentModel pdfFR;

	protected DocumentModel wordEN;

	@Inject
	CoreSession coreSession;

	@Inject
	protected EventService eventService;

	@Inject
	protected ConversionService conversionService;

	@Inject
	MimetypeRegistryService mimetypeRegistryService;

	@Inject
	NaturalLanguage naturalLanguageService;

	protected GoogleNaturalLanguageProvider googleNaturalLanguageProvider = null;

	protected DocumentModel createFileDocument(DocumentModel parent, String filePath) {

		File file = FileUtils.getResourceFileFromContext(filePath);
		FileBlob fileBlob = new FileBlob(file);
		fileBlob.setFilename(file.getName());

		String mimeType = mimetypeRegistryService.getMimetypeFromBlob(fileBlob);
		// At runtime, importing a Word doc sets the correct mimetype. In
		// Eclipse, running the test in debug mode, it is set to
		// "Application/zip" (on Mac), and so, the conversion to text fails.
		if (file.getName().indexOf(".docx") > 0 && mimeType.indexOf("/zip") > 0) {
			mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		}
		fileBlob.setMimeType(mimeType);

		DocumentModel doc = coreSession.createDocumentModel(parent.getPathAsString(), file.getName(), "File");
		doc.setPropertyValue("dc:title", file.getName());
		doc.setPropertyValue("file:content", fileBlob);
		doc = coreSession.createDocument(doc);
		doc = coreSession.saveDocument(doc);

		return doc;
	}

	@Before
	public void setup() {

		Assume.assumeTrue("Credential are not set", areCredentialsSet());

		NaturalLanguageProvider languageProvider = naturalLanguageService.getProvider("google");

		assertNotNull(languageProvider);
		assertTrue(languageProvider instanceof GoogleNaturalLanguageProvider);

		googleNaturalLanguageProvider = (GoogleNaturalLanguageProvider) languageProvider;

		DocumentModel parent = coreSession.createDocumentModel("/", "test-nat-language", "Folder");
		parent.setPropertyValue("dc:title", "test-nat-language");
		parent = coreSession.createDocument(parent);
		parent = coreSession.saveDocument(parent);

		pdfEN = createFileDocument(parent, PDF_EN);
		pdfES = createFileDocument(parent, PDF_ES);
		pdfJP = createFileDocument(parent, PDF_JP);
		pdfFR = createFileDocument(parent, PDF_FR);
		wordEN = createFileDocument(parent, WORD_EN);

		coreSession.save();

		TransactionHelper.commitOrRollbackTransaction();
		TransactionHelper.startTransaction();

		eventService.waitForAsyncCompletion();

	}

	protected String extractRawText(DocumentModel doc) throws UnsupportedEncodingException, IOException {

		BlobHolder blobHolder = doc.getAdapter(BlobHolder.class);

		return extractRawText(blobHolder.getBlob());
	}

	protected String extractRawText(Blob blob) throws UnsupportedEncodingException, IOException {

		SimpleBlobHolder blobHolder = new SimpleBlobHolder(blob);
		BlobHolder resultBlob = conversionService.convert("any2text", blobHolder, null);
		String text = new String(resultBlob.getBlob().getByteArray(), "UTF-8");

		return text;
	}

	@Test
	public void testLanguageDetection() throws UnsupportedEncodingException, IOException {

		Assume.assumeTrue("Credential are not set", areCredentialsSet());

		String text;
		String language;
		NaturalLanguageResponse response;

		List<NaturalLanguageFeature> features = new ArrayList<NaturalLanguageFeature>();
		// features.add(NaturalLanguageFeature.ENTITIES);
		features.add(NaturalLanguageFeature.DOCUMENT_SENTIMENT);
		// features.add(NaturalLanguageFeature.SYNTAX);

		pdfEN.refresh();
		text = extractRawText(pdfEN);
		response = googleNaturalLanguageProvider.processText(text, features, null);
		assertNotNull(response);
		language = response.getLanguage();
		assertEquals("en", language);

		pdfES.refresh();
		text = extractRawText(pdfES);
		response = googleNaturalLanguageProvider.processText(text, features, null);
		assertNotNull(response);
		language = response.getLanguage();
		assertEquals("es", language);

		pdfJP.refresh();
		text = extractRawText(pdfJP);
		response = googleNaturalLanguageProvider.processText(text, features, null);
		assertNotNull(response);
		language = response.getLanguage();
		assertEquals("ja", language);

		pdfFR.refresh();
		text = extractRawText(pdfFR);
		response = googleNaturalLanguageProvider.processText(text, features, null);
		assertNotNull(response);
		language = response.getLanguage();
		assertEquals("fr", language);

	}

	@Test
	public void testtWordDocument() throws UnsupportedEncodingException, IOException {

		Assume.assumeTrue("Credential are not set", areCredentialsSet());

		String text;
		String language;
		NaturalLanguageResponse response;

		List<NaturalLanguageFeature> features = new ArrayList<NaturalLanguageFeature>();
		features.add(NaturalLanguageFeature.ENTITIES);
		features.add(NaturalLanguageFeature.DOCUMENT_SENTIMENT);
		features.add(NaturalLanguageFeature.SYNTAX);

		wordEN.refresh();
		text = extractRawText(wordEN);
		response = googleNaturalLanguageProvider.processText(text, features, NaturalLanguageEncoding.UTF8);
		assertNotNull(response);
		language = response.getLanguage();
		assertEquals("en", language);

		Float magnitude = response.getSentimentMagnitude();
		Float score = response.getSentimentScore();
		// Useless to test a value here
		assertNotNull(magnitude);
		assertNotNull(score);

		List<NaturalLanguageEntity> entities = response.getEntities();
		assertNotNull(entities);

		List<NaturalLanguageSentence> sentences = response.getSentences();
		assertNotNull(sentences);

		List<NaturalLanguageToken> tockens = response.getTokens();
		assertNotNull(tockens);

	}

	@Test
	public void testSyntax() throws UnsupportedEncodingException, IOException {

		List<NaturalLanguageFeature> features = new ArrayList<NaturalLanguageFeature>();
		features.add(NaturalLanguageFeature.SYNTAX);

		pdfEN.refresh();
		String text = extractRawText(pdfEN);
		NaturalLanguageResponse response = googleNaturalLanguageProvider.processText(text, features,
				NaturalLanguageEncoding.UTF8);
		assertNotNull(response);

		List<NaturalLanguageToken> tockens = response.getTokens();
		assertNotNull(tockens);

		// Just check the first 3 tokens, we know what we expect with this file
		// First words are "Document management system". Let's check we have
		// offsets (we passed the encoding => Google will give the offsets)
		assertTrue(tockens.size() > 1000);
		NaturalLanguageToken tocken = tockens.get(0);
		assertEquals("Document", tocken.getText());
		assertEquals(0, tocken.getBeginOffset());

		tocken = tockens.get(1);
		assertEquals("management", tocken.getText());
		assertEquals(9, tocken.getBeginOffset());

		tocken = tockens.get(2);
		assertEquals("system", tocken.getText());
		assertEquals(20, tocken.getBeginOffset());

	}

	@Test
	public void testProviderSentimentAndEntities() throws IOException, JSONException {

		Assume.assumeTrue("Credential are not set", areCredentialsSet());

		assertNotNull(googleNaturalLanguageProvider);

		File file = FileUtils.getResourceFileFromContext("files/status_quo.txt");
		Blob blob = Blobs.createBlob(file);
		String text = blob.getString();

		List<NaturalLanguageFeature> features = new ArrayList<NaturalLanguageFeature>();
		features.add(NaturalLanguageFeature.ENTITIES);
		features.add(NaturalLanguageFeature.DOCUMENT_SENTIMENT);
		// features.add(NaturalLanguageFeature.SYNTAX);
		NaturalLanguageResponse response = googleNaturalLanguageProvider.processText(text, features, null);

		assertNotNull(response);
		String language = response.getLanguage();
		assertEquals("en", language);

		Float magnitude = response.getSentimentMagnitude();
		assertNotNull(magnitude);

		Float score = response.getSentimentScore();
		assertNotNull(score);

		List<NaturalLanguageEntity> entities = response.getEntities();
		assertNotNull(entities);

		NaturalLanguageEntity entity;
		Map<String, String> entityMetadata;
		String metadataValue;

		// We check some know return values
		entity = NaturalLanguageEntity.findEntityForName(entities, "The Status Quo");
		assertNotNull(entity);
		// So, we found the "The Status Quo" entity, check other values:
		// -> Type
		assertEquals("OTHER", entity.getType());
		// -> Mentions
		// Must have several entries, the example text about Status
		// Quo should mention their original name as "The Spectres", and also
		// "Status Quo" (without "The")
		assertNotNull(entity.getMentions());
		assertTrue(entity.getMentions().indexOf("The Spectres") > -1);
		assertTrue(entity.getMentions().indexOf("Status Quo") > -1);
		// -> Metadata must have a wikipedia link
		entityMetadata = entity.getMetadata();
		assertNotNull(entityMetadata);
		metadataValue = entityMetadata.get("wikipedia_url");
		assertNotNull(metadataValue);

		// Now, check just the type for 2 other entities
		entity = NaturalLanguageEntity.findEntityForName(entities, "Rick Parfitt");
		assertNotNull(entity);
		assertEquals("PERSON", entity.getType());
		// One of the best song ever ;-)
		entity = NaturalLanguageEntity.findEntityForName(entities, "Whatever You Want");
		assertNotNull(entity);
		assertEquals("WORK_OF_ART", entity.getType());

		List<NaturalLanguageSentence> sentences = response.getSentences();
		assertNotNull(sentences);
		assertEquals(9, sentences.size());

		// We did not ask for tokens
		List<NaturalLanguageToken> tokens = response.getTokens();
		assertTrue("Tokes not requested, should be empty or null", tokens == null || tokens.size() == 0);

		// Test JSON representation
		JSONObject obj = response.toJSON();
		assertTrue(obj.get("language").equals("en"));

		JSONArray array = obj.getJSONArray("sentences");
		assertEquals(9, array.length());

		array = obj.getJSONArray("entities");
		assertTrue(array.length() > 0);

		array = obj.getJSONArray("tokens");
		assertEquals(0, array.length());

	}

	@Test
	public void testHasNativeClient() {

		Assume.assumeTrue("Credential are not set", areCredentialsSet());

		GoogleNaturalLanguageProvider gnl = (GoogleNaturalLanguageProvider) naturalLanguageService
				.getProvider("google");

		LanguageServiceClient client = gnl.getNativeClient();
		assertNotNull(client);

	}

	protected GoogleNaturalLanguageProvider getGoogleNaturalLanguageProvider() {
		if (googleNaturalLanguageProvider != null) {
			return googleNaturalLanguageProvider;
		}
		Map<String, String> params = new HashMap<>();
		params.put(GoogleNaturalLanguageProvider.APP_NAME_PARAM, "Nuxeo");
		params.put(GoogleNaturalLanguageProvider.CREDENTIAL_PATH_PARAM,
				System.getProperty(SimpleFeatureCustom.GOOGLE_CREDENTIALS_CONFIGURATION_PARAM));
		googleNaturalLanguageProvider = new GoogleNaturalLanguageProvider(params);

		return googleNaturalLanguageProvider;
	}

	protected boolean areCredentialsSet() {
		return StringUtils.isNotBlank(System.getProperty(SimpleFeatureCustom.GOOGLE_CREDENTIALS_CONFIGURATION_PARAM));
	}

}
