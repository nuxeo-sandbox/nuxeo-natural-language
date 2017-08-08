/*
 * (C) Copyright 2015-2016 Nuxeo SA (http://nuxeo.com/) and others.
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
package org.nuxeo.natural.language.service;

import java.util.List;

/**
 * The response from the service.
 *
 * Notice: This interface is based on the {@link Entity} class of Google Cloud
 * Natural Language API. Not all APIs and accessors are made available, at least
 * in the first version, but one can always call <code>getNativeObject</code>
 * and then extract all and every info as returned by the provider. Also, we
 * made it simpler (sentences are just a list of String, no offset in the document)
 *
 * @since 9.1
 */
public interface NaturalLanguageResponse {

    /**
     *
     * @return the language, when extracted
     */
    String getLanguage();

    /**
     * Return the entities, along with their semantic information, in the input
     * document.
     *
     * @return value or {@code null} for none
     */
    List<NaturalLanguageEntity> getEntities();

    /**
     * Return the Document Sentiment of the Document (magnitude and polarity)
     *
     * @return value or {@code null} for none
     */
    NaturalLanguageDocumentSentiment getDocumentSentiment();

    /**
     * Return the Document sentences
     *
     * @return value or {@code null} for none
     */
    List<String> getSentences();

    /**
     * Tokens, along with their syntactic information, in the input document. Populated if the user
     * enables AnnotateTextRequest.Features.extract_syntax.
     *
     * @return value or {@code null} for none
     */
    List<NaturalLanguageToken> getTokens();

    /**
     *
     * @return the native object with no modification, as returned by the
     *         service provider
     */
    Object getNativeObject();
}
