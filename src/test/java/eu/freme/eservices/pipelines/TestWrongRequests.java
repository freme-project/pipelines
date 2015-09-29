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

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.conversion.rdf.RDFConstants;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Gerald Haesendonck
 */
public class TestWrongRequests extends LocalServerTestBase {

	/**
	 * Send a pipelines request without body. This is not acceptable!
	 * @throws UnirestException
	 */
	@Test
	public void testNoBody() throws UnirestException {
		HttpResponse<String> response = Unirest.post("http://localhost:9000/pipelining/chain")
				.header("content-type", RDFConstants.RDFSerialization.JSON.contentType())
				.asString();
		assertEquals(HttpStatus.SC_NOT_ACCEPTABLE, response.getStatus());
	}

}
