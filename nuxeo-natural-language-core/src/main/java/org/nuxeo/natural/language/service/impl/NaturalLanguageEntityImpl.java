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
import java.util.List;
import java.util.Map;

import org.nuxeo.natural.language.service.api.NaturalLanguageEntity;

/**
 * IMplementaiton of the NaturalLanguageEntity interface
 *
 *
 * @since 9.2
 */
public class NaturalLanguageEntityImpl implements NaturalLanguageEntity{

	protected String name;

	protected String type;

	protected float salience;

	protected List<String> mentions;

	protected Map<String, String> metadata;

	public NaturalLanguageEntityImpl(String name, String type, float salience, List<String> mentions,
			Map<String, String> metadata) {
		this.name = name;
		this.type = type;
		this.salience = salience;
		this.mentions = mentions;
		this.metadata = metadata;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public float getSalience() {
		return salience;
	}

	@Override
	public List<String> getMentions() {
		return mentions;
	}

	@Override
	public Map<String, String> getMetadata() {
		return metadata;
	}

	@Override
	public String toString() {
		return "" + name + "\n" + type + "\n" + salience + "\n" + mentions + "\n" + metadata;
	}

	@Override
	public Map<String, String> toMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("type", type);
		map.put("salience", "" + salience);
		if (mentions != null) {
			map.put("mentions", mentions.toString());
		} else {
			map.put("mentions", "");
		}
		if (metadata != null) {
			map.put("metadata", metadata.toString());
		} else {
			map.put("metadata", "");
		}

		return map;
	}

}
