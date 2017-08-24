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
package org.nuxeo.natural.language.service.impl;

import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.natural.language.service.api.NaturalLanguageToken;

/**
 * Represents the smallest syntactic building block of the text.
 *
 * Notice: This class is based on the {@link com.google.cloud.language.v1.Token}
 * class of Google Cloud Natural Language API. See
 * https://cloud.google.com/natural-language/docs/basics#syntactic_analysis_responses
 *
 * @since 9.2
 */
public class NaturalLanguageTokenImpl implements NaturalLanguageToken {

	protected String text;

	protected int beginOffset;

	protected String tag;

	protected String lemma;

	protected String number;

	protected String gender;

	protected String mood;

	protected String person;

	protected String proper;

	protected String form;

	protected String aspect;

	protected String kase;

	public NaturalLanguageTokenImpl(String text, int beginOffset, String tag, String lemma, String number,
			String gender, String mood, String person, String proper, String form, String aspect, String theCase) {
		this.text = text;
		this.beginOffset = beginOffset;
		this.tag = tag;
		this.lemma = lemma;
		this.number = number;
		this.gender = gender;
		this.mood = mood;
		this.person = person;
		this.proper = proper;
		this.form = form;
		this.aspect = aspect;
		kase = theCase;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public int getBeginOffset() {
		return beginOffset;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public String getGender() {
		return gender;
	}

	@Override
	public String getMood() {
		return gender;
	}

	@Override
	public String getPerson() {
		return person;
	}

	@Override
	public String getProper() {
		return proper;
	}

	@Override
	public String getForm() {
		return form;
	}

	@Override
	public String getAspect() {
		return aspect;
	}

	@Override
	public String getCase() {
		return kase;
	}

	@Override
	public String getLemma() {
		return lemma;
	}

	@Override
	public String getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return "Text:\n" + text + "\n\nBeginOffset\n" + beginOffset + "\n\nTag\n" + tag + "\n\nLemma\n" + lemma
				+ "\n\nNumber\n" + number + "\n\nGender\n" + gender + "\n\nmMood\n" + mood + "\n\nPerson\n" + person
				+ "\n\nProper\n" + proper + "\n\nForm\n" + form + "\n\nAspect\n" + aspect + "\n\nCase\n" + kase + "\n";
	}

	@Override
	public JSONObject toJSON() throws JSONException {

		JSONObject obj = new JSONObject();

		obj.put("text", text);
		obj.put("beginOffset", beginOffset);
		obj.put("tag", tag);
		obj.put("lemma", lemma);
		obj.put("number", number);
		obj.put("gender", gender);
		obj.put("mood", mood);
		obj.put("person", person);
		obj.put("proper", proper);
		obj.put("form", form);
		obj.put("aspect", aspect);
		obj.put("case", kase);

		return obj;
	}

}
