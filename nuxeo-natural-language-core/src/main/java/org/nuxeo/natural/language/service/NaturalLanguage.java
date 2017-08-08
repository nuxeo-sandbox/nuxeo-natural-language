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
package org.nuxeo.natural.language.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;

/**
 * A service that performs Natural Language tasks like text analysis, sentiment extraction, ...
 */
public interface NaturalLanguage {

    String EVENT_NATURAL_LANGUAGE_PROCESSING_DONE = "naturalLanguageProcessingDone";

    /**
     * @param blob the text blob
     * @param features the feature to request from the service
     * @param maxResults the maximum number of results per feature
     * @return a {@link NaturalLanguageResponse} object
     * @since 9.2
     */
    NaturalLanguageResponse processBlob(Blob blob, List<NaturalLanguageFeature> features, int maxResults)
            throws IOException, GeneralSecurityException;

    /**
     * 
     * @param provider the provider to use
     * @param blob the text blob
     * @param features the feature to request from the service
     * @param maxResults the maximum number of results per feature
     * @return a {@link NaturalLanguageResponse} object
     * @since 9.2
     */
    NaturalLanguageResponse processBlob(String provider, Blob blob, List<NaturalLanguageFeature> features, int maxResults)
            throws IOException, GeneralSecurityException;

    /**
     * @param blobs a list of text blobs
     * @param features the feature to request from the service
     * @param maxResults the maximum number of results per feature
     * @return a list of {@link NaturalLanguageResponse} object
     */
    List<NaturalLanguageResponse> processBlobs(List<Blob> blobs, List<NaturalLanguageFeature> features, int maxResults)
            throws IOException, GeneralSecurityException;


    /**
     *
     * @param provider the provider to use
     * @param blobs a list of text blobs
     * @param features the feature to request from the service
     * @param maxResults the maximum number of results per feature
     * @return a list of {@link NaturalLanguageResponse} object
     * @since 9.2
     */
    List<NaturalLanguageResponse> processBlobs(String provider, List<Blob> blobs, List<NaturalLanguageFeature> features, int maxResults)
            throws IOException, GeneralSecurityException;

    /**
     * @return The name of the automation name to use for Pictures
     */
    String getMapperChainName();

    /**
     * @return The name of default provider
     * @since 9.2
     */
    String getDefaultProvider();

    /**
     * @return The provider object
     * @since 9.2
     */
    NaturalLanguageProvider getProvider(String name);

    /**
     * @return all registered providers
     * @since 9.2
     */
    Map<String, NaturalLanguageProvider> getProviders();

}
