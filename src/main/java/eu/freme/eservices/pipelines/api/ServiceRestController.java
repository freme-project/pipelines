/**
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.freme.eservices.pipelines.api;

import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.eservices.pipelines.core.PipelineService;
import eu.freme.eservices.pipelines.requests.RequestFactory;
import eu.freme.eservices.pipelines.requests.SerializedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author Gerald Haesendonck
 */
@RestController
@SuppressWarnings("unused")
public class ServiceRestController {

	@Autowired
	PipelineService pipelineAPI;

	@RequestMapping(value = "/pipelining/chain", method = RequestMethod.POST)
	public ResponseEntity<String> pipeline(@RequestBody String requests) throws IOException, UnirestException {
		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");
		List<SerializedRequest> serializedRequests = RequestFactory.fromJson(requests);
		return new ResponseEntity<>(pipelineAPI.chain(serializedRequests), headers, HttpStatus.OK);
	}
}