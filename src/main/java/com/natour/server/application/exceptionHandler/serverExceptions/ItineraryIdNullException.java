package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class ItineraryIdNullException extends ServerException{
	
	private static final long CODE = 204;
	private static final String MESSAGE = "";
	
	public ItineraryIdNullException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}

}
