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
package org.nuxeo.natural.language.google.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.SimpleFeature;

/**
 * To test the feature, we don't want to hard code the Google credentials info,
 * of course.. There are 2 ways to handle authentication during tests:
 *
 * <ul>
 * <li>Use the (git ignored) "natural-language-test.conf" file:
 * <ul>
 * <li>This file is at
 * nuxeo-natural-language/nuxeo-natural-language-google/src/test/resources/</li>
 * <li>It contains the key that tells the credentials file to use</li>
 * <li>The .gitignore config file ignores this file, so it is not sent on
 * GitHub</li>
 * <li>So, basically to run the test, create this file at
 * nuxeo-natural-language/nuxeo-natural-language-google/src/test/resources/natural-language-test.conf
 * and set the properties as expected by the Google provider. See the
 * google-natural-language-provider.xml file in the natural-language-google
 * source code, and add this to the file:
 *
 * <pre>
 * org.nuxeo.natural.language.google.credential=/path/to/your/credentials.json
 * </pre>
 *
 * </li>
 * </ul>
 * </li>
 * <li>Use environment variables
 * <li>This is mainly used with maven</li>
 * <li>See the pom.xml file of the plugin, it defines some variables in the
 * systemPropertyVariables attribute of the maven plugin</li></li>
 * <ul>
 *
 * </ul>
 * </ul>
 *
 * When adding a new provider, you can follow the same logic.
 *
 * @since 9.2
 */
public class SimpleFeatureCustom extends SimpleFeature {

	public static final String TEST_CONF_FILE = "natural-language-test.conf";

	public static final String GOOGLE_CREDENTIALS_CONFIGURATION_PARAM = "org.nuxeo.natural.language.google.credential";

	// FOund in the pom.xml
	public static final String GOOGLE_CREDENTIALS_TEST_PARAM = "org.nuxeo.natural.language.test.google.credential.file";

	protected static Properties props = null;

	@Override
	public void initialize(FeaturesRunner runner) throws Exception {

		File file = null;
		FileInputStream fileInput = null;
		try {
			file = FileUtils.getResourceFileFromContext(TEST_CONF_FILE);
			fileInput = new FileInputStream(file);
			System.out.println("fileInput is null?: " + fileInput == null);
			props = new Properties();
			props.load(fileInput);

		} catch (Exception e) {
			props = null;
		} finally {
			if (fileInput != null) {
				try {
					fileInput.close();
				} catch (IOException e) {
					// Ignore
				}
				fileInput = null;
			}
		}

		// If the file was not there, try with environment variables
		if (props == null) {
			// Try to get environment variables
			addEnvironmentVariable(GOOGLE_CREDENTIALS_CONFIGURATION_PARAM);
			addEnvironmentVariable(GOOGLE_CREDENTIALS_TEST_PARAM);
		}

		filterValues();

		if (props != null) {
			Properties systemProps = System.getProperties();
			systemProps.setProperty(GOOGLE_CREDENTIALS_CONFIGURATION_PARAM,
					props.getProperty(GOOGLE_CREDENTIALS_CONFIGURATION_PARAM));
		}
	}

	@Override
	public void stop(FeaturesRunner runner) throws Exception {

		Properties p = System.getProperties();
		p.remove(GOOGLE_CREDENTIALS_TEST_PARAM);
		p.remove(GOOGLE_CREDENTIALS_CONFIGURATION_PARAM);
	}

	protected void filterValues() {
		if (props != null) {
			String value = props.getProperty(GOOGLE_CREDENTIALS_TEST_PARAM);
			if (value != null) {
				props.put(GOOGLE_CREDENTIALS_CONFIGURATION_PARAM, value);
			}
		}
	}

	protected void addEnvironmentVariable(String key) {
		String value = System.getenv(key);
		if (value != null) {
			if (props == null) {
				props = new Properties();
			}
			props.put(key, value);
		}
	}

}
