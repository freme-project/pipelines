package eu.freme.eservices.pipelines.requests;

/**
 * @author Gerald Haesendonck
 */
public enum ServiceConstants {
	E_ENTITY_SPOTLIGHT("/e-entity/dbpedia-spotlight/documents"),
	E_ENTITY_FREME_NER("/e-entity/freme-ner/documents");

	private final String uri;

	ServiceConstants(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}
}
