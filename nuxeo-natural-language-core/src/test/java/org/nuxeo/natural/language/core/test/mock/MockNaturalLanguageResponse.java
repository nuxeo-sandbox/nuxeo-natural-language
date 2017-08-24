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

import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.natural.language.service.api.NaturalLanguageEntity;
import org.nuxeo.natural.language.service.api.NaturalLanguageResponse;
import org.nuxeo.natural.language.service.api.NaturalLanguageSentence;
import org.nuxeo.natural.language.service.api.NaturalLanguageToken;

/**
 * A NaturalLanguageResponse mock-up returning hard coded values
 *
 * @since 9.2
 */
public class MockNaturalLanguageResponse implements NaturalLanguageResponse {

	public static final String LANGUAGE = "en";

	public static final float SCORE = 0.5f;

	public static final float MAGNITUDE = 0.3f;

	@Override
	public String getLanguage() {
		return LANGUAGE;
	}

	@Override
	public Float getSentimentScore() {
		return SCORE;
	}

	@Override
	public Float getSentimentMagnitude() {
		return MAGNITUDE;
	}

	@Override
	public List<NaturalLanguageSentence> getSentences() {
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

	@Override
	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("language", LANGUAGE);
		obj.put("score", SCORE);
		obj.put("magnitude", MAGNITUDE);

		return obj;
	}

}
