package eu.freme.eservices.pipelines;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.eservices.pipelines.requests.RequestFactory;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
/**
 * Some tests for the e-Entity, e-Link pipeline
 *
 * @author Gerald Haesendonck
 */
public class TestEntityLink extends LocalServerTestBase {

	/**
	 * e-Entity using the Spotlight NER and e-Link using template 3 (Geo pos). All should go well.
	 * @throws UnirestException
	 * @throws IOException
	 */
	@Test
	public void testSpotlight() throws UnirestException, IOException {
		//String data = "A court in Libya has sentenced Saif al-Islam Gaddafi, son of deposed leader Col Muammar Gaddafi, and eight others to death over war crimes linked to the 2011 revolution.";
		String data = "This summer there is the Zomerbar in Antwerp, one of the most beautiful cities in Belgium.";
		SerializedRequest entityRequest = RequestFactory.createEntitySpotlight(data, "en");
		SerializedRequest linkRequest = RequestFactory.createLink("3");	// Geo pos

		HttpResponse<String> response = sendRequest(entityRequest, linkRequest);
		System.out.println("response.getStatus() = " + response.getStatus());
		assertEquals("Wrong status code", 200, response.getStatus());
		System.out.println("response.getStatusText() = " + response.getStatusText());
		System.out.println("response.getBody() = " + response.getBody());
	}

	/**
	 * e-Entity using FREME NER with database viaf and e-Link using template 3 (Geo pos). All should go well.
	 * @throws UnirestException
	 * @throws IOException
	 */
	@Test
	public void testFremeNER() throws UnirestException, IOException {
		String data = "This summer there is the Zomerbar in Antwerp, one of the most beautiful cities in Belgium.";
		SerializedRequest entityRequest = RequestFactory.createEntityFremeNER(data, "en", "viaf");
		SerializedRequest linkRequest = RequestFactory.createLink("3");	// Geo pos

		HttpResponse<String> response = sendRequest(entityRequest, linkRequest);
		System.out.println("response.getStatus() = " + response.getStatus());
		assertEquals("Wrong status code", 200, response.getStatus());
		System.out.println("response.getStatusText() = " + response.getStatusText());
		System.out.println("response.getBody() = " + response.getBody());
	}

	/**
	 * e-Entity using an unexisting data set to test error reporting.
	 */
	@Test
	@Ignore
	public void testWrongDatasetEntity() throws IOException, UnirestException {
		String data = "This summer there is the Zomerbar in Antwerp, one of the most beautiful cities in Belgium.";
		SerializedRequest entityRequest = RequestFactory.createEntityFremeNER(data, "en", "anunexistingdatabase");
		SerializedRequest linkRequest = RequestFactory.createLink("3");	// Geo pos

		HttpResponse<String> response = sendRequest(entityRequest, linkRequest);
		System.out.println("response.getStatus() = " + response.getStatus());
		assertEquals("Wrong status code (see issue #29 of e-Entity)", 400, response.getStatus());
		System.out.println("response.getStatusText() = " + response.getStatusText());
		System.out.println("response.getBody() = " + response.getBody());
	}

	/**
	 * e-Entity using an unexisting language set to test error reporting.
	 */
	@Test
	public void testWrongLanguageEntity() throws IOException, UnirestException {
		String data = "This summer there is the Zomerbar in Antwerp, one of the most beautiful cities in Belgium.";
		SerializedRequest entityRequest = RequestFactory.createEntityFremeNER(data, "zz", "viaf");
		SerializedRequest linkRequest = RequestFactory.createLink("3");	// Geo pos

		HttpResponse<String> response = sendRequest(entityRequest, linkRequest);
		System.out.println("response.getStatus() = " + response.getStatus());
		assertEquals("Wrong status code", 400, response.getStatus());
		System.out.println("response.getStatusText() = " + response.getStatusText());
		System.out.println("response.getBody() = " + response.getBody());
	}
}
