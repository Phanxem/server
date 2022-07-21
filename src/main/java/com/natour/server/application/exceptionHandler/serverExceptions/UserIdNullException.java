package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class UserIdNullException extends ServerException{
	
	private static final long CODE = 201;
	private static final String MESSAGE = "";
	
	public UserIdNullException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
}
