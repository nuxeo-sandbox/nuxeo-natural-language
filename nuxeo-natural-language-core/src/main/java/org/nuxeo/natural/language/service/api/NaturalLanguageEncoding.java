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

import org.apache.commons.lang.StringUtils;

/**
 * Defines the encoding of a text passed to the API.
 *
 * NOTE: Using the same numerical values as
 * com.google.cloud.language.v1.EncodingType
 *
 * @since 9.2
 */
public enum NaturalLanguageEncoding {
	UTF8(1), UTF16(2), UTF32(3);

	private final int value;

	private NaturalLanguageEncoding(int value) {
		this.value = value;
	}

	public static NaturalLanguageEncoding fromString(String value) {
		if (StringUtils.isEmpty(value)) {
			throw new java.lang.IllegalArgumentException("Can't get the value from a null or empty string.");
		}
		switch (value) {
		case "UTF8":
		case "utf8":
		case "UTF-8":
		case "utf-8":
			return UTF8;

		case "UTF16":
		case "utf16":
		case "UTF-16":
		case "utf-16":
			return UTF16;

		case "UTF32":
		case "utf32":
		case "UTF-32":
		case "utf-32":
			return UTF32;

		default:
			throw new java.lang.IllegalArgumentException("Can't get the value from a null or empty string.");
		}
	}

	@Override
	public String toString() {
		return name();
	}

	public final int getNumber() {
		return value;
	}
}
