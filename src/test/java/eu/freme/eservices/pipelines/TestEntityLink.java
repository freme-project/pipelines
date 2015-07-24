package eu.freme.eservices.pipelines;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

/**
 * @author Gerald Haesendonck
 */
public class TestEntityLink extends LocalServerTestBase {

	@Test
	public void test() throws UnirestException {
		HttpResponse<String> response = Unirest.post("http://0.0.0.0:8080/pipelining").asString();
		System.out.println("response.getStatus() = " + response.getStatus());
		System.out.println("response.getStatusText() = " + response.getStatusText());
		System.out.println("response.getBody() = " + response.getBody());
	}
}
