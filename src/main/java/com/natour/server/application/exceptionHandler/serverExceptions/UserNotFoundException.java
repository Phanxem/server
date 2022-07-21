package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class UserNotFoundException extends ServerException{
	
	private static final long CODE = 100;
	private static final String MESSAGE = "";
	
	public UserNotFoundException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
}
