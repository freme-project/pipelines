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
package eu.freme.eservices.pipelines;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.conversion.rdf.RDFConstants;
import eu.freme.eservices.pipelines.core.PipelineService;
import eu.freme.eservices.pipelines.requests.RequestFactory;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import org.junit.After;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Gerald Haesendonck
 */
public abstract class LocalServerTestBase {
	protected Run webService;

	@Before
	public void setup() {
		// server
		webService = new Run();
		webService.run();
	}

	@After
	public void tearDown() {
		webService.close();
	}

	/**
	 * Sends the actual pipeline request. It serializes the request objects to JSON and puts this into the body of
	 * the request.
	 * @param expectedResponseCode	The expected HTTP response code. Will be checked against.
	 * @param requests		The serialized requests to send.
	 * @return				The result of the request. This can either be the result of the pipelined requests, or an
	 *                      error response with some explanation what went wrong in the body.
	 * @throws UnirestException
	 */
	protected HttpResponse<String> sendRequest(int expectedResponseCode, SerializedRequest... requests) throws UnirestException {
		List<SerializedRequest> serializedRequests = Arrays.asList(requests);

		HttpResponse<String> response = Unirest.post("http://localhost:9000/pipelining/chain")
				.body(new JsonNode(RequestFactory.toJson(serializedRequests)))
				.asString();

		// print some response info
		System.out.println("response.getStatus() = " + response.getStatus());
		System.out.println("response.getStatusText() = " + response.getStatusText());
		System.out.println("response.getBody() = " + response.getBody());

		RDFConstants.RDFSerialization responseContentType = RDFConstants.RDFSerialization.fromValue(response.getHeaders().getFirst("content-type"));
		RDFConstants.RDFSerialization accept = PipelineService.getContentTypeOfLastResponse(serializedRequests);
		assertEquals(expectedResponseCode, response.getStatus());
		if (expectedResponseCode / 100 != 2) {
			assertEquals(RDFConstants.RDFSerialization.PLAINTEXT, responseContentType);
		} else {
			assertEquals(responseContentType, accept);
		}

		return response;
	}
}
