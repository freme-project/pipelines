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

import eu.freme.eservices.pipelines.requests.SerializedRequest;

import java.util.List;

/**
 * <p>An object representing a pipeline template</p>
 *
 * @author Gerald Haesendonck
 */
public class Pipeline {
	private long id;
	private boolean persist;
	private String visibility;
	private String owner;
	private List<SerializedRequest> serializedRequests;

	public Pipeline(long id, boolean persist, String owner, String visibility, List<SerializedRequest> serializedRequests) {
		this.id = id;
		this.persist = persist;
		this.owner = owner;
		this.visibility = visibility;
		this.serializedRequests = serializedRequests;
	}

	public long getId() {
		return id;
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

	public List<SerializedRequest> getSerializedRequests() {
		return serializedRequests;
	}
}
