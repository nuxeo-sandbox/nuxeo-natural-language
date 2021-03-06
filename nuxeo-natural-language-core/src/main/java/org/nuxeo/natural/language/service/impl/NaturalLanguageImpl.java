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
 *     Thibaud ARguillere
 */
package org.nuxeo.natural.language.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.convert.api.ConversionException;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.natural.language.service.api.NaturalLanguage;
import org.nuxeo.natural.language.service.api.NaturalLanguageEncoding;
import org.nuxeo.natural.language.service.api.NaturalLanguageFeature;
import org.nuxeo.natural.language.service.api.NaturalLanguageProvider;
import org.nuxeo.natural.language.service.api.NaturalLanguageResponse;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * Implementation of the NaturalLanguage service
 *
 * @since 9.2
 */
public class NaturalLanguageImpl extends DefaultComponent implements NaturalLanguage {

	private static final Log log = LogFactory.getLog(NaturalLanguageImpl.class);

	protected static final String CONFIG_EXT_POINT = "configuration";

	protected static final String PROVIDER_EXT_POINT = "provider";

	protected NaturalLanguageDescriptor config = null;

	protected Map<String, NaturalLanguageProvider> providers = new HashMap<>();

	protected Boolean documentListenerEnabled = null;

	@Override
	public JSONObject getServiceConfiguration(DocumentModel doc) {

		JSONObject obj = new JSONObject();

		try {

			obj.put("listenerEnabled", isDocumentListenerEnabled());
			obj.put("defaultProcessingChainName", getDefaultDocumentProcessingChainName());
			obj.put("excludedFacets", getAnalyzeExcludedFacets());
			obj.put("excludedDocTypes", getAnalyzeExcludedDocTypes());
			obj.put("canProcessDocument", doc == null ? false : canProcessDocument(doc));

		} catch (JSONException e) {
			// Ignore the error
		}

		return obj;
	}

	/**
	 * Component activated notification. Called when the component is activated.
	 * All component dependencies are resolved at that moment. Use this method
	 * to initialize the component.
	 *
	 * @param context
	 *            the component context.
	 */
	@Override
	public void activate(ComponentContext context) {
		super.activate(context);
	}

	/**
	 * Component deactivated notification. Called before a component is
	 * unregistered. Use this method to do cleanup if any and free any resources
	 * held by the component.
	 *
	 * @param context
	 *            the component context.
	 */
	@Override
	public void deactivate(ComponentContext context) {
		super.deactivate(context);
	}

	/**
	 * Application started notification. Called after the application started.
	 * You can do here any initialization that requires a working application
	 * (all resolved bundles and components are active at that moment)
	 *
	 * @param context
	 *            the component context. Use it to get the current bundle
	 *            context
	 * @throws Exception
	 */
	@Override
	public void applicationStarted(ComponentContext context) {
	}

