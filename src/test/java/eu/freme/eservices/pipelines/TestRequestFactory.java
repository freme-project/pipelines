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
package eu.freme.eservices.pipelines;

import com.google.gson.JsonSyntaxException;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import eu.freme.eservices.pipelines.serialization.Serializer;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;

/**
 * @author Gerald Haesendonck
 */
public class TestRequestFactory {

	@Test
	public void testWrongRequestInJSON() {
		// POST = POS!
		String wrongRequest =
				"[\n" +
						"    {\n" +
						"        \"method\": \"POS\",\n" +
						"        \"endpoint\": \"http://api.freme-project.eu/current/e-entity/dbpedia-spotlight/documents\",\n" +
						"        \"parameters\": {\n" +
						"            \"language\": \"en\",\n" +
						"            \"prefix\": \"http://freme-project.eu/\"\n" +
						"        },\n" +
						"        \"headers\": {\n" +
						"            \"content-method\": \"text/plain\",\n" +
						"            \"accept\": \"text/turtle\"\n" +
						"        },\n" +
						"        \"body\": \"This summer there is the Zomerbar in Antwerp, one of the most beautiful cities in Belgium.\"\n" +
						"    }]";
		try {
			List<SerializedRequest> requests = Serializer.fromJson(wrongRequest);
			fail("A JsonSyntaxException is expected, but all went well (which is wrong!)"); // should throw exception
		} catch (JsonSyntaxException e) {
			// very good!
			System.out.println("Test succeeded. Error msg: " + e.getMessage());
		}
	}
}
