package eu.freme.eservices.pipelines.requests;

import com.google.gson.Gson;
import eu.freme.conversion.rdf.RDFConstants;

import java.util.List;

/**
 * <p>Makes creating requests less painful. It returns default requests for services that can be modified afterwards.</p>
 * @author Gerald Haesendonck
 */
public class RequestFactory {
	private final static Gson gson = new Gson();

	/**
	 * Creates a default request to the e-Entity Spotlight service.
	 * @param text		The text to enrich.
	 * @param language	The language the text is in.
	 * @return			A request that can still be modified.
	 */
	public static SerializedRequest createEntitySpotlight(final String text, final String language) {
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_ENTITY_SPOTLIGHT.getUri());
		return builder
				.informat(RDFConstants.RDFSerialization.PLAINTEXT)
				.parameter("language", language)
				.body(text).build();
	}

	public static String toJson(final List<SerializedRequest> requests) {
		return gson.toJson(requests);
	}
}
