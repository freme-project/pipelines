package eu.freme.eservices.pipelines.core;

import java.util.Map;

/**
 * <p>Some metadata about the response. Only timings at this moment.</p>
 * <p>
 * <p>Copyright 2015 MMLab, UGent</p>
 *
 * @author Gerald Haesendonck
 */
public class Metadata {
	private final Map<String, Long> serviceDurations;
	private final long totalDuration;

	public Metadata(Map<String, Long> serviceToDuration, long totalDuration) {
		this.serviceDurations = serviceToDuration;
		this.totalDuration = totalDuration;
	}
}
