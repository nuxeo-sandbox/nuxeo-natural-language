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
package org.nuxeo.natural.language.service.impl;

import org.nuxeo.natural.language.service.api.NaturalLanguageSentence;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementation of the NaturalLanguageSentence interface, with also the
 * toString() and toMap() utilities
 *
 * @since 9.2
 */
public class NaturalLanguageSentenceImpl implements NaturalLanguageSentence {

	protected String text;

	protected float score;

	protected float magnitude;

	public NaturalLanguageSentenceImpl(String text, float score, float magnitude) {

		this.text = text;
		this.score = score;
		this.magnitude = magnitude;
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
	public String toString() {
		return "Text:\n" + text + "\n\nScore: " + score + "\nMagnitude: " + magnitude;
	}

	@Override
	public JSONObject toJSON() throws JSONException {

		JSONObject obj = new JSONObject();

		obj.put("text", text);
		obj.put("score", score);
		obj.put("magnitude", magnitude);

		return obj;
	}

}
