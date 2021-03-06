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
package org.nuxeo.natural.language.core.test.mock;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.natural.language.service.api.NaturalLanguageEncoding;
import org.nuxeo.natural.language.service.api.NaturalLanguageFeature;
import org.nuxeo.natural.language.service.api.NaturalLanguageProvider;
import org.nuxeo.natural.language.service.api.NaturalLanguageResponse;

/**
 * A NaturalLanguageProvider mock-up. Does not connect to anything, returns hard
 * coded values (see {@code MockNaturalLanguageResponse}
 *
 * Configured in /src/test/resources/OSGI-INF/mock-provider-contrib.xml
 *
 * @since 9.2
 */
public class MockNaturalLanguageProvider implements NaturalLanguageProvider {

	public static final String NAME = "mock";

	public MockNaturalLanguageProvider(Map<String, String> parameters) {

	}

	@Override
	public NaturalLanguageResponse processText(String text, List<NaturalLanguageFeature> features,
			NaturalLanguageEncoding encoding) throws NuxeoException {
		return new MockNaturalLanguageResponse();
	}

	@Override
	public List<NaturalLanguageFeature> getSupportedFeatures() {
		return Arrays.asList(NaturalLanguageFeature.DOCUMENT_SENTIMENT);
	}

	@Override
	public Object getNativeClient() {
		return null;
	}

}
