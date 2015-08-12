package eu.freme.eservices.pipelines;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.eservices.pipelines.requests.RequestFactory;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import org.junit.After;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

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
	 * @param requests		The serialized requests to send.
	 * @return				The result of the request. This can either be the result of the pipelined requests, or an
	 *                      error response with some explanation what went wrong in the body.
	 * @throws UnirestException
	 */
	protected HttpResponse<String> sendRequest(SerializedRequest... requests) throws UnirestException {
		List<SerializedRequest> serializedRequests = Arrays.asList(requests);
		return Unirest.post("http://localhost:9000/pipelining/chain")
				.body(new JsonNode(RequestFactory.toJson(serializedRequests)))
				.asString();
	}
}
