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
package eu.freme.eservices.pipelines.serialization;

/**
 * <p>An object representing a pipeline template</p>
 *
 * @author Gerald Haesendonck
 */
public class Pipeline {
	private long id;
	private String label;
	private String description;
	private boolean persist;
	private String visibility;
	private String owner;
	private String serializedRequests;

	public Pipeline(final String label, final String description, final String serializedRequests) {
		this(-1, label, description, false, null, null, serializedRequests);
	}

	public Pipeline(long id,
					final String label,
					final String description,
					boolean persist,
					final String owner,
					final String visibility,
					final String serializedRequests) {
		this.id = id;
		this.label = label;
		this.description = description;
		this.persist = persist;
		this.owner = owner;
		this.visibility = visibility;
		this.serializedRequests = serializedRequests;
	}

	public long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getDescription() {
		return description;
	}

	public boolean isPersist() {
		return persist;
	}

	public String getVisibility() {
		return visibility;
	}

	public String getOwner() {
		return owner;
	}

	public String getSerializedRequests() {
		return serializedRequests;
	}

	public void setSerializedRequests(String serializedRequests) {
		this.serializedRequests = serializedRequests;
	}

	public String isValid() {
		if (label == null) {
			return "No label given.";
		}
		if (description == null) {
			return "No description given.";
		}
		if (serializedRequests == null) {
			return "No requests given.";
		}
		return "";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Pipeline pipeline = (Pipeline) o;

		if (id != pipeline.id) return false;
		if (persist != pipeline.persist) return false;
		if (!label.equals(pipeline.label)) return false;
		if (!description.equals(pipeline.description)) return false;
		if (visibility != null ? !visibility.equals(pipeline.visibility) : pipeline.visibility != null) return false;
		if (owner != null ? !owner.equals(pipeline.owner) : pipeline.owner != null) return false;
		return serializedRequests.equals(pipeline.serializedRequests);

	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + label.hashCode();
		result = 31 * result + description.hashCode();
		result = 31 * result + (persist ? 1 : 0);
		result = 31 * result + (visibility != null ? visibility.hashCode() : 0);
		result = 31 * result + (owner != null ? owner.hashCode() : 0);
		result = 31 * result + serializedRequests.hashCode();
		return result;
	}
}
