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
package org.nuxeo.natural.language.service.api;

import java.util.List;

/**
 * The response from the service.
 *
 * Notice: This interface is based on the {@link AnnotateTextResponse} class of
 * Google Cloud Natural Language API. Not all APIs and accessors are made
 * available, at least in the first version, but one can always call
 * <code>getNativeObject</code> and then extract all and every info as returned
 * by the provider for the given features. Also, we made it simpler (sentences
 * are just a list of String, no other information for example)
 *
 * @since 9.2
 */
public interface NaturalLanguageResponse {

	// <--------------------- Language -------------------->
	/**
	 *
	 * @return the language, when extracted
	 */
	String getLanguage();

	// <--------------------- Sentiment -------------------->
	/**
	 * score of the sentiment ranges between -1.0 (negative) and 1.0 (positive)
	 * and corresponds to the overall emotional leaning of the text.
	 *
	 * @return value or {@code null} for none (asking sentiment info while it
	 *         was not requested in the features for example)
	 */
	public Float getSentimentScore();

	/**
	 * magnitude indicates the overall strength of emotion (both positive and
	 * negative) within the given text, between 0.0 and +inf. Unlike score,
	 * magnitude is not normalized; each expression of emotion within the text
	 * (both positive and negative) contributes to the text's magnitude (so
	 * longer text blocks may have greater magnitudes)
	 *
	 * @return value or {@code null} for none (asking sentiment info while it
	 *         was not requested in the features for example)
	 */
	public Float getSentimentMagnitude();

	// <--------------------- Sentences -------------------->
	/**
	 * Return the Document sentences
	 *
	 * @return value or {@code null} for none
	 */
	List<NaturalLanguageSentence> getSentences();

	// <--------------------- Entities -------------------->
	/**
	 * Return the entities, along with their semantic information, in the input
	 * document.
	 *
	 * @return value or {@code null} for none
	 */
	List<NaturalLanguageEntity> getEntities();

	// <--------------------- Tokens -------------------->
	/**
	 * Tokens, along with their syntactic information, in the input document.
	 * Populated if the user enables
	 * AnnotateTextRequest.Features.extract_syntax.
	 *
	 * @return value or {@code null} for none
	 */
	List<NaturalLanguageToken> getTokens();

	// <--------------------- Other -------------------->
	/**
	 *
	 * @return the native object with no modification, as returned by the
	 *         service provider
	 */
	Object getNativeObject();
}
