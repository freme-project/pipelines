package eu.freme.eservices.pipelines.core;

import java.util.Map;

/**
 * <p>A PipelineResponse with some extra stats.</p>
 * <p>
 * <p>Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
public class WrappedPipelineResponse {
	private final PipelineResponse pipelineResponse;
	private final Map<String, Long> serviceToDuration;
	private final long totalDuration;

	public WrappedPipelineResponse(PipelineResponse pipelineResponse, Map<String, Long> serviceToDuration, long totalDuration) {
		this.pipelineResponse = pipelineResponse;
		this.serviceToDuration = serviceToDuration;
		this.totalDuration = totalDuration;
	}

	public PipelineResponse getPipelineResponse() {
		return pipelineResponse;
	}
}
