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
package org.nuxeo.natural.language.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * @since 9.2
 */
public class NaturalLanguageImpl extends DefaultComponent implements NaturalLanguage {

    private static final Log log = LogFactory.getLog(NaturalLanguageImpl.class);

    protected static final String CONFIG_EXT_POINT = "configuration";

    protected static final String PROVIDER_EXT_POINT = "provider";

    protected NaturalLanguageDescriptor config = null;

    protected Map<String, NaturalLanguageProvider> providers = new HashMap<>();

    /**
     * Component activated notification. Called when the component is activated. All component dependencies are resolved
     * at that moment. Use this method to initialize the component.
     *
     * @param context the component context.
     */
    @Override
    public void activate(ComponentContext context) {
        super.activate(context);
    }

    /**
     * Component deactivated notification. Called before a component is unregistered. Use this method to do cleanup if
     * any and free any resources held by the component.
     *
     * @param context the component context.
     */
    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    /**
     * Application started notification. Called after the application started. You can do here any initialization that
     * requires a working application (all resolved bundles and components are active at that moment)
     *
     * @param context the component context. Use it to get the current bundle context
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
                NaturalLanguageProvider provider = (NaturalLanguageProvider) desc.getKlass().getConstructor(Map.class).newInstance(
                        desc.getParameters());
                providers.put(desc.getProviderName(), provider);
            } catch (ReflectiveOperationException e) {
                throw new NuxeoException(e);
            }
        }
    }

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        // Logic to do when unregistering any contribution
    }

    @Override
    public NaturalLanguageResponse processBlob(Blob blob, List<NaturalLanguageFeature> features, int maxResults)
            throws IOException, GeneralSecurityException, IllegalStateException {
        return processBlob(config.getDefaultProviderName(), blob, features, maxResults);
    }

    @Override
    public List<NaturalLanguageResponse> processBlobs(List<Blob> blobs, List<NaturalLanguageFeature> features, int maxResults)
            throws IOException, GeneralSecurityException, IllegalStateException {
        return processBlobs(config.getDefaultProviderName(), blobs, features, maxResults);
    }

    @Override
    public NaturalLanguageResponse processBlob(String providerName, Blob blob, List<NaturalLanguageFeature> features, int maxResults)
            throws IOException, GeneralSecurityException {
        if (blob == null) {
            throw new IllegalArgumentException("Input Blob cannot be null");
        } else if (features == null || features.size() == 0) {
            throw new IllegalArgumentException("The feature list cannot be empty or null");
        }

        List<NaturalLanguageResponse> results = processBlobs(providerName, Arrays.asList(blob), features, maxResults);
        if (results.size() > 0) {
            return results.get(0);
        } else {
            throw new NuxeoException(
                    "Provider " + providerName + " Natural Language returned empty results for " + blob.getFilename());
        }
    }

    @Override
    public List<NaturalLanguageResponse> processBlobs(String providerName, List<Blob> blobs, List<NaturalLanguageFeature> features,
            int maxResults) throws IOException, GeneralSecurityException {
        NaturalLanguageProvider provider = providers.get(providerName);

        if (provider == null) {
            throw new NuxeoException("Unknown provider: " + providerName);
        }

        if (blobs == null || blobs.size() == 0) {
            throw new IllegalArgumentException("Input Blob list cannot be null or empty");
        } else if (!provider.checkBlobs(blobs)) {
            throw new IllegalArgumentException("Too many blobs or size exceeds the API limit");
        } else if (features == null || features.size() == 0) {
            throw new IllegalArgumentException("The feature list cannot be empty or null");
        }
        return provider.processBlobs(blobs, features, maxResults);
    }

    @Override
    public String getMapperChainName() {
        return config.getMapperChainName();
    }

    @Override
    public String getDefaultProvider() {
        return config.getDefaultProviderName();
    }

    @Override
    public NaturalLanguageProvider getProvider(String name) {
        return providers.get(name);
    }

    @Override
    public Map<String, NaturalLanguageProvider> getProviders() {
        return providers;
    }

}
