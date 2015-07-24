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
