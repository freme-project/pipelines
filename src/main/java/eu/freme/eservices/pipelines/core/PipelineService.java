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
package eu.freme.eservices.pipelines.core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.conversion.rdf.RDFSerializationFormats;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import eu.freme.i18n.api.EInternationalizationAPI;
import eu.freme.i18n.okapi.nif.converter.ConversionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gerald Haesendonck
 */
public class PipelineService {
	private final static Logger logger = LoggerFactory.getLogger(PipelineService.class);

	@Autowired
	private RDFSerializationFormats serializationFormats;

	@Autowired
	private EInternationalizationAPI eInternationalizationApi;

	/**
	 * Performs a chain of requests to other e-services (pipeline).
	 * @param serializedRequests  Requests to different services, serialized in JSON.
	 * @return                    The result of the pipeline.
	 */
	@SuppressWarnings("unused")
	public WrappedPipelineResponse chain(final List<SerializedRequest> serializedRequests) throws IOException, UnirestException, ServiceException {
		Map<String, Long> executionTime = new LinkedHashMap<>();

		// determine mime types of first and last pipeline request
		Conversion conversion = null;
		boolean roundtrip = false; // true: convert HTML input to NIF, execute pipeline, convert back to HTML at the end.
		if (serializedRequests.size() > 1) {
			RDFConstants.RDFSerialization mime1 = serializedRequests.get(0).getInputMime(serializationFormats);
			RDFConstants.RDFSerialization mime2 = serializedRequests.get(serializedRequests.size() - 1).getOutputMime(serializationFormats);
			if (mime1.equals(RDFConstants.RDFSerialization.HTML) && mime1.equals(mime2)) {
				roundtrip = true;
				conversion = new Conversion(eInternationalizationApi);
				try {
					long startOfRequest = System.currentTimeMillis();
					String nif = conversion.htmlToNif(serializedRequests.get(0).getBody());
					executionTime.put("e-Internationalization (HTML -> NIF)", (System.currentTimeMillis() - startOfRequest));
					serializedRequests.get(0).setBody(nif);
				} catch (ConversionException e) {
					logger.warn("Could not convert the HTLM contents to NIF. Tying to proceed without converting... Error: ", e);
					roundtrip = false;
				}
			}
		}

		PipelineResponse lastResponse = new PipelineResponse(serializedRequests.get(0).getBody(), null);
		long start = System.currentTimeMillis();
		for (int reqNr = 0; reqNr < serializedRequests.size(); reqNr++) {
			long startOfRequest = System.currentTimeMillis();
			SerializedRequest serializedRequest = serializedRequests.get(reqNr);
			try {
				if (roundtrip) {
					serializedRequest.getHeaders().put("content-type", RDFConstants.RDFSerialization.TURTLE.contentType());
					serializedRequest.getHeaders().put("accept", RDFConstants.RDFSerialization.TURTLE.contentType());
					serializedRequest.getParameters().remove("informat");
					serializedRequest.getParameters().remove("f");
					serializedRequest.getParameters().remove("outformat");
					serializedRequest.getParameters().remove("o");
				}
				lastResponse = execute(serializedRequest, lastResponse.getBody());
			} catch (UnirestException e) {
				throw new UnirestException("Request " + reqNr + ": " + e.getMessage());
			} catch (IOException e) {
				throw new IOException("Request " + reqNr + ": " + e.getMessage());
			} finally {
				long endOfRequest = System.currentTimeMillis();
				executionTime.put(serializedRequest.getEndpoint(), (endOfRequest - startOfRequest));
			}
		}
		if (roundtrip) {
			long startOfRequest = System.currentTimeMillis();
			String html = conversion.nifToHtml(lastResponse.getBody());
			lastResponse = new PipelineResponse(html, RDFConstants.RDFSerialization.HTML.contentType());
			executionTime.put("e-Internationalization (NIF -> HTML)", (System.currentTimeMillis() - startOfRequest));
		}
		long end = System.currentTimeMillis();
		return new WrappedPipelineResponse(lastResponse, executionTime, (end - start));
	}

	private PipelineResponse execute(final SerializedRequest request, final String body) throws UnirestException, IOException, ServiceException {
		switch (request.getMethod()) {
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
						throw new ServiceException(new PipelineResponse( "The service \"" + request.getEndpoint() + "\" reported HTTP status " + status.toString() + ". No further explanation given by service.", RDFConstants.RDFSerialization.PLAINTEXT.contentType()), status);
					} else {
						throw new ServiceException(new PipelineResponse(errorBody, response.getHeaders().getFirst("content-type")), status);
					}
				}
				return new PipelineResponse(response.getBody(), response.getHeaders().getFirst("content-type"));
		}
	}
}
