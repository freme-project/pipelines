/**
 * Copyright (C) 2015 Deutsches Forschungszentrum für Künstliche Intelligenz (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.eservices.pipelines.requests;

import com.google.gson.Gson;
import eu.freme.conversion.rdf.RDFConstants;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Makes creating requests less painful. It returns default requests for services that can be modified afterwards.</p>
 * @author Gerald Haesendonck
 */
public class RequestFactory {
	private final static Gson gson = new Gson();

	/**
	 * Creates a default request to the e-Entity Spotlight service.
	 * @param text		The text to enrich.
	 * @param language	The language the text is in.
	 * @return			A request that can still be modified.
	 */
	public static SerializedRequest createEntitySpotlight(final String text, final String language) {
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_ENTITY_SPOTLIGHT.getUri());
		return builder
				.informat(RDFConstants.RDFSerialization.PLAINTEXT)
				.parameter("language", language)
				.body(text).build();
	}

	public static String toJson(final List<SerializedRequest> requests) {
		return gson.toJson(requests);
	}

	public static List<SerializedRequest> fromJson(final String serializedRequests) {
		SerializedRequest[] requests = gson.fromJson(serializedRequests, SerializedRequest[].class);
		return Arrays.asList(requests);
	}
}
