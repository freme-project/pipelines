package eu.freme.eservices.pipelines;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.eservices.pipelines.requests.RequestFactory;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Gerald Haesendonck
 */
public class TestEntityLinkTranslation extends LocalServerTestBase {

	@Test
	public void testSomethingThatWorks() throws UnirestException {
		String data = "The Belfry in Ghent is one of the oldest buildings in Belgium.";
		SerializedRequest entityRequest = RequestFactory.createEntitySpotlight(data, "en");
		SerializedRequest linkRequest = RequestFactory.createLink("3");	// Geo pos
		SerializedRequest translateRequest = RequestFactory.createTranslation("en", "fr");

		HttpResponse<String> response = sendRequest(entityRequest, linkRequest, translateRequest);
		System.out.println("response.getStatus() = " + response.getStatus());
		assertEquals("Wrong status code", 200, response.getStatus());
		System.out.println("response.getStatusText() = " + response.getStatusText());
		System.out.println("response.getBody() = " + response.getBody());
	}
}
