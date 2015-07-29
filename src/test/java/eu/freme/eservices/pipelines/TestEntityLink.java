package eu.freme.eservices.pipelines;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.eservices.pipelines.requests.RequestType;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gerald Haesendonck
 */
public class TestEntityLink extends LocalServerTestBase {
	private final Gson gson = new Gson();

	@Test
	public void test() throws UnirestException, IOException {
		String data = "A court in Libya has sentenced Saif al-Islam Gaddafi, son of deposed leader Col Muammar Gaddafi, and eight others to death over war crimes linked to the 2011 revolution.";
		SerializedRequest entityRequest = createEntityRequestSpotlight(data);
		SerializedRequest[] serializedRequests = {entityRequest};

		HttpResponse<String> response = Unirest.post("http://localhost:8080/pipelining/chain")
				.body(new JsonNode(gson.toJson(serializedRequests)))
				.asString();
		System.out.println("response.getStatus() = " + response.getStatus());
		System.out.println("response.getStatusText() = " + response.getStatusText());
		System.out.println("response.getBody() = " + response.getBody());
		Unirest.shutdown();
	}

	private SerializedRequest createEntityRequestSpotlight(final String textToEnrich) {

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("informat", "text");
		parameters.put("outformat", "turtle");
		parameters.put("language", "en");
		parameters.put("input", textToEnrich);

		return new SerializedRequest(
				RequestType.POST,
				"http://api.freme-project.eu/0.2/e-entity/dbpedia-spotlight/documents",
				parameters,
				null,
				null
		);
	}
}
