package eu.freme.eservices.pipelines.requests;

import com.google.gson.Gson;
import eu.freme.conversion.rdf.RDFConstants;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Makes creating requests less painful. It returns default requests for services that can be modified afterwards.</p>
 * @author Gerald Haesendonck
 */
public class RequestFactory {
	private final static Gson gson = new Gson();

	/**
	 * Creates a default request to the e-Entity Spotlight service.
	 * @param text		The text to enrich (plain text).
	 * @param language	The language the text is in.
	 * @return			A request for e-Entity Spotlight that can still be modified.
	 */
	public static SerializedRequest createEntitySpotlight(final String text, final String language) {
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_ENTITY_SPOTLIGHT.getUri());
		return builder
				.informat(RDFConstants.RDFSerialization.PLAINTEXT)
				.parameter("language", language)
				.body(text).build();
	}

	/**
	 * Creates a default request to the e-Entity FREME NER service.
	 * @param text		The text to enrich (plain text).
	 * @param language  The language the text is in.
	 * @param dataSet   The data set to use for enrichment.
	 * @return        	A request for e-Entity FREME NER that can still be modified.
	 */
	public static SerializedRequest createEntityFremeNER(final String text, final String language, final String dataSet) {
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_ENTITY_FREME_NER.getUri());
		return builder
				.informat(RDFConstants.RDFSerialization.PLAINTEXT)
				.parameter("language", language)
				.parameter("dataset", dataSet)
				.body(text)
				.build();
	}

	/**
	 * Creates a default request to the e-Link service.
	 * @param templateID	The template ID to use for linking.
	 * @return				A request for e-Link that can still be modified
	 */
	public static SerializedRequest createLink(final String templateID) {
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_LINK.getUri());
		return builder
				.parameter("templateid", templateID)
				.build();
	}

	/**
	 * Converts a list of requests to JSON.
	 * @param requests	The list of requests to convert to JSON.
	 * @return			A JSON string representing serialized requests, which can be sent to the Pipelines API.
	 */
	public static String toJson(final List<SerializedRequest> requests) {
		return gson.toJson(requests);
	}

	/**
	 * Converts a JSON string to a list of requests.
	 * @param serializedRequests	The JSON string of requests to convert.
	 * @return						The list of requests represented by the JSON string.
	 */
	public static List<SerializedRequest> fromJson(final String serializedRequests) {
		SerializedRequest[] requests = gson.fromJson(serializedRequests, SerializedRequest[].class);
		return Arrays.asList(requests);
	}
}
