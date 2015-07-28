package eu.freme.eservices.pipelines.core;

import eu.freme.eservices.pipelines.requests.SerializedRequest;

/**
 * @author Gerald Haesendonck
 */
public class PipelineService {

	/**
	 * Performs a chain of requests to other e-services (pipeline).
	 * @param serializedRequests  Requests to different services, serialized in JSON.
	 * @return                    The result of the pipeline.
	 */
	public String chain(final SerializedRequest[] serializedRequests) {
		return "Hello world!";
	}
}
