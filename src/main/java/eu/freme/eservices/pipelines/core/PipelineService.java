package eu.freme.eservices.pipelines.core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import org.apache.http.HttpStatus;

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
	public String chain(final List<SerializedRequest> serializedRequests) throws IOException, UnirestException {
		String body = serializedRequests.get(0).getBody();
		for (SerializedRequest serializedRequest : serializedRequests) {
			body = execute(serializedRequest, body);
		}
		return body;
	}

	private String execute(final SerializedRequest request, final String body) throws UnirestException, IOException {
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
				if (response.getStatus() != HttpStatus.SC_OK) {    // TODO: check for other status that could be OK
					throw new IOException("Request to " + request.getEndpoint() + " failed. Response from server: " + response.getStatusText());
					// TODO:
				}
				return response.getBody();
		}
	}
}
