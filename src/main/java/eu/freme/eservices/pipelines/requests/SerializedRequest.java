package eu.freme.eservices.pipelines.requests;

import java.util.Map;

/**
 * This class contains the data to form a HTTP request.
 *
 * @author Gerald Haesendonck
 */
public class SerializedRequest {
	private RequestType type;
	private String endpoint;
	private Map<String, String> parameters;
	private Map<String, String> headers;
	private String body;	// only textual format for now...

	public SerializedRequest(RequestType type, String endpoint, Map<String, String> parameters, Map<String, String> headers, String body) {
		this.type = type;
		this.endpoint = endpoint;
		this.parameters = parameters;
		this.headers = headers;
		this.body = body;
	}

	public RequestType getType() {
		return type;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getBody() {
		return body;
	}
}
