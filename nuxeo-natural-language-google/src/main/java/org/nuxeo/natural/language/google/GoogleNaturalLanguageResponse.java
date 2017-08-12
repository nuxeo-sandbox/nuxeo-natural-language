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
package org.nuxeo.natural.language.google;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.natural.language.service.api.NaturalLanguageResponse;
import org.nuxeo.natural.language.service.impl.NaturalLanguageEntity;
import org.nuxeo.natural.language.service.impl.NaturalLanguageToken;

import com.google.cloud.language.v1.AnnotateTextResponse;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.PartOfSpeech;
import com.google.cloud.language.v1.Sentence;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;

/**
 * Implementation of the NaturalLanguageResponse wrapping Google's
 * {@code AnnotateTextResponse}
 *
 * @since 9.2
 */
public class GoogleNaturalLanguageResponse implements NaturalLanguageResponse {

	AnnotateTextResponse nativeResponse;

	Sentiment sentimentDoc = null;

	boolean sentimentDocTested = false;

	List<String> sentences = null;

	boolean sentencesTested = false;

	List<NaturalLanguageToken> tokens = null;

	boolean tokensTested = false;

	List<NaturalLanguageEntity> entities = null;

	boolean entitiesTested = false;

	public GoogleNaturalLanguageResponse(AnnotateTextResponse response) {
		nativeResponse = response;
	}

	protected Sentiment getDocumentSentiment() {
		if (sentimentDoc == null && !sentimentDocTested) {
			sentimentDoc = nativeResponse.getDocumentSentiment();
			sentimentDocTested = true;
		}

		return sentimentDoc;
	}

	// <--------------------- Language -------------------->
	@Override
	public String getLanguage() {
		return nativeResponse.getLanguage();
	}

	// <--------------------- Sentiment -------------------->
	@Override
	public Float getSentimentScore() {
		Sentiment sentiment = getDocumentSentiment();
		if (sentiment != null) {
			return sentiment.getScore();
		}

		return null;
	}

	@Override
	public Float getSentimentMagnitude() {
		Sentiment sentiment = getDocumentSentiment();
		if (sentiment != null) {
			return sentiment.getMagnitude();
		}

		return null;
	}

	// <--------------------- Sentences -------------------->
	/**
	 * Return the Document sentences
	 *
	 * @return value or {@code null} for none
	 */
	@Override
	public List<String> getSentences() {

		if (sentences == null && !sentencesTested) {

			List<Sentence> googleSentences = nativeResponse.getSentencesList();
			if (googleSentences != null) {
				sentences = new ArrayList<String>();
				for (Sentence oneSentence : googleSentences) {
					// TODO
					// Add a NaturalLanguageSentence class that also contains
					// these information
					// Sentiment sentiment = oneSentence.getSentiment();
					// sentiment.getMagnitude();
					// sentiment.getScore();
					sentences.add(oneSentence.getText().getContent());
				}
			}

			sentencesTested = true;
		}

		return sentences;
	}

	// <--------------------- Entities -------------------->
	@Override
	public List<NaturalLanguageEntity> getEntities() {
		if (entities == null && !entitiesTested) {

			List<Entity> googleEntities = nativeResponse.getEntitiesList();
			if (googleEntities != null) {
				NaturalLanguageEntity entity;
				entities = new ArrayList<NaturalLanguageEntity>();
				for (Entity googleEntity : googleEntities) {
					ArrayList<String> mentions = new ArrayList<String>();
					List<EntityMention> googleMentions = googleEntity.getMentionsList();
					for (EntityMention googleMention : googleMentions) {
						mentions.add(googleMention.getText().getContent());
						// googleMention.getType().name();
						// TYPE_UNKNOWN
						// PROPER
						// COMMON
						// UNRECOGNIZED
					}

					Entity.Type entityType = googleEntity.getType();
					String typeName = entityType == null ? Entity.Type.UNKNOWN.name() : entityType.name();

					entity = new NaturalLanguageEntity(googleEntity.getName(), typeName, googleEntity.getSalience(),
							mentions, googleEntity.getMetadataMap());
					entities.add(entity);
				}

			}

			entitiesTested = true;
		}

		return entities;
	}

	// <--------------------- Tokens -------------------->
	@Override
	public List<NaturalLanguageToken> getTokens() {

		if (tokens == null && !tokensTested) {

			List<Token> googleTokens = nativeResponse.getTokensList();
			if (googleTokens != null) {
				NaturalLanguageToken token;
				tokens = new ArrayList<NaturalLanguageToken>();
				for (Token googleToken : googleTokens) {
					String text = googleToken.getText().getContent(); // toString();
					int beginOffset = googleToken.getText().getBeginOffset();
					String lemma = googleToken.getLemma();

					PartOfSpeech partOfSpeech = googleToken.getPartOfSpeech();
					String tagName = partOfSpeech.getTag().name();
					String gender = partOfSpeech.getGender().name();
					String mood = partOfSpeech.getMood().name();
					String person = partOfSpeech.getPerson().name();
					String proper = partOfSpeech.getProper().name();
					String form = partOfSpeech.getForm().name();
					String aspect = partOfSpeech.getAspect().name();
					String theCase = partOfSpeech.getCase().name();

					token = new NaturalLanguageToken(text, beginOffset, tagName, lemma, gender, mood, person, proper,
							form, aspect, theCase);
					tokens.add(token);

				}
			}

			tokensTested = true;
		}

		return tokens;
	}

	// <--------------------- Other -------------------->
	@Override
	public Object getNativeObject() {
		return nativeResponse;
	}

}
