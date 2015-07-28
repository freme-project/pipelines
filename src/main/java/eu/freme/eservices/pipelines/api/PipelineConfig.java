package eu.freme.eservices.pipelines.api;

import eu.freme.eservices.pipelines.core.PipelineService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Spring configuration that defines the service.
 * @author Gerald Haesendonck
 */

@Component
@SuppressWarnings("unused")
public class PipelineConfig {

	@Bean
	public PipelineService getPipelineApi() {
		return new PipelineService();
	}
}
