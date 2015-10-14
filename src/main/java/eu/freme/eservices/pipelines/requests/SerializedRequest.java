/**
 * Copyright (C) 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains the data to form a HTTP request.
 *
 * @author Gerald Haesendonck
 */
public class SerializedRequest {
	private HttpMethod method;
	private String endpoint;
	private Map<String, Object> parameters;
	private Map<String, String> headers;
	private String body;

	/**
	 * Creates a single request for usage in the pipelines service.
	 * Use the {@link RequestFactory} or {@link RequestBuilder} to create requests.
	 * @param method			The method of the request. Can be {@code GET} or {@code POST}.
	 * @param endpoint	    The URI to send te request to. In other words, the service endpoint.
	 * @param parameters	URL parameters to add to the request.
	 * @param headers		HTTP headers to add to the request.
	 * @param body			HTTP body to add to the request. Makes only sense when method is {@code POST}, but it's possible.
	 */
	SerializedRequest(HttpMethod method, String endpoint, Map<String, Object> parameters, Map<String, String> headers, String body) {
		this.method = method;
		this.endpoint = endpoint;
		this.parameters = parameters;
		this.headers = new HashMap<>(headers.size(), 1);

		// convert header names to lowercase (not their values). This is important for further processing...
		for (Map.Entry<String, String> header2value : headers.entrySet()) {
			this.headers.put(header2value.getKey().toLowerCase(), header2value.getValue());
		}
		this.body = body;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}

	public String isValid() {
		if (endpoint == null) {
			return "No endpoint given.";
		}
		if (method == null) {
			return "HTTP Method not supported. Only GET and POST are supported.";
		}
		try {
			new URL(endpoint);
		} catch (MalformedURLException e) {
			return e.getMessage();
		}
		return "";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SerializedRequest request = (SerializedRequest) o;

		if (method != request.method) return false;
		if (!endpoint.equals(request.endpoint)) return false;
		if (parameters != null ? !parameters.equals(request.parameters) : request.parameters != null) return false;
		if (headers != null ? !headers.equals(request.headers) : request.headers != null) return false;
		return !(body != null ? !body.equals(request.body) : request.body != null);

	}

	@Override
	public int hashCode() {
		int result = method.hashCode();
		result = 31 * result + endpoint.hashCode();
		result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
		result = 31 * result + (headers != null ? headers.hashCode() : 0);
		result = 31 * result + (body != null ? body.hashCode() : 0);
		return result;
	}
}
