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

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the smallest syntactic building block of the text.
 *
 * Notice: This class is based on the {@link Entity} class of Google Cloud
 * Natural Language API. See
 * https://cloud.google.com/natural-language/docs/basics#syntactic_analysis_responses
 *
 * @since 9.2
 */
public class NaturalLanguageToken {

	protected String text;

	protected String tag;

	protected String lemma;

	protected String gender;

	protected String mood;

	protected String person;

	protected String proper;

	protected String form;

	protected String aspect;

	protected String theCase;

	public NaturalLanguageToken(String text, String tag, String lemma, String gender, String mood, String person,
			String proper, String form, String aspect, String theCase) {
		this.text = text;
		this.tag = tag;
		this.lemma = lemma;
		this.gender = gender;
		this.mood = mood;
		this.person = person;
		this.proper = proper;
		this.form = form;
		this.aspect = aspect;
		this.theCase = theCase;
	}

	/**
	 * The token text.
	 *
	 * @return value or {@code null} for none
	 */
	public String getText() {
		return text;
	}

	/**
	 * Part of speech tag for this token.
	 *
	 * A value among
	 * https://cloud.google.com/natural-language/reference/rest/v1beta1/documents/annotateText#tag
	 * For example:
	 *
	 * <pre>
	 * UNKNOWN
	 * ADJ (Adjective)
	 * ADV (Adverb)
	 * VERB
	 * ...
	 * </pre>
	 *
	 * @return value or {@code null} for none
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * <<Gammatical gender: Gender classes of nouns reflected in the behaviour
	 * of associated words.>>
	 *
	 * Examples:
	 *
	 * <pre>
	 * GENDER_UNKNOWN
	 * FEMININE
	 * MASCULINE
	 * NEUTER
	 * UNRECOGNIZED
	 * </pre>
	 *
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * <<The grammatical feature of verbs, used for showing modality and
	 * attitude.>>
	 *
	 * Examples:
	 *
	 * <pre>
	 * MOOD_UNKNOWN
	 * CONDITIONAL_MOOD
	 * IMPERATIVE
	 * INDICATIVE
	 * INTERROGATIVE
	 * JUSSIVE
	 * SUBJUNCTIVE
	 * UNRECOGNIZED
	 * </pre>
	 *
	 * @return the mood
	 */
	public String getMood() {
		return gender;
	}

	/**
	 * <<The distinction between the speaker, second person, third person,
	 * etc.>>
	 *
	 * Examples:
	 *
	 * <pre>
	 * PERSON_UNKNOWN
	 * FIRST
	 * SECOND
	 * THIRD
	 * REFLEXIVE_PERSON
	 * </pre>
	 *
	 * @return the person
	 */
	public String getPerson() {
		return person;
	}

	/**
	 * <<This category shows if the token is part of a proper name.>>
	 *
	 * Examples:
	 *
	 * <pre>
	 * PROPER_UNKNOWN
	 * PROPER
	 * NOT_PROPER
	 * UNRECOGNIZED
	 * </pre>
	 *
	 * @return the proper
	 */
	public String getProper() {
		return proper;
	}

	/**
	 * The grammatical form. <<Depending on the language, Form can be
	 * categorizing different forms of verbs, adjectives, adverbs, etc. For
	 * example, categorizing inflected endings of verbs and adjectives or
	 * distinguishing between short and long forms of adjectives and
	 * participles>>
	 *
	 * Examples:
	 *
	 * <pre>
	 * FORM_UNKNOWN
	 * ADNOMIAL
	 * AUXILIARY
	 * COMPLEMENTIZER
	 * FINAL_ENDING
	 * GERUND
	 * REALIS
	 * IRREALIS
	 * SHORT
	 * LONG
	 * ORDER
	 * SPECIFIC
	 * UNRECOGNIZED
	 * </pre>
	 *
	 * @return the form
	 */
	public String getForm() {
		return form;
	}

	/**
	 * <<The grammatical aspect. The characteristic of a verb that expresses
	 * time flow during an event.>>
	 *
	 * Examples:
	 *
	 * <pre>
	 * ASPECT_UNKNOWN
	 * PERFECTIVE
	 * IMPERFECTIVE
	 * PROGRESSIVE
	 * UNRECOGNIZED
	 * </pre>
	 *
	 * @return the aspect
	 */
	public String getAspect() {
		return aspect;
	}

	/**
	 * <<The grammatical case. The grammatical function performed by a noun or
	 * pronoun in a phrase, clause, or sentence. In some languages, other parts
	 * of speech, such as adjective and determiner, take case inflection in
	 * agreement with the noun.>>
	 *
	 * Examples:
	 *
	 * <pre>
	 * CASE_UNKNOWN
	 * ACCUSATIVE
	 * ADVERBIAL
	 * COMPLEMENTIVE
	 * DATIVE
	 * GENITIVE
	 * INSTRUMENTAL
	 * LOCATIVE
	 * NOMINATIVE
	 * OBLIQUE
	 * PARTITIVE
	 * PREPOSITIONAL
	 * REFLEXIVE_CASE
	 * RELATIVE_CASE
	 * VOCATIVE
	 * UNRECOGNIZED
	 * </pre>
	 *
	 * @return the case
	 */
	public String getCase() {
		return theCase;
	}

	/**
	 * The "root" word upon which this word is based, which allows you to
	 * canonicalize word usage within your text. For example, the words "write",
	 * "writing", "wrote" and "written" all are based on the same lemma
	 * ("write")
	 *
	 * @return value or {@code null} for none
	 */
	public String getLemma() {
		return lemma;
	}

	/**
	 * Utility method to output an entity as String
	 *
	 * @return the string representation
	 * @since 9.2
	 */
	@Override
	public String toString() {
		return "Text:\n" + text + "\n\nTag\n" + tag + "\n\nLemma\n" + lemma + "\n\nGender\n" + gender + "\n\nmMood\n"
				+ mood + "\n\nPerson\n" + person + "\n\nProper\n" + proper + "\n\nForm\n" + form + "\n\nAspect\n"
				+ aspect + "\n\nCase\n" + theCase + "\n";
	}

	/**
	 * Utility method to output an entity as Map
	 *
	 * @return the string representation
	 * @since 9.2
	 */
	public Map<String, String> toMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("text", text);
		map.put("tag", tag);
		map.put("lemma", lemma);
		map.put("gender", gender);
		map.put("mood", mood);
		map.put("person", person);
		map.put("proper", proper);
		map.put("form", form);
		map.put("aspect", aspect);
		map.put("case", theCase);

		return map;
	}

}
