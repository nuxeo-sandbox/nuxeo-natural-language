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
 * The feeling associated with the entire text or entities in the text.
 *
 * Notice: This interface is based on the {@link Sentiment} class of Google
 * Cloud Natural Language API
 *
 * @since 9.2
 */
public interface NaturalLanguageDocumentSentiment {

    /**
     * A non-negative number in the [0, +inf) range, which represents the
     * absolute magnitude of sentiment regardless of polarity (positive or
     * negative).
     *
     * @return value or {@code null} for none
     */
    public Float getMagnitude();

    /**
     * Polarity of the sentiment in the [-1.0, 1.0] range. Larger numbers
     * represent more positive sentiments.
     *
     * @return value or {@code null} for none
     */
    public Float getPolarity();

}
