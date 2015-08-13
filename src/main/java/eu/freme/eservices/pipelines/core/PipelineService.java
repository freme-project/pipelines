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
package eu.freme.eservices.pipelines.core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import eu.freme.conversion.rdf.RDFConstants;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Gerald Haesendonck
 */
public class PipelineService {

	/**
	 * Performs a chain of requests to other e-services (pipeline).
	 * @param serializedRequests  Requests to different services, serialized in JSON.
	 * @return                    The result of the pipeline.
	 */
	public String chain(final List<SerializedRequest> serializedRequests) throws IOException, UnirestException, ServiceException {
		String body = serializedRequests.get(0).getBody();
		for (SerializedRequest serializedRequest : serializedRequests) {
			body = execute(serializedRequest, body);
		}
		return body;
	}

	private String execute(final SerializedRequest request, final String body) throws UnirestException, IOException, ServiceException {
		switch (request.getType()) {
			case GET:
				throw new UnsupportedOperationException("GET is not supported at this moment.");
			default:
				HttpRequestWithBody req = Unirest.post(request.getEndpoint());
				if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
					req.headers(request.getHeaders());
				}
				if (request.getParameters() != null && !request.getParameters().isEmpty()) {
					req.queryString(request.getParameters());	// encode as POST parameters
				}

				HttpResponse<String> response;
				if (body != null) {
					 response = req.body(body).asString();
				} else {
					response = req.asString();
				}
				if (!HttpStatus.Series.valueOf(response.getStatus()).equals(HttpStatus.Series.SUCCESSFUL)) {
					String errorBody = response.getBody();
					HttpStatus status = HttpStatus.valueOf(response.getStatus());
					if (errorBody == null || errorBody.isEmpty()) {
						errorBody = "No reason given by service.";
					}
					throw new ServiceException(errorBody, status);
				}
				return response.getBody();
		}
	}

	/**
	 * Helper method that returns the content type of the response of the last request (or: the value of the 'accept'
	 * header of the last request).
	 * @param serializedRequests	The requests that (will) serve as input for the pipelining service.
	 * @return						The content type of the response that the service will return.
	 */
	public static RDFConstants.RDFSerialization getContentTypeOfLastResponse(final List<SerializedRequest> serializedRequests) {
		String contentType = "";
		if (!serializedRequests.isEmpty()) {
			SerializedRequest lastRequest = serializedRequests.get(serializedRequests.size() - 1);
			Map<String, String> headers = lastRequest.getHeaders();
			if (headers.containsKey("accept")) {
				contentType = headers.get("accept");
			} else {
				Map<String, Object> parameters = lastRequest.getParameters();
				if (parameters.containsKey("outformat")) {
					contentType = parameters.get("outformat").toString();
				}
			}
		}
		RDFConstants.RDFSerialization serialization = RDFConstants.RDFSerialization.fromValue(contentType);
		return serialization != null ? serialization : RDFConstants.RDFSerialization.TURTLE;
	}
}
