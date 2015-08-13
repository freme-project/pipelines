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

import java.util.Map;

/**
 * This class contains the data to form a HTTP request.
 *
 * @author Gerald Haesendonck
 */
public class SerializedRequest {
	private RequestType type;
	private String endpoint;
	private Map<String, Object> parameters;
	private Map<String, String> headers;
	private String body;	// only textual format for now...

	public SerializedRequest(RequestType type, String endpoint, Map<String, Object> parameters, Map<String, String> headers, String body) {
		this.type = type;
		this.endpoint = endpoint;
		this.parameters = parameters;
		this.headers = headers;
		this.body = body;
	}

	public RequestType getType() {
		return type;
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
}
