package eu.freme.eservices.pipelines;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.eservices.pipelines.requests.RequestFactory;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
/**
 * @author Gerald Haesendonck
 */
public class TestEntityLink extends LocalServerTestBase {

	@Test
	public void testSpotlight() throws UnirestException, IOException {
		//String data = "A court in Libya has sentenced Saif al-Islam Gaddafi, son of deposed leader Col Muammar Gaddafi, and eight others to death over war crimes linked to the 2011 revolution.";
		String data = "This summer there is the Zomerbar in Antwerp, one of the most beautiful cities in Belgium.";
		SerializedRequest entityRequest = RequestFactory.createEntitySpotlight(data, "en");
		SerializedRequest linkRequest = RequestFactory.createLink("3");	// Geo pos

		List<SerializedRequest> serializedRequests = Arrays.asList(entityRequest, linkRequest);

		HttpResponse<String> response = Unirest.post("http://localhost:9000/pipelining/chain")
				.body(new JsonNode(RequestFactory.toJson(serializedRequests)))
				.asString();
		System.out.println("response.getStatus() = " + response.getStatus());
		assertEquals("Wrong status code", 200, response.getStatus());
		System.out.println("response.getStatusText() = " + response.getStatusText());
		System.out.println("response.getBody() = " + response.getBody());
		Unirest.shutdown();
	}

	@Test
	public void testFremeNER() throws UnirestException, IOException {
		String data = "This summer there is the Zomerbar in Antwerp, one of the most beautiful cities in Belgium.";
		SerializedRequest linkRequest = RequestFactory.createLink("3");	// Geo pos

		SerializedRequest entityRequest = RequestFactory.createEntityFremeNER(data, "en", "viaf");
		List<SerializedRequest> serializedRequests = Arrays.asList(entityRequest, linkRequest);

		HttpResponse<String> response = Unirest.post("http://localhost:9000/pipelining/chain")
				.body(new JsonNode(RequestFactory.toJson(serializedRequests)))
				.asString();
		System.out.println("response.getStatus() = " + response.getStatus());
		assertEquals("Wrong status code", 200, response.getStatus());
		System.out.println("response.getStatusText() = " + response.getStatusText());
		System.out.println("response.getBody() = " + response.getBody());
		Unirest.shutdown();
	}
}
