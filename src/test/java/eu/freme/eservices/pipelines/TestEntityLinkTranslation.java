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
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.eservices.pipelines.requests.RequestFactory;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import org.apache.http.HttpStatus;
import org.junit.Test;

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

		HttpResponse<String> response = sendRequest(HttpStatus.SC_OK, entityRequest, linkRequest, translateRequest);
		System.out.println("response.getStatus() = " + response.getStatus());
		System.out.println("response.getStatusText() = " + response.getStatusText());
		System.out.println("response.getBody() = " + response.getBody());
	}
}
