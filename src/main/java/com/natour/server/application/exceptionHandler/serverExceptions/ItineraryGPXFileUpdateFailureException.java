package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class ItineraryGPXFileUpdateFailureException extends ServerException{
	private static final long CODE = 402;
	private static final String MESSAGE = "";
	
	public ItineraryGPXFileUpdateFailureException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
}
