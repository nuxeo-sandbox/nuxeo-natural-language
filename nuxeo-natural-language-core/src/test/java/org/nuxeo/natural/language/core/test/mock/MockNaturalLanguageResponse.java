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
package org.nuxeo.natural.language.core.test.mock;

import java.util.List;

import org.nuxeo.natural.language.service.api.NaturalLanguageResponse;
import org.nuxeo.natural.language.service.impl.NaturalLanguageEntity;
import org.nuxeo.natural.language.service.impl.NaturalLanguageToken;

/**
 *
 * @since 9.2
 */
public class MockNaturalLanguageResponse implements NaturalLanguageResponse {

    public static final String LANGUAGE = "en";

	@Override
	public String getLanguage() {
		return LANGUAGE;
	}

	@Override
	public Float getSentimentScore() {
		return null;
	}

	@Override
	public Float getSentimentMagnitude() {
		return null;
	}

	@Override
	public List<String> getSentences() {
		return null;
	}

	@Override
	public List<NaturalLanguageEntity> getEntities() {
		return null;
	}

	@Override
	public List<NaturalLanguageToken> getTokens() {
		return null;
	}

	@Override
	public Object getNativeObject() {
		return null;
	}

}
