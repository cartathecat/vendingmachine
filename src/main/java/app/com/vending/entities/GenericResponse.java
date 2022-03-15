package app.com.vending.entities;

/**
 * GenericResponse
 * 
 * March 2022
 * 
 * A generic response object
 * 
 */

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class GenericResponse {

	private String message;

	public GenericResponse() {
		super();
	}

	public GenericResponse(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return String.format("GenericResponse [message=%s]", message);
	}
	
}