	@Override
	public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
		if (CONFIG_EXT_POINT.equals(extensionPoint)) {
			config = (NaturalLanguageDescriptor) contribution;
		} else if (PROVIDER_EXT_POINT.equals(extensionPoint)) {
			NaturalLanguageProviderDescriptor desc = (NaturalLanguageProviderDescriptor) contribution;
			try {
				NaturalLanguageProvider provider = (NaturalLanguageProvider) desc.getKlass().getConstructor(Map.class)
						.newInstance(desc.getParameters());
				providers.put(desc.getProviderName(), provider);
			} catch (ReflectiveOperationException e) {
				throw new NuxeoException(e);
			}
		}
	}

	@Override
	public NaturalLanguageResponse processText(String providerName, String text, List<NaturalLanguageFeature> features,
			NaturalLanguageEncoding encoding) throws NuxeoException {

		if (text == null) {
			throw new IllegalArgumentException("Input text cannot be null");
		} else if (features == null || features.size() == 0) {
			throw new IllegalArgumentException("The feature list cannot be empty or null");
		}

		NaturalLanguageProvider provider = getProviderOrDefault(providerName);
		if (provider == null) {
			throw new NuxeoException("Unknown provider: "
					+ (StringUtils.isBlank(providerName) ? getDefaultProviderName() : providerName));
		}

		return provider.processText(text, features, encoding);
	}

	protected String extractRawText(Blob blob) {

		try {
			SimpleBlobHolder blobHolder = new SimpleBlobHolder(blob);
			ConversionService conversionService = Framework.getLocalService(ConversionService.class);
			BlobHolder resultBlob = conversionService.convert("any2text", blobHolder, null);
			String text;
			text = new String(resultBlob.getBlob().getByteArray(), "UTF-8");

			return text;
		} catch (ConversionException | IOException e) {
			throw new NuxeoException(e);
		}
	}

	@Override
	public NaturalLanguageResponse processBlob(String providerName, Blob blob, List<NaturalLanguageFeature> features)
			throws NuxeoException {
		if (blob == null) {
			throw new IllegalArgumentException("Input Blob cannot be null");
		} else if (features == null || features.size() == 0) {
			throw new IllegalArgumentException("The feature list cannot be empty or null");
		}

		NaturalLanguageProvider provider = getProviderOrDefault(providerName);
		if (provider == null) {
			throw new NuxeoException("Unknown provider: "
					+ (StringUtils.isBlank(providerName) ? getDefaultProviderName() : providerName));
		}

		String text = extractRawText(blob);
		return provider.processText(text, features, NaturalLanguageEncoding.UTF8);
	}

	@Override
	public NaturalLanguageResponse processDocument(String providerName, DocumentModel doc, String xpath,
			List<NaturalLanguageFeature> features) throws NuxeoException {

		if (doc == null) {
			throw new IllegalArgumentException("Input DocumentModel cannot be null");
		}

		Blob blob;
		if (StringUtils.isBlank(xpath)) {
			xpath = "file:content";
		}
		blob = (Blob) doc.getPropertyValue(xpath);
		return processBlob(providerName, blob, features);

	}

	@Override
	public String getDefaultProviderName() {
		return config.getDefaultProviderName();
	}

	@Override
	public NaturalLanguageProvider getProvider(String name) {
		return providers.get(name);
	}

	/*
	 * Utility to handle a null provider name
	 */
	protected NaturalLanguageProvider getProviderOrDefault(String name) {
		if (StringUtils.isBlank(name)) {
			name = getDefaultProviderName();
		}

		return getProvider(name);
	}

	@Override
	public Map<String, NaturalLanguageProvider> getProviders() {
		return providers;
	}

	@Override
	public boolean isDocumentListenerEnabled() {
		if (documentListenerEnabled == null) {
			documentListenerEnabled = config.isDocumentListenerEnabled();
		}
		return documentListenerEnabled;
	}

	@Override
	public void setDocumentListenerEnabled(boolean value) {
		documentListenerEnabled = value;
	}

	@Override
	public String getDefaultDocumentProcessingChainName() {
		return config.getDefaultDocumentProcessingChainName();
	}

	@Override
	public ArrayList<String> getAnalyzeExcludedFacets() {
		return config.getAnalyzeExcludedFacets();
	}

	@Override
	public ArrayList<String> getAnalyzeExcludedDocTypes() {
		return config.getAnalyzeExcludedDocTypes();
	}

	@Override
	public boolean canProcessDocument(DocumentModel doc) {

		boolean hasNLFacet = doc.hasFacet(NaturalLanguage.FACET_NAME);

		// Does the doc has a facet telling us it must be ignored?
		List<String> excludedFacets = getAnalyzeExcludedFacets();
		if (excludedFacets != null && excludedFacets.size() > 0) {
			Set<String> docFacets = doc.getFacets();
			for (String facet : excludedFacets) {
				if (docFacets.contains(facet)) {
					// Handling facet dynamically added. Maybe the doc did not
					// originally have the facet, Natural Language was processed
					// => caller should remove it
					if (hasNLFacet) {
						return true;
					}
					return false;
				}
			}
		}

		// Does the doc has a type telling us it must be ignored?
		List<String> excludedDocTypes = getAnalyzeExcludedDocTypes();
		if (excludedDocTypes != null && excludedDocTypes.size() > 0) {
			if (excludedDocTypes.indexOf(doc.getType()) > -1) {
				return false;
			}
		}

		// Do we have blob? yes/No, do we already have the facet?
		Blob blob = null;
		if (doc.hasSchema("file")) {
			blob = (Blob) doc.getPropertyValue("file:content");
		}
		if (blob == null && !hasNLFacet) {
			return false;
		}

		// So we have a blob, let's check if it was already processed
		if (blob != null && hasNLFacet) {
			String sourceDigest = (String) doc.getPropertyValue(NaturalLanguage.XPATH_SOURCE_DIGEST);
			if (sourceDigest != null && blob.getDigest().equals(sourceDigest)) {
				return false;
			}
		}

		// Ok, looks like we _really_ want to process this document after all
		return true;
	}

}
