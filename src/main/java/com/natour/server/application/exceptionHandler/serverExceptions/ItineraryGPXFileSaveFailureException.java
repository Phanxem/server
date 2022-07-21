package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class ItineraryGPXFileSaveFailureException extends ServerException{
	private static final long CODE = 501;
	private static final String MESSAGE = "";
	
	public ItineraryGPXFileSaveFailureException(Exception e) {
		super(e);
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
	public ItineraryGPXFileSaveFailureException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
		
	}
}
