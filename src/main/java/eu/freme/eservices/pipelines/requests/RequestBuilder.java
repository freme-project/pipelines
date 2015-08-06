package eu.freme.eservices.pipelines.requests;

import eu.freme.conversion.rdf.RDFConstants;

import java.util.HashMap;
import java.util.Map;

import static eu.freme.conversion.rdf.RDFConstants.RDFSerialization.TURTLE;

/**
 * <p>To ease the creation of a request.</p>
 *
 * @author Gerald Haesendonck
 */
public class RequestBuilder {
	private RequestType requestType;
	private String service;		// the URI to the specific service, e.g. /e-entity/dbpedia-spotlight/documents
	private String baseEndpoint;
	private RDFConstants.RDFSerialization informat;
	private RDFConstants.RDFSerialization outformat;
	private String prefix;
	private String body;

	private final Map<String, Object> parameters;	// some extra app-specific parameters that will be added to the URI
	private final Map<String, String> headers;		// some extra app-specific headers.

	public RequestBuilder(final String serviceURI) {
		service = serviceURI;
		parameters = new HashMap<>(4);
		headers = new HashMap<>(2);

		// the default values:
		requestType = RequestType.POST;
		//baseEndpoint = "http://api.freme-project.eu/0.2";
		baseEndpoint = "http://api.freme-project.eu/current";
		informat = TURTLE;
		outformat = TURTLE;
		prefix = "http://freme-project.eu/";
	}

	/**
	 * Sets the request type.
	 * @param type	The type of HTTP request. Currently only GET and POST are supported; the default is POST.
	 * @return		A builder object with the request type set.
	 */
	public RequestBuilder type(RequestType type) {
		requestType = type;
		return this;
	}

	/**
	 * Sets the base endpoint.
	 * @param baseEndpointURI	The URI to the base URI of the services endpoint. Defaults to http://api.freme-project.eu/0.2
	 * @return	                A builder object with the base endpoint set.
	 */
	public RequestBuilder baseEndpoint(final String baseEndpointURI) {
		this.baseEndpoint = baseEndpoint;
		return this;
	}

	/**
	 * Sets the format of the input.
	 * @param informat	The input format. The default is TURTLE.
	 * @return A builder object with the informat set.
	 */
	public RequestBuilder informat(final RDFConstants.RDFSerialization informat) {
		this.informat = informat;
		return this;
	}

	/**
	 * Sets the format of the output.
	 * @param outformat	The output format. The default is TURTLE.
	 * @return A builder object with the outformat set.
	 */
	public RequestBuilder outformat(final RDFConstants.RDFSerialization outformat) {
		this.outformat = outformat;
		return this;
	}

	/**
	 * Sets the prefix.
	 * @param prefix     The url of rdf resources generated from plaintext. Has default value "http://freme-project.eu/".
	 * @return           A builder object with the prefix set.
	 */
	public RequestBuilder prefix(final String prefix) {
		this.prefix = prefix;
		return this;
	}

	/**
	 * Sets the body of the request.
	 * @param body 	The body of the request. The default is null, which is treated as an empty body.
	 * @return  	A builder object with the body set.
	 */
	public RequestBuilder body(final String body) {
		this.body = body;
		return this;
	}

	/**
	 * Sets a parameter.
	 * @param name	The name of the parameter
	 * @param value The value of the parameter
	 * @return		A builder with the parameter set.
	 */
	public RequestBuilder parameter(final String name, final String value) {
		parameters.put(name, value);
		return this;
	}

	/**
	 * Sets a header.
	 * @param name	The name of the header
	 * @param value	The value of the header
	 * @return  	A builder with the header set.
	 */
	public RequestBuilder header(final String name, final String value) {
		headers.put(name, value);
		return this;
	}

	/**
	 * Create a SerializedRequest instance with the given values.
	 * @return	A brand new request!
	 */
	public SerializedRequest build() {
		String serviceEndpoint = baseEndpoint + service;
		header("Content-Type", informat.contentType());
		header("Accept", outformat.contentType());
		parameter("prefix", prefix);
		return new SerializedRequest(requestType, serviceEndpoint, parameters, headers, body);
	}

}
