/**
 * Copyright (C) 2015 Deutsches Forschungszentrum für Künstliche Intelligenz (http://freme-project.eu)
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
package eu.freme.eservices.pipelines;

import org.junit.After;
import org.junit.Before;

/**
 * @author Gerald Haesendonck
 */
public abstract class LocalServerTestBase {
	protected Run webService;

	@Before
	public void setup() {
		// server
		webService = new Run();
		webService.run();
	}

	@After
	public void tearDown() {
		webService.close();
	}
}
