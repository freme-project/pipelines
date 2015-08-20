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

/**
 * @author Gerald Haesendonck
 */
public class PipelineService {

	/**
	 * Performs a chain of requests to other e-services (pipeline).
	 * @param serializedRequests  Requests to different services, serialized in JSON.
	 * @return                    The result of the pipeline.
	 */
	public PipelineResponse chain(final List<SerializedRequest> serializedRequests) throws IOException, UnirestException, ServiceException {
		PipelineResponse lastResponse = new PipelineResponse(serializedRequests.get(0).getBody(), null);
		for (int reqNr = 0; reqNr < serializedRequests.size(); reqNr++) {
			SerializedRequest serializedRequest = serializedRequests.get(reqNr);
			try {
				lastResponse = execute(serializedRequest, lastResponse.getBody());
			} catch (UnirestException e) {
				throw new UnirestException("Request " + reqNr + ": " + e.getMessage());
			}
		}
		return lastResponse;
	}

	private PipelineResponse execute(final SerializedRequest request, final String body) throws UnirestException, IOException, ServiceException {
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
						throw new ServiceException(new PipelineResponse( "No reason given by service.", RDFConstants.RDFSerialization.PLAINTEXT.contentType()), status);
					} else {
						throw new ServiceException(new PipelineResponse(errorBody, response.getHeaders().getFirst("content-type")), status);
					}
				}
				return new PipelineResponse(response.getBody(), response.getHeaders().getFirst("content-type"));
		}
	}
}
