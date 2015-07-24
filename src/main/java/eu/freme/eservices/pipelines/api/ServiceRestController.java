package eu.freme.eservices.pipelines.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Gerald Haesendonck
 */
@RestController
@SuppressWarnings("unused")
public class ServiceRestController {

	@Autowired
	PipelineService pipelineAPI;

	@RequestMapping(value = "/pipelining", method = RequestMethod.POST)
	public ResponseEntity<String> pipeline(/*@RequestParam("htmlZip") MultipartFile file, @RequestParam("metadata") String jMetadata*/) throws IOException {
		//Gson gson = new Gson();
		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");
		return new ResponseEntity<>(pipelineAPI.aMethod(), headers, HttpStatus.OK);
	}
}