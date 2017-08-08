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
 *     Thibaud Arguillere
 */

package org.nuxeo.natural.language.google;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.NuxeoException;

import org.nuxeo.natural.language.service.NaturalLanguageFeature;
import org.nuxeo.natural.language.service.NaturalLanguageProvider;
import org.nuxeo.natural.language.service.NaturalLanguageResponse;

public class GoogleNaturalLanguageProvider implements NaturalLanguageProvider {

    public static final String APP_NAME_PARAM = "appName";

    public static final String API_KEY_PARAM = "apiKey";

    public static final String CREDENTIAL_PATH_PARAM = "credentialFilePath";

    protected Map<String, String> params;

    public GoogleNaturalLanguageProvider(Map<String, String> parameters) {
        this.params = parameters;
    }

    /**
     * volatile on purpose to allow for the double-checked locking idiom
     */
    //protected volatile com.google.api.services.vision.v1.Vision visionClient;

    @Override
    public List<NaturalLanguageResponse> processBlobs(List<Blob> blobs, List<NaturalLanguageFeature> features, int maxResults)
            throws IOException, GeneralSecurityException, IllegalStateException {
        return null;
    }

    @Override
    public List<NaturalLanguageFeature> getSupportedFeatures() {
        return null;
    }

    @Override
    public boolean checkBlobs(List<Blob> blobs) throws IOException {
        return false;
    }

    @Override
    public Object getNativeClient() {
        return null;
    }

}
