/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
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

/**
 * Represents the smallest syntactic building block of the text.
 *
 * Notice: This interface is based on the {@link Entity} class of Google Cloud
 * Natural Language API. We inly use some fields to define the interface
 *
 *
 * @since 9.1
 */
public interface NaturalLanguageToken {

    /**
     * The token text.
     *
     * @return value or {@code null} for none
     */
    public String getText();

    /**
     * Part of speech tag for this token.
     *
     * A value among https://cloud.google.com/natural-language/reference/rest/v1beta1/documents/annotateText#tag
     * For example:
     * <ul>
     * <li>ADJ (Adjective)</li>
     * <li>ADV (Adverb)</li>
     * <li>VERB</li>
     * <li>...</li>
     * </ul>
     *
     * @return value or {@code null} for none
     */
    public String getPartOfSpeech();

    /**
     * [Lemma](https://en.wikipedia.org/wiki/Lemma_(morphology)) of the token.
     *
     * @return value or {@code null} for none
     */
    public String getLemma();

}
