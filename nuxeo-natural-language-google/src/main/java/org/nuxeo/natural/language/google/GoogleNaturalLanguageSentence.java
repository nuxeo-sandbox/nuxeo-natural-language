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
 *     thibaud
 */
package org.nuxeo.natural.language.google;

import java.util.Map;

import org.nuxeo.natural.language.service.api.NaturalLanguageSentence;

import com.google.cloud.language.v1.Sentence;
import com.google.cloud.language.v1.Sentiment;

/**
 *
 * @since 9.2
 */
public class GoogleNaturalLanguageSentence implements NaturalLanguageSentence {

	protected String text;

	protected float score;

	protected float magnitude;

	public GoogleNaturalLanguageSentence(Sentence sentence) {

		text = sentence.getText().getContent();

		Sentiment sentiment = sentence.getSentiment();
		score = sentiment.getScore();
		magnitude = sentiment.getMagnitude();
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public float getScore() {
		return score;
	}

	@Override
	public float getMagnitude() {
		return magnitude;
	}

	@Override
	public Map<String, String> toMap() {
		// TODO Auto-generated method stub
		// return null;
		throw new UnsupportedOperationException();
	}

}
