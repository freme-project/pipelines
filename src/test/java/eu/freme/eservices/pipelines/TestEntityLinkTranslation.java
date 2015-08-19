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

import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.eservices.pipelines.requests.RequestFactory;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import org.apache.http.HttpStatus;
import org.junit.Test;

/**
 * Some tests for the e-Entity, e-Link, e-Translate pipeline
 *
 * @author Gerald Haesendonck
 */
public class TestEntityLinkTranslation extends LocalServerTestBase {

	/**
	 * e-Entity using the Spotlight NER , e-Link using template 3 (Geo pos), and e-Translate en -> fr on one sentence.
	 * All should go well.
	 * @throws UnirestException
	 */
	@Test
	public void testSomethingThatWorks() throws UnirestException {
		String data = "The Belfry in Ghent is one of the oldest buildings in Belgium.";
		SerializedRequest entityRequest = RequestFactory.createEntitySpotlight(data, "en");
		SerializedRequest linkRequest = RequestFactory.createLink("3");	// Geo pos
		SerializedRequest translateRequest = RequestFactory.createTranslation("en", "fr");

		sendRequest(HttpStatus.SC_OK, entityRequest, linkRequest, translateRequest);
	}

	/**
	 * e-Entity using FREME NER, e-Link using template 3 (Geo pos), and e-Translate en -> nl on two paragraphs.
	 * All should go well.
	 * @throws UnirestException
	 */
	@Test
	public void testALongerText() throws UnirestException {
		String input = "With just 200,000 residents, Reykjavík ranks as one of Europe’s smallest capital cities. But when Iceland’s total population only hovers around 300,000, it makes sense that the capital is known as the “big city” and offers all the cultural perks of a much larger place.\n" +
				"\n" +
				"“From live music almost every night to cosy cafes, colourful houses and friendly cats roaming the street, Reykjavík has all the charms of a small town in a fun capital city,” said Kaelene Spence, ";
		SerializedRequest entityRequest = RequestFactory.createEntityFremeNER(input, "en", "dbpedia");
		SerializedRequest linkRequest = RequestFactory.createLink("3");	// Geo pos
		SerializedRequest translateRequest = RequestFactory.createTranslation("en", "nl");

		sendRequest(HttpStatus.SC_OK, entityRequest, linkRequest, translateRequest);
	}

	/**
	 * A non-existing language pair is given; the the response should give "not acceptable".
	 * @throws UnirestException
	 */
	@Test
	public void testWrongLanguagePair() throws UnirestException {
		String data = "The Belfry in Ghent is one of the oldest buildings in Belgium.";
		SerializedRequest entityRequest = RequestFactory.createEntitySpotlight(data, "en");
		SerializedRequest linkRequest = RequestFactory.createLink("3");	// Geo pos
		SerializedRequest translateRequest = RequestFactory.createTranslation("es", "pt");

		sendRequest(HttpStatus.SC_NOT_ACCEPTABLE, entityRequest, linkRequest, translateRequest);
	}
}
