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
package org.nuxeo.natural.language.service.api;

import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * A service that performs Natural Language tasks like text analysis, sentiment
 * extraction, ...
 *
 * @since 9.2
 */
public interface NaturalLanguage {

	public static final String DEFAULT_PROVIDER_NAME = "google";

	public static final String DEFAULT_PROCESSING_CHAIN = "javascript.NaturalLanguageDefaultDocumentProcessing";

	public static final String SCHEMA_NAME = "NaturalLanguage";

	public static final String FACET_NAME = "NaturalLanguage";

	public static final String XPATH_JSON = "natural_language:json";

	public static final String XPATH_SOURCE_DIGEST = "natural_language:source_digest";

	/**
	 * Send the text to the provider.
	 *
	 * The NuxeoException should encapsulate any exception:
	 * IO/GeneralSecurity/etc. but also provider-specific errors such a an
	 * unsupported language (i.e. an error when the string is written in a
	 * language not supported by the provider, like latin for Google)
	 *
	 * @param provider
	 *            Provider to use. Can be {@code null} (using default provider
	 *            then)
	 * @param text
	 *            Text to analyze
	 * @param features
	 *            Feature to request from the service
	 * @param encoding
	 *            Encoding to use. Can be {@code null}
	 * @return a {@link NaturalLanguageResponse} object
	 * @throws NuxeoException
	 */
	NaturalLanguageResponse processText(String providerName, String text, List<NaturalLanguageFeature> features,
			NaturalLanguageEncoding encoding) throws NuxeoException;

	/**
	 * Extract the text form the blob and send it to the provider.
	 *
	 * The NuxeoException should encapsulate any exception:
	 * IO/GeneralSecurity/etc. but also provider-specific errors such a an
	 * unsupported language (i.e. an error when the string is written in a
	 * language not supported by the provider, like latin for Google)
	 *
	 * @param provider
	 *            Provider to use. Can be {@code null} (using default provider
	 *            then)
	 * @param blob
	 *            Raw text will be extracted from this blob
	 * @param features
	 *            Features to request from the service
	 * @return a {@link NaturalLanguageResponse} object
	 * @throws NuxeoException
	 */
	NaturalLanguageResponse processBlob(String provider, Blob blob, List<NaturalLanguageFeature> features)
			throws NuxeoException;

	/**
	 * Extract the text from the blob at xpath and send it to the provider.
	 *
	 * The NuxeoException should encapsulate any exception:
	 * IO/GeneralSecurity/etc. but also provider-specific errors such a an
	 * unsupported language (i.e. an error when the string is written in a
	 * language not supported by the provider, like latin for Google)
	 *
	 * @param provider
	 *            Provider to use. Can be {@code null} (using default provider
	 *            then)
	 * @param doc
	 *            The document containing the blob to analyze
	 * @param xpath
	 *            The xpath where to find the blob (null or empty: uses
	 *            file:content)
	 * @param features
	 *            Features to request from the service
	 * @return a {@link NaturalLanguageResponse} object
	 * @throws NuxeoException
	 */
	NaturalLanguageResponse processDocument(String providerName, DocumentModel doc, String xpath,
			List<NaturalLanguageFeature> features) throws NuxeoException;

	/**
	 * @return The name of default provider or {@code null} is not found
	 */
	String getDefaultProviderName();

	/**
	 * @param name
	 *            The name of the provider to return
	 * @return The provider object or {@code null} is not found
	 */
	NaturalLanguageProvider getProvider(String name);

	/**
	 * @return all registered providers
	 */
	Map<String, NaturalLanguageProvider> getProviders();

	/**
	 *
	 * @return true if documents must be analyze in a listener automatically
	 */
	boolean isAutoAnalyze();

	/**
	 *
	 * @return the automation chain used to process documents when isAutoAnalyze
	 *         is true
	 */
	String getDefaultDocumentProcessingChainName();


	public String[] getAutoAnalyzeExcludedFacets();

	public String[] getAutoAnalyzeExcludedDocTypes();

}
