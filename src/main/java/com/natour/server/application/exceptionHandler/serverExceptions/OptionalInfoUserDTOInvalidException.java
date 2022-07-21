package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class OptionalInfoUserDTOInvalidException extends ServerException{
	
	private static final long CODE = 600;
	private static final String MESSAGE = "";
	
	public OptionalInfoUserDTOInvalidException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
}
