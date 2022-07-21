package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class ItineraryNotFoundException extends ServerException{
	private static final long CODE = 101;
	private static final String MESSAGE = "";

	public ItineraryNotFoundException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
}
