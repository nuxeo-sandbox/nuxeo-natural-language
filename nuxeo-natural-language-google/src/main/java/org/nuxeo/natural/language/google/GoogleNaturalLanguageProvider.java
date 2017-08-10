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

package org.nuxeo.natural.language.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.AnnotateTextRequest;
import com.google.cloud.language.v1.AnnotateTextRequest.Features;
import com.google.cloud.language.v1.AnnotateTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.natural.language.service.api.NaturalLanguageEncoding;
import org.nuxeo.natural.language.service.api.NaturalLanguageFeature;
import org.nuxeo.natural.language.service.api.NaturalLanguageProvider;
import org.nuxeo.natural.language.service.api.NaturalLanguageResponse;

public class GoogleNaturalLanguageProvider implements NaturalLanguageProvider {

	public static final String APP_NAME_PARAM = "appName";

	public static final String API_KEY_PARAM = "apiKey";

	public static final String CREDENTIAL_PATH_PARAM = "credentialFilePath";

	protected Map<String, String> params;

	protected LanguageServiceClient languageServiceClient = null;

	public GoogleNaturalLanguageProvider(Map<String, String> parameters) {
		params = parameters;
	}

	protected LanguageServiceClient getLanguageServiceClient() throws IOException {

		if (languageServiceClient == null) {
			synchronized (this) {
				if (languageServiceClient == null) {

					if (usesServiceAccount()) {
						final LanguageServiceSettings languageServiceSettings;
						try (InputStream is = new FileInputStream(new File(getCredentialFilePath()))) {
							final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is)
									.createScoped(LanguageServiceSettings.getDefaultServiceScopes());

							final CredentialsProvider credentialsProvider = FixedCredentialsProvider
									.create(myCredentials);

							languageServiceSettings = LanguageServiceSettings.newBuilder()
									.setCredentialsProvider(credentialsProvider)
									.setTransportProvider(LanguageServiceSettings.defaultTransportProvider()).build();

							languageServiceClient = LanguageServiceClient.create(languageServiceSettings);

						} catch (IOException ioe) {
							throw new NuxeoException(ioe);
						}
					} else {
						try {
							languageServiceClient = LanguageServiceClient.create();
						} catch (IOException ioe) {
							throw new NuxeoException(ioe);
						}
					}
				}
			}
		}

		return languageServiceClient;
	}

	@Override
	public NaturalLanguageResponse processText(String text, List<NaturalLanguageFeature> features, NaturalLanguageEncoding encoding) throws IOException {

		LanguageServiceClient language = getLanguageServiceClient();

		AnnotateTextRequest request;
		AnnotateTextRequest.Builder requestBuilder = AnnotateTextRequest.newBuilder();

		Document.Builder docBuilder = Document.newBuilder();
		docBuilder.setContent(text).setType(Type.PLAIN_TEXT);
		requestBuilder.setDocument(docBuilder.build());
		if(encoding != null) {
			requestBuilder.setEncodingTypeValue(encoding.getNumber());
		}

		boolean doEntities = features.contains(NaturalLanguageFeature.ENTITIES);
		boolean doSentiment = features.contains(NaturalLanguageFeature.DOCUMENT_SENTIMENT);
		boolean doSyntax = features.contains(NaturalLanguageFeature.SYNTAX);
		Features requestFeatures = Features.newBuilder().setExtractDocumentSentiment(doSentiment)
				.setExtractEntities(doEntities).setExtractSyntax(doSyntax).build();

		requestBuilder.setFeatures(requestFeatures);

		request = requestBuilder.build();
		AnnotateTextResponse response;
		response = language.annotateText(request);

		GoogleNaturalLanguageResponse gnlr = new GoogleNaturalLanguageResponse(response);

		return gnlr;
	}

	@Override
	public List<NaturalLanguageFeature> getSupportedFeatures() {
		return null;
	}

	@Override
	public LanguageServiceClient getNativeClient() {
		try {
			return getLanguageServiceClient();
		} catch (IOException e) {
			throw new NuxeoException(e);
		}
	}

	protected String getCredentialFilePath() {
		return params.get(CREDENTIAL_PATH_PARAM);
	}

	protected String getApiKey() {
		return params.get(API_KEY_PARAM);
	}

	protected String getAppName() {
		return params.get(APP_NAME_PARAM);
	}

	protected boolean usesServiceAccount() {
		String path = getCredentialFilePath();
		return StringUtils.isNotEmpty(path);
	}

	protected boolean usesApiKey() {
		String key = getApiKey();
		return StringUtils.isNotEmpty(key);
	}

	/**
	 * Use by unit test
	 *
	 * @param value
	 * @since 9.2
	 */
	public void setCredentialFilePath(String value) {
		params.put(CREDENTIAL_PATH_PARAM, value);
	}

}
