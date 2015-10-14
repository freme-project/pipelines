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
package eu.freme.eservices.pipelines.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.eservices.pipelines.serialization.Pipeline;

import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>Makes creating requests less painful. It returns default requests for services that can be modified afterwards.</p>
 * @author Gerald Haesendonck
 */
public class RequestFactory {
	private final static Gson gson = new Gson();
	private final static Gson gson_pretty = new GsonBuilder().setPrettyPrinting().create();
	private static Set<String> requestFieldNames = new HashSet<>(5, 1);

	static {
		Class<SerializedRequest> src = SerializedRequest.class;
		for (Field field : src.getDeclaredFields()) {
			requestFieldNames.add(field.getName());
		}
	}

	/**
	 * Creates a default request to the e-Entity Spotlight service.
	 * @param text		The text to enrich (plain text).
	 * @param language	The language the text is in.
	 * @return			A request for e-Entity Spotlight.
	 */
	public static SerializedRequest createEntitySpotlight(final String text, final String language) {
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_ENTITY_SPOTLIGHT.getUri());
		return builder
				.informat(RDFConstants.RDFSerialization.PLAINTEXT)
				.parameter("language", language)
				.body(text).build();
	}

	/**
	 * Creates a default request to the e-Entity Spotlight service without input. This is used when the input is the output of
	 * another request.
	 * @param language	The language the text is in.
	 * @return			A request for e-Entity Spotlight.
	 */
	public static SerializedRequest createEntitySpotlight(final String language) {
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_ENTITY_SPOTLIGHT.getUri());
		return builder
				.informat(RDFConstants.RDFSerialization.PLAINTEXT)
				.parameter("language", language)
				.build();
	}

	/**
	 * Creates a default request to the e-Entity FREME NER service.
	 * @param text		The text to enrich (plain text).
	 * @param language  The language the text is in.
	 * @param dataSet   The data set to use for enrichment.
	 * @return        	A request for e-Entity FREME NER.
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
	 * Creates a default request to the e-Link service without input. This is used when the input is the output of
	 * another request.
	 * @param templateID	The template ID to use for linking.
	 * @return				A request for e-Link.
	 */
	public static SerializedRequest createLink(final String templateID) {
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_LINK.getUri());
		return builder
				.parameter("templateid", templateID)
				.build();
	}

	/**
	 * Creates a default request to the e-Translate service.
	 * @param text			The input text to translate, in plain text.
	 * @param sourceLang 	The source language.
	 * @param targetLang	The target language.
	 * @return				A request for e-Translate.
	 */
	public static SerializedRequest createTranslation(final String text, final String sourceLang, final String targetLang) {
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_TRANSLATION.getUri());
		return builder
				.parameter("source-lang", sourceLang)
				.parameter("target-lang", targetLang)
				.informat(RDFConstants.RDFSerialization.PLAINTEXT)
				.body(text)
				.build();
	}

	/**
	 * Creates a default request to the e-Translate service without input. This is used when the input is the output of
	 * another request.
	 * @param sourceLang 	The source language.
	 * @param targetLang	The target language.
	 * @return				A request for e-Translate.
	 */
	public static SerializedRequest createTranslation(final String sourceLang, final String targetLang) {
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_TRANSLATION.getUri());
		return builder
				.parameter("source-lang", sourceLang)
				.parameter("target-lang", targetLang)
				.build();
	}

	/**
	 * Creates a default request to the e-Terminology service without input. This is used when the input is the output of
	 * another request.
	 * @param sourceLang 	The source language.
	 * @param targetLang	The target language.
	 * @return				A request for e-Translate.
	 */
	public static SerializedRequest createTerminology(final String sourceLang, final String targetLang) {
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_TERMINOLOGY.getUri());
		return builder
				.parameter("source-lang", sourceLang)
				.parameter("target-lang", targetLang)
				.build();
	}

	/**
	 * Converts requests to JSON in the given order.
	 * @param requests The requests to convert to JSON.
	 * @return		   A JSON string representing serialized requests, which can be sent to the Pipelines API.
	 */
	public static String toJson(final SerializedRequest... requests) {
		return toJson(Arrays.asList(requests));
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
	 * @throws JsonSyntaxException	Something is wrong with the JSON syntax.
	 */
	public static List<SerializedRequest> fromJson(final String serializedRequests) {
		checkOnMembers(serializedRequests);
		SerializedRequest[] requests = gson.fromJson(serializedRequests, SerializedRequest[].class);
		for (int reqNr = 0; reqNr < requests.length; reqNr++) {
			String invalid = requests[reqNr].isValid();
			if (!invalid.isEmpty()) {
				throw new JsonSyntaxException("Request " + (reqNr + 1) + ": " + invalid);
			}
		}
		return Arrays.asList(requests);
	}

	/**
	 * Converts a pipeline template to a JSON string.
	 * @param pipeline	The pipeline template to convert.
	 * @return			A JSON string representing the pipeline template. This is id, if it is persistent, the owner name,
	 * 					the visibility (PUBLIC or PRIVATE) and the serialized requests.
	 * @throws JsonSyntaxException	Something is wrong with the JSON syntax.	.
	 */
	public static String toJson(final eu.freme.common.persistence.model.Pipeline pipeline) {
		List<SerializedRequest> serializedRequests = fromJson(pipeline.getSerializedRequests());
		Pipeline pipelineObj = new Pipeline(
				pipeline.getId(),
				pipeline.isPersistent(),
				pipeline.getOwner().getName(),
				pipeline.getVisibility().name(),
				serializedRequests);
		return gson_pretty.toJson(pipelineObj);
	}

	/**
	 * Converts a JSON string into an object containing pipeline template information.
	 * @param pipelineTemplate 	A JSON string representing the pipeline template.
	 * @return  			    The pipeline template info object.
	 */
	public static Pipeline templateFromJson(final String pipelineTemplate) {
		return gson.fromJson(pipelineTemplate, Pipeline.class);
	}

	/**
	 * Converts a list of pipeline templates to a JSON string.
	 * @param pipelines	The pipeline templates to convert.
	 * @return			A JSON string representing the pipeline templates. This is id, if it is persistent, the owner name,
	 * 					the visibility (PUBLIC or PRIVATE) and the serialized requests per pipeline.
	 * @throws JsonSyntaxException	Something is wrong with the JSON syntax.	.
	 */
	public static String templatesToJson(final List<eu.freme.common.persistence.model.Pipeline> pipelines) {
		List<Pipeline> pipelineInfos = new ArrayList<>();
		for (eu.freme.common.persistence.model.Pipeline pipeline : pipelines) {
			List<SerializedRequest> serializedRequests = fromJson(pipeline.getSerializedRequests());
			Pipeline pipelineObj = new Pipeline(
					pipeline.getId(),
					pipeline.isPersistent(),
					pipeline.getOwner().getName(),
					pipeline.getVisibility().name(),
					serializedRequests);
			pipelineInfos.add(pipelineObj);
		}
		return gson_pretty.toJson(pipelineInfos);
	}

	/**
	 * Converts a JSON string into an object containing pipeline templates information.
	 * @param pipelineTemplates 	A JSON string representing the pipeline templates.
	 * @return  			    	The pipeline template info objects.
	 */
	public static List<Pipeline> templatesFromJson(final String pipelineTemplates) {
		Pipeline[] requests = gson.fromJson(pipelineTemplates, Pipeline[].class);
		return Arrays.asList(requests);
	}

	/**
	 * Checks if all fields in the JSON string are valid field names of the {@link SerializedRequest} class. Throws an
	 * exception if not valid.
	 * @param serializedRequests	The JSON string to check; it should represent a list of {@link SerializedRequest} objects.
	 * @throws JsonSyntaxException	A field is not recognized.
	 */
	private static void checkOnMembers(final String serializedRequests) {
		Object serReqObj = gson.fromJson(serializedRequests, Object.class);
		if (! (serReqObj instanceof ArrayList)) {
			throw new JsonSyntaxException("Expected an array of requests");
		}
		ArrayList<LinkedTreeMap> requests = (ArrayList<LinkedTreeMap>)serReqObj;
		for (int reqNr = 0; reqNr < requests.size(); reqNr++) {
			LinkedTreeMap map = requests.get(reqNr);
			for (Object o : map.keySet()) {
				String fieldName = (String)o;
				if (!requestFieldNames.contains(fieldName)) {
					throw new JsonSyntaxException("Request " + (reqNr + 1) + ": field \"" + fieldName + "\" not known.");
				}
			}
		}


	}
}
