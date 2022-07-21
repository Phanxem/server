package com.natour.server.application.exceptionHandler.serverExceptions;

import com.natour.server.application.exceptionHandler.ServerException;

public class UserUsernameNullException extends ServerException{

	private static final long CODE = 200;
	private static final String MESSAGE = "";
	
	public UserUsernameNullException() {
		this.setCode(CODE);
		this.setMessage(MESSAGE);
	}
	
}
