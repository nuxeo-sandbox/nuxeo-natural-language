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
import java.util.Map;

/**
 * Represents a phrase in the text that is a known entity, such as a person, an
 * organization, or location. The API associates information, such as salience
 * and mentions, with entities.
 *
 * Notice: This class is based on the {@link Entity} class of Google Cloud
 * Natural Language API => see
 * https://cloud.google.com/natural-language/docs/basics#entity_analysis
 *
 *
 * @since 9.2
 */
public interface NaturalLanguageEntity {

	/**
	 * The value of the entity
	 *
	 * @return value
	 */
	public String getName();

	/**
	 * The entity type (for example if the entity is a person, location,
	 * consumer good, etc.)
	 *
	 * @return value or {@code null} for none
	 */
	public String getType();

	/**
	 * The salience score associated with the entity in the [0, 1.0] range.
	 *
	 * The salience indicates the importance or relevance of this entity to the
	 * entire document text. This score can assist information retrieval and
	 * summarization by prioritizing salient entities. Scores closer to 0.0 are
	 * less important, while scores closer to 1.0 are highly important.
	 *
	 * @return value or {@code null} for none
	 */
	public float getSalience();

	/**
	 * List of entities that are . This information can be useful if you want to
	 * find all mentions of the person “Lawrence” in the text but not the film
	 * title. You can also use mentions to collect the list of entity aliases
	 *
	 * (NOTE: Google Language has a more complex EntityMention, giving also the
	 * offset of the mention)
	 *
	 * @return value or {@code null} for none
	 */
	public List<String> getMentions();

	/**
	 * Metadata associated with the entity.
	 *
	 * Currently, only Wikipedia URLs are provided, if available. The associated
	 * key is "wikipedia_url".
	 *
	 * @return value or {@code null} for none
	 */
	public Map<String, String> getMetadata();

	/**
	 * Utility method to output an entity
	 *
	 * @return the string representation
	 */
	@Override
	public String toString();


	/**
	 * Utility method to output an entity in a map
	 *
	 * @return the map representation
	 */
	public Map<String, String> toMap();

	/**
	 * Static utility searching for the first entity with this name
	 *
	 * @param entities
	 * @param name
	 * @return the found entity or null
	 */
	static public NaturalLanguageEntity findEntityForName(List<NaturalLanguageEntity> entities, String name) {

		for (NaturalLanguageEntity entity : entities) {
			if (entity.getName().equals(name)) {
				return entity;
			}
		}

		return null;
	}

}
