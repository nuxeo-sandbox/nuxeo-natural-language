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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents the smallest syntactic building block of the text.
 *
 * Notice: This class is based on the {@link com.google.cloud.language.v1.Token}
 * class of Google Cloud Natural Language API. See
 * https://cloud.google.com/natural-language/docs/basics#syntactic_analysis_responses
 *
 * @since 9.2
 */
public interface NaturalLanguageToken {

	/**
	 * The token text.
	 *
	 * @return value or {@code null} for none
	 */
	public String getText();

	/**
	 * The token text begin offset.
	 *
	 * @return value or -1 when parsing could not detect the offset (main
	 *         reason: No encoding passed when calling the service)
	 */
	public int getBeginOffset();

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
	public String getTag();

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
	public String getGender();

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
	public String getMood();

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
	public String getPerson();

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
	public String getProper();

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
	public String getForm();

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
	public String getAspect();

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
	public String getCase();

	/**
	 * The "root" word upon which this word is based, which allows you to
	 * canonicalize word usage within your text. For example, the words "write",
	 * "writing", "wrote" and "written" all are based on the same lemma
	 * ("write")
	 *
	 * @return value or {@code null} for none
	 */
	public String getLemma();

	/**
	 *
	 * @return the JSON representation of the sentence
	 * @throws JSONException
	 */
	public JSONObject toJSON() throws JSONException;

	/**
	 * Utility, to build a JSON Array from the list of sentences
	 *
	 * @param sentences
	 * @return the JSON array representation of the sentences
	 * @throws JSONException
	 */
	public static JSONArray tokensToJSONArray(List<NaturalLanguageToken> tokens) throws JSONException {

		JSONArray array = new JSONArray();

		if (tokens != null) {
			for (NaturalLanguageToken token : tokens) {
				array.put(token.toJSON());
			}
		}

		return array;
	}

}
