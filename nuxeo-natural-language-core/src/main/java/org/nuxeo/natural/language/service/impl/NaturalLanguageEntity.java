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

import java.util.List;
import java.util.Map;

/**
 * Represents a phrase in the text that is a known entity, such as a person, an
 * organization, or location. The API associates information, such as salience
 * and mentions, with entities.
 *
 * Notice: This class is based on the {@link Entity} class of Google Cloud
 * Natural Language API
 *
 *
 * @since 9.2
 */
public class NaturalLanguageEntity {

	protected String name;

	protected String type;

	protected float salience;

	protected Map<String, String> metadata;

	public NaturalLanguageEntity(String name, String type, float salience, Map<String, String> metadata) {
		this.name = name;
		this.type = type;
		this.salience = salience;
		this.metadata = metadata;
	}

	public String getName() {
		return name;
	}

	/**
     * The entity type.
     *
     * @return value or {@code null} for none
     */
	public String getType() {
		return type;
	}

	/**
     * The salience score associated with the entity in the [0, 1.0] range.
     *
     * The salience score for an entity provides information about the
     * importance or centrality of that entity to the entire document text.
     * Scores closer to 0 are less salient, while scores closer to 1.0 are
     * highly salient.
     *
     * @return value or {@code null} for none
     */
	public float getSalience() {
		return salience;
	}

	/**
     * Metadata associated with the entity.
     *
     * Currently, only Wikipedia URLs are provided, if available. The associated
     * key is "wikipedia_url".
     *
     * @return value or {@code null} for none
     */
	public Map<String, String> getMetadata() {
		return metadata;
	}

	/**
	 * Static utility searching for the first entity with this name
	 *
	 * @param entities
	 * @param name
	 * @return the found entity or null
	 */
	static public NaturalLanguageEntity findEntityForName(List<NaturalLanguageEntity> entities, String name) {

		for(NaturalLanguageEntity entity : entities) {
			if(entity.getName().equals(name)) {
				return entity;
			}
		}

		return null;
	}

	/**
     * Utility method to output an entity
     *
     * @param e
     * @return the string representation
     */
    @Override
	public  String toString() {
        return "" + getName() + "\n" + getType() + "\n" + getSalience()
                + "\n" + getMetadata();
    }


}
