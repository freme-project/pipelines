package eu.freme.eservices.pipelines.core;

import org.springframework.http.HttpStatus;

/**
 * @author Gerald Haesendonck
 */
public class ServiceException extends Exception {
	final HttpStatus status;

	public ServiceException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}
}
