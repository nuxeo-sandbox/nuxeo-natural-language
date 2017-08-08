/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
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
package org.nuxeo.natural.language.service;

import java.util.Map;

/**
 * Represents a phrase in the text that is a known entity, such as a person, an
 * organization, or location. The API associates information, such as salience
 * and mentions, with entities.
 *
 * Notice: This interface is based on the {@link Entity} class of Google Cloud
 * Natural Language API
 *
 *
 * @since 9.2
 */
public interface NaturalLanguageEntity {

    /**
     * The representative name for the entity.
     *
     * @return value or {@code null} for none
     */
    public String getName();

    /**
     * The entity type.
     *
     * @return value or {@code null} for none
     */
    public String getType();

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
    public float getSalience();

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
     * @param e
     * @return the string representation
     */
    public static String toString(NaturalLanguageEntity e) {
        return "" + e.getName() + "\n" + e.getType() + "\n" + e.getSalience()
                + "\n" + e.getMetadata();
    }

}
