/*
 * (C) Copyright 2015-2017 Nuxeo (http://nuxeo.com/) and others.
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
 *     Remi Cattiau (via nuxeo-vision)
 *     Michael Vachette (via nuxeo-vision)
 *     Thibaud Arguillere
 */

package org.nuxeo.natural.language.service.api;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * A Natural Language provider is a wrapper that encapsulates calls to a given
 * Natural Language service
 *
 * @since 9.2
 */
public interface NaturalLanguageProvider {

	/**
	 *
	 * The NuxeoException should encapsulate any exception:
	 * IO/GeneralSecurity/etc. but also provider-specific errors such a an
	 * unsupported language (i.e. an error when the string is written in a
	 * language not supported by the provider, like latin for Google)
	 *
	 * @param text
	 *            Text to analyze
	 * @param features
	 *            Features to request
	 * @param encoding
	 *            Encoding to use. Can be {@code null}. Possible values are
	 *            "UTF8", "UTF16" and "UTF32"
	 * @throws IOException
	 * @throws GeneralSecurityException
	 * @throws IllegalStateException
	 * @throws NuxeoException
	 * @return a {@link NaturalLanguageResponse} object
	 */
	NaturalLanguageResponse processText(String text, List<NaturalLanguageFeature> features,
			NaturalLanguageEncoding encoding) throws NuxeoException;

	/**
	 * @return The list of feature supported by the provider
	 */
	List<NaturalLanguageFeature> getSupportedFeatures();

	/**
	 * @return the provider native client object
	 */
	Object getNativeClient();

}
