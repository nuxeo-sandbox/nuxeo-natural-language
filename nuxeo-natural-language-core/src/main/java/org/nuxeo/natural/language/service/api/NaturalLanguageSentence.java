/*
 * (C) Copyright 2017 Nuxeo SA (http://nuxeo.com/) and others.
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

import java.util.Map;

/**
 * On sentence in the analyzed text, with its own sentiment.
 *
 * Notice: This class is based on the {@link google.cloud.language.v1.Sentence}}
 * class of Google Cloud Natural Language API.
 *
 * @since 9.2
 */
public interface NaturalLanguageSentence {

	/**
	 * The sentence.
	 *
	 * @return value or {@code null} for none
	 */
	public String getText();

	/**
	 * The score of this sentence
	 *
	 * @return value
	 */
	public float getScore();

	/**
	 * The magnitude of this sentence
	 *
	 * @return value
	 */
	public float getMagnitude();

	/**
	 * Utility method to output an entity as String
	 *
	 * @return the string representation
	 * @since 9.2
	 */
	@Override
	public String toString();

	/**
	 * Utility method to output an entity as Map
	 *
	 * @return the string representation
	 * @since 9.2
	 */
	public Map<String, String> toMap();

}
